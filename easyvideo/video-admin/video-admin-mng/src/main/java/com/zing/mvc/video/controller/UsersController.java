package com.zing.mvc.video.controller;

import com.zing.mvc.video.pojo.AdminUser;
import com.zing.mvc.video.pojo.User;
import com.zing.mvc.video.service.IUsersService;
import com.zing.mvc.video.utils.JSONResult;
import com.zing.mvc.video.utils.PagedResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private IUsersService usersService;

    @GetMapping("/showList")
    public String showList() {
        return "users/usersList";
    }

    @PostMapping("/list")
    @ResponseBody
    public PagedResult list(User user , Integer page) {
        return usersService.queryUsers(user, page == null ? 1 : page, 10);
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("login")
    @ResponseBody
    public JSONResult userLogin(String username, String password,
                                HttpServletRequest request) {
        // TODO 模拟登陆
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JSONResult.errorMap("用户名和密码不能为空");
        } else if (username.equals("root") && password.equals("123456")) {
            String token = UUID.randomUUID().toString();
            AdminUser user = new AdminUser(username, password, token);
            request.getSession().setAttribute("sessionUser", user);
            return JSONResult.ok();
        }

        return JSONResult.errorMsg("登陆失败，请重试...");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("sessionUser");
        return "login";
    }
}
