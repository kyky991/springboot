package com.zing.passbook.service.impl;

import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import com.zing.passbook.constant.Constants;
import com.zing.passbook.service.IUserService;
import com.zing.passbook.vo.Response;
import com.zing.passbook.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zing
 * @date 2019-11-27
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Response createUser(User user) throws Exception {
        byte[] FAMILY_B = Constants.UserTable.FAMILY_B.getBytes();
        byte[] NAME = Constants.UserTable.NAME.getBytes();
        byte[] AGE = Constants.UserTable.AGE.getBytes();
        byte[] GENDER = Constants.UserTable.GENDER.getBytes();

        byte[] FAMILY_O = Constants.UserTable.FAMILY_O.getBytes();
        byte[] PHONE = Constants.UserTable.PHONE.getBytes();
        byte[] ADDRESS = Constants.UserTable.ADDRESS.getBytes();

        Long count = redisTemplate.opsForValue().increment(Constants.USE_COUNT_REDIS_KEY, 1);
        Long userId = getUserId(count);

        Put put = new Put(Bytes.toBytes(userId));
        put.addColumn(FAMILY_B, NAME, Bytes.toBytes(user.getBaseInfo().getName()));
        put.addColumn(FAMILY_B, AGE, Bytes.toBytes(user.getBaseInfo().getAge()));
        put.addColumn(FAMILY_B, GENDER, Bytes.toBytes(user.getBaseInfo().getGender()));

        put.addColumn(FAMILY_O, PHONE, Bytes.toBytes(user.getOtherInfo().getPhone()));
        put.addColumn(FAMILY_O, ADDRESS, Bytes.toBytes(user.getOtherInfo().getAddress()));

        List<Mutation> dataList = new ArrayList<>();
        dataList.add(put);

        hbaseTemplate.saveOrUpdates(Constants.UserTable.TABLE_NAME, dataList);

        user.setId(userId);
        return Response.success(user);
    }

    /**
     * <h2>生成 userId</h2>
     *
     * @param prefix 当前用户数
     * @return 用户 id
     */
    private Long getUserId(Long prefix) {
        return Long.valueOf(prefix + RandomStringUtils.randomNumeric(5));
    }
}
