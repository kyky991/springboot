package com.zing.passbook.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zing.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Zing
 * @date 2019-11-29
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class InventoryServiceTest extends AbstractServiceTest {

    @Autowired
    private IInventoryService inventoryService;

    @Test
    public void getInventoryInfo() throws Exception {
        Response response = inventoryService.getInventoryInfo(userId);
        log.info(JSON.toJSONString(response, SerializerFeature.DisableCircularReferenceDetect));
    }
}