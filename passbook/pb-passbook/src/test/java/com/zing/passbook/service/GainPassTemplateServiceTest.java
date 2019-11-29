package com.zing.passbook.service;

import com.alibaba.fastjson.JSON;
import com.zing.passbook.vo.GainPassTemplateRequest;
import com.zing.passbook.vo.PassTemplate;
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
public class GainPassTemplateServiceTest extends AbstractServiceTest {

    @Autowired
    private IGainPassTemplateService gainPassTemplateService;

    @Test
    public void gainPassTemplate() throws Exception {
        PassTemplate target = new PassTemplate();
        target.setId(1L);
        target.setTitle("西兰花-2");
        target.setHasToken(true);
        Response response = gainPassTemplateService.gainPassTemplate(new GainPassTemplateRequest(userId, target));
        log.info(JSON.toJSONString(response));
    }
}