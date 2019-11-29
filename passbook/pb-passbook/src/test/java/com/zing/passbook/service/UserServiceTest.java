package com.zing.passbook.service;

import com.alibaba.fastjson.JSON;
import com.zing.passbook.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Zing
 * @date 2019-11-29
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private IUserService userService;

    @Test
    public void createUser() throws Exception {
        User user = new User();
        user.setBaseInfo(new User.BaseInfo("Zing", 18, "M"));
        user.setOtherInfo(new User.OtherInfo("123456789", "kkp"));

        log.info(JSON.toJSONString(user));

        userService.createUser(user);
    }
}