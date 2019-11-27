package com.zing.passbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import com.zing.passbook.constant.Constants;
import com.zing.passbook.constant.PassStatus;
import com.zing.passbook.entity.Merchants;
import com.zing.passbook.mapper.PassRowMapper;
import com.zing.passbook.repository.MerchantsRepository;
import com.zing.passbook.service.IUserPassService;
import com.zing.passbook.vo.Pass;
import com.zing.passbook.vo.PassInfo;
import com.zing.passbook.vo.PassTemplate;
import com.zing.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zing
 * @date 2019-11-27
 */
@Slf4j
@Service
public class UserPassServiceImpl implements IUserPassService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private MerchantsRepository merchantsRepository;

    @Override
    public Response<List<PassInfo>> getUserPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.UNUSED);
    }

    @Override
    public Response<List<PassInfo>> getUserUsedPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.USED);
    }

    @Override
    public Response<List<PassInfo>> getUserAllPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.ALL);
    }

    @Override
    public Response userUsePass(Pass pass) {
        // 根据 userId 构造行键前缀
        byte[] prefix = Bytes.toBytes(new StringBuilder(String.valueOf(pass.getUserId())).reverse().toString());

        byte[] FAMILY_I = Constants.PassTable.FAMILY_I.getBytes();
        byte[] TEMPLATE_ID = Constants.PassTable.TEMPLATE_ID.getBytes();
        byte[] CON_DATE = Constants.PassTable.CON_DATE.getBytes();

        List<Filter> filters = new ArrayList<>();
        filters.add(new PrefixFilter(prefix));
        filters.add(new SingleColumnValueFilter(
                FAMILY_I,
                TEMPLATE_ID,
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes(pass.getTemplateId())));
        filters.add(new SingleColumnValueFilter(
                FAMILY_I,
                CON_DATE,
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes("-1")));
        Scan scan = new Scan();
        scan.setFilter(new FilterList(filters));

        List<Pass> passes = hbaseTemplate.find(Constants.PassTable.TABLE_NAME, scan, new PassRowMapper());
        if (passes == null || passes.size() != 1) {
            log.error("UserUsePass Error: {}", JSON.toJSONString(pass));
            return Response.error("UserUsePass Error");
        }

        List<Mutation> dataList = new ArrayList<>();
        Put put = new Put(passes.get(0).getRowKey().getBytes());
        put.addColumn(FAMILY_I, CON_DATE, Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(new Date())));
        dataList.add(put);

        hbaseTemplate.saveOrUpdates(Constants.PassTable.TABLE_NAME, dataList);

        return Response.success();
    }

    /**
     * <h2>根据优惠券状态获取优惠券信息</h2>
     *
     * @param userId 用户 id
     * @param status {@link PassStatus}
     * @return {@link Response}
     */
    private Response<List<PassInfo>> getPassInfoByStatus(Long userId, PassStatus status) throws Exception {
        // 根据 userId 构造行键前缀
        byte[] prefix = Bytes.toBytes(new StringBuilder(String.valueOf(userId)).reverse().toString());

        CompareFilter.CompareOp op = status == PassStatus.UNUSED ? CompareFilter.CompareOp.EQUAL : CompareFilter.CompareOp.NOT_EQUAL;

        List<Filter> filters = new ArrayList<>();

        // 1. 行键前缀过滤器, 找到特定用户的优惠券
        filters.add(new PrefixFilter(prefix));
        // 2. 基于列单元值的过滤器, 找到未使用的优惠券
        if (status != PassStatus.ALL) {
            filters.add(new SingleColumnValueFilter(
                    Constants.PassTable.FAMILY_I.getBytes(),
                    Constants.PassTable.CON_DATE.getBytes(),
                    op,
                    Bytes.toBytes("-1"))
            );
        }

        Scan scan = new Scan();
        scan.setFilter(new FilterList(filters));
        List<Pass> passes = hbaseTemplate.find(Constants.PassTable.TABLE_NAME, scan, new PassRowMapper());
        Map<String, PassTemplate> passTemplateMap = buildPassTemplateMap(passes);
        Map<Long, Merchants> merchantsMap = buildMerchantsMap(new ArrayList<>(passTemplateMap.values()));

        List<PassInfo> results = new ArrayList<>();
        for (Pass pass : passes) {
            PassTemplate passTemplate = passTemplateMap.getOrDefault(pass.getTemplateId(), null);
            if (passTemplate == null) {
                log.error("PassTemplate Null : {}", pass.getTemplateId());
                continue;
            }

            Merchants merchants = merchantsMap.getOrDefault(passTemplate.getId(), null);
            if (merchants == null) {
                log.error("Merchants Null : {}", passTemplate.getId());
                continue;
            }

            results.add(new PassInfo(pass, passTemplate, merchants));
        }
        return Response.success(results);
    }

    /**
     * <h2>通过获取的 Passes 对象构造 Map</h2>
     *
     * @param passes {@link Pass}
     * @return Map {@link PassTemplate}
     */
    private Map<String, PassTemplate> buildPassTemplateMap(List<Pass> passes) throws Exception {
        String[] patterns = new String[]{"yyyy-MM-dd"};

        byte[] FAMILY_B = Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B);
        byte[] ID = Bytes.toBytes(Constants.PassTemplateTable.ID);
        byte[] TITLE = Bytes.toBytes(Constants.PassTemplateTable.TITLE);
        byte[] SUMMARY = Bytes.toBytes(Constants.PassTemplateTable.SUMMARY);
        byte[] DESC = Bytes.toBytes(Constants.PassTemplateTable.DESC);
        byte[] HAS_TOKEN = Bytes.toBytes(Constants.PassTemplateTable.HAS_TOKEN);
        byte[] BACKGROUND = Bytes.toBytes(Constants.PassTemplateTable.BACKGROUND);

        byte[] FAMILY_C = Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C);
        byte[] LIMIT = Bytes.toBytes(Constants.PassTemplateTable.LIMIT);
        byte[] START = Bytes.toBytes(Constants.PassTemplateTable.START);
        byte[] END = Bytes.toBytes(Constants.PassTemplateTable.END);

        List<String> templateIds = passes.stream().map(Pass::getTemplateId).collect(Collectors.toList());

        List<Get> templateGets = templateIds.stream().map(id -> new Get(Bytes.toBytes(id))).collect(Collectors.toList());

        Result[] templateResults = hbaseTemplate.getConnection()
                .getTable(TableName.valueOf(Constants.PassTemplateTable.TABLE_NAME))
                .get(templateGets);

        // 构造 PassTemplateId -> PassTemplate Object 的 Map, 用于构造 PassInfo
        Map<String, PassTemplate> results = new HashMap<>();
        for (Result result : templateResults) {
            PassTemplate passTemplate = new PassTemplate();
            passTemplate.setId(Bytes.toLong(result.getValue(FAMILY_B, ID)));
            passTemplate.setTitle(Bytes.toString(result.getValue(FAMILY_B, TITLE)));
            passTemplate.setSummary(Bytes.toString(result.getValue(FAMILY_B, SUMMARY)));
            passTemplate.setDesc(Bytes.toString(result.getValue(FAMILY_B, DESC)));
            passTemplate.setHasToken(Bytes.toBoolean(result.getValue(FAMILY_B, HAS_TOKEN)));
            passTemplate.setBackground(Bytes.toInt(result.getValue(FAMILY_B, BACKGROUND)));

            passTemplate.setLimit(Bytes.toLong(result.getValue(FAMILY_C, LIMIT)));
            passTemplate.setStart(DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_C, START)), patterns));
            passTemplate.setEnd(DateUtils.parseDate(Bytes.toString(result.getValue(FAMILY_C, END)), patterns));

            results.put(Bytes.toString(result.getRow()), passTemplate);
        }
        return results;
    }

    /**
     * <h2>通过获取的 PassTemplate 对象构造 Merchants Map</h2>
     *
     * @param passTemplates {@link PassTemplate}
     * @return {@link Merchants}
     */
    private Map<Long, Merchants> buildMerchantsMap(List<PassTemplate> passTemplates) {
        List<Long> ids = passTemplates.stream().map(PassTemplate::getId).collect(Collectors.toList());
        List<Merchants> list = merchantsRepository.findByIdIn(ids);
        return list.stream().collect(Collectors.toMap(Merchants::getId, m -> m));
    }
}
