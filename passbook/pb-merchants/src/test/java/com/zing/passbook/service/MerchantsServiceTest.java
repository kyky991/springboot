package com.zing.passbook.service;

import com.alibaba.fastjson.JSON;
import com.zing.passbook.vo.CreateMerchantsRequest;
import com.zing.passbook.vo.PassTemplate;
import com.zing.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author Zing
 * @date 2019-11-29
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MerchantsServiceTest {

    @Autowired
    private IMerchantsService merchantsService;

    @Test
    public void createMerchant() {
        CreateMerchantsRequest request = new CreateMerchantsRequest();
        request.setName("西兰花");
        request.setLogoUrl("www.zing.com");
        request.setBusinessLicenseUrl("www.zing.com");
        request.setPhone("123456789");
        request.setAddress("地球");

        Response response = merchantsService.createMerchants(request);
        log.info(JSON.toJSONString(response));
    }

    @Test
    public void buildMerchantsInfoById() {
        Response response = merchantsService.buildMerchantsInfoById(1L);
        log.info(JSON.toJSONString(response));
    }

    @Test
    public void dropPassTemplate() {
        PassTemplate passTemplate = new PassTemplate();
        passTemplate.setId(1L);
        passTemplate.setTitle("西兰花-2");
        passTemplate.setSummary("简介: 西兰花");
        passTemplate.setDesc("详情: 西兰花");
        passTemplate.setLimit(10000L);
        passTemplate.setHasToken(true);
        passTemplate.setBackground(2);
        passTemplate.setStart(DateUtils.addDays(new Date(), -10));
        passTemplate.setEnd(DateUtils.addDays(new Date(), 10));

        Response response = merchantsService.dropPassTemplate(passTemplate);

        log.info(JSON.toJSONString(response));
    }
}