package com.zing.passbook.service;

import com.alibaba.fastjson.JSON;
import com.zing.passbook.vo.Pass;
import com.zing.passbook.vo.PassInfo;
import com.zing.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Zing
 * @date 2019-11-29
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserPassServiceTest extends AbstractServiceTest {

    @Autowired
    private IUserPassService userPassService;

    @Test
    public void getUserPassInfo() throws Exception {
        Response<List<PassInfo>> response = userPassService.getUserPassInfo(userId);
        log.info(JSON.toJSONString(response));
    }

    @Test
    public void getUserUsedPassInfo() throws Exception {
        Response<List<PassInfo>> response = userPassService.getUserUsedPassInfo(userId);
        log.info(JSON.toJSONString(response));
    }

    @Test
    public void getUserAllPassInfo() throws Exception {
        Response<List<PassInfo>> response = userPassService.getUserAllPassInfo(userId);
        log.info(JSON.toJSONString(response));
    }

    @Test
    public void userUsePass() {
        Pass pass = new Pass();
        pass.setUserId(userId);
        pass.setTemplateId("364ad9f9e53774e59e40c92b1eb44ba8");
        Response response = userPassService.userUsePass(pass);
        log.info(JSON.toJSONString(response));
    }
}