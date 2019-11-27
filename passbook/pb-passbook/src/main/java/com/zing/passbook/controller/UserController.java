package com.zing.passbook.controller;

import com.zing.passbook.log.LogConstants;
import com.zing.passbook.log.LogGenerator;
import com.zing.passbook.service.IUserService;
import com.zing.passbook.vo.Response;
import com.zing.passbook.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Zing
 * @date 2019-11-27
 */
@Slf4j
@RestController
@RequestMapping("/passbook")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * <h2>创建用户</h2>
     *
     * @param user {@link User}
     * @return {@link Response}
     */
    @PostMapping("/createUser")
    public Response createUser(@RequestBody User user) throws Exception {
        LogGenerator.log(httpServletRequest, -1L, LogConstants.ActionName.CREATE_USER, user);
        return userService.createUser(user);
    }

}
