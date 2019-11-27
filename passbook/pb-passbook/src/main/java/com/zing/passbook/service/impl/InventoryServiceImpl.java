package com.zing.passbook.service.impl;

import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import com.zing.passbook.constant.Constants;
import com.zing.passbook.entity.Merchants;
import com.zing.passbook.mapper.PassTemplateRowMapper;
import com.zing.passbook.repository.MerchantsRepository;
import com.zing.passbook.service.IInventoryService;
import com.zing.passbook.service.IUserPassService;
import com.zing.passbook.utils.RowKeyGenUtils;
import com.zing.passbook.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.LongComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Zing
 * @date 2019-11-27
 */
@Slf4j
@Service
public class InventoryServiceImpl implements IInventoryService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private MerchantsRepository merchantsRepository;

    @Autowired
    private IUserPassService userPassService;

    @Override
    public Response getInventoryInfo(Long userId) throws Exception {
        Response<List<PassInfo>> response = userPassService.getUserAllPassInfo(userId);
        List<PassInfo> passInfos = response.getData();

        List<String> excludeIds = passInfos.stream()
                .map(PassInfo::getPassTemplate)
                .map(RowKeyGenUtils::genPassTemplateRowKey)
                .collect(Collectors.toList());
        return Response.success(new InventoryResponse(userId, buildPassTemplateInfo(getAvailablePassTemplate(excludeIds))));
    }

    /**
     * <h2>获取系统中可用的优惠券</h2>
     *
     * @param excludeIds 需要排除的优惠券 ids
     * @return {@link PassTemplate}
     */
    private List<PassTemplate> getAvailablePassTemplate(List<String> excludeIds) {
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);

        filterList.addFilter(new SingleColumnValueFilter(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                CompareFilter.CompareOp.GREATER,
                new LongComparator(0L)
        ));

        filterList.addFilter(new SingleColumnValueFilter(
                Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes("-1")
        ));

        Scan scan = new Scan();
        scan.setFilter(filterList);
        List<PassTemplate> templates = hbaseTemplate.find(Constants.PassTemplateTable.TABLE_NAME, scan, new PassTemplateRowMapper());

        List<PassTemplate> availablePassTemplates = new ArrayList<>();

        Date now = new Date();

        for (PassTemplate template : templates) {
            if (excludeIds.contains(RowKeyGenUtils.genPassTemplateRowKey(template))) {
                continue;
            }

            if (now.getTime() >= template.getStart().getTime() && now.getTime() <= template.getEnd().getTime()) {
                availablePassTemplates.add(template);
            }
        }
        return availablePassTemplates;
    }

    /**
     * <h2>构造优惠券的信息</h2>
     *
     * @param passTemplates {@link PassTemplate}
     * @return {@link PassTemplateInfo}
     */
    private List<PassTemplateInfo> buildPassTemplateInfo(List<PassTemplate> passTemplates) {
        List<Long> merchantsIds = passTemplates.stream().map(PassTemplate::getId).collect(Collectors.toList());
        List<Merchants> merchants = merchantsRepository.findByIdIn(merchantsIds);

        Map<Long, Merchants> merchantsMap = merchants.stream().collect(Collectors.toMap(Merchants::getId, m -> m));

        List<PassTemplateInfo> results = new ArrayList<>(passTemplates.size());
        for (PassTemplate passTemplate : passTemplates) {
            Merchants m = merchantsMap.getOrDefault(passTemplate.getId(), null);
            if (m == null) {
                log.error("Merchants Error: {}", passTemplate.getId());
                continue;
            }

            results.add(new PassTemplateInfo(passTemplate, m));
        }
        return results;
    }
}
