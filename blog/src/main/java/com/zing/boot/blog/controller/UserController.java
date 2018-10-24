package com.zing.boot.blog.controller;

import com.zing.boot.blog.pojo.Authority;
import com.zing.boot.blog.pojo.User;
import com.zing.boot.blog.service.IAuthorityService;
import com.zing.boot.blog.service.IUserService;
import com.zing.boot.blog.util.ConstraintViolationExceptionHandler;
import com.zing.boot.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuthorityService authorityService;

    @GetMapping
    public ModelAndView list(@RequestParam(value = "async", required = false) boolean async,
                             @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                             @RequestParam(value = "name", required = false, defaultValue = "") String name,
                             Model model) {

        Pageable pageable = new PageRequest(pageIndex, pageSize);
        Page<User> page = userService.listUsersByNameLike(name, pageable);
        List<User> list = page.getContent();    // 当前所在页面数据列表

        model.addAttribute("page", page);
        model.addAttribute("userList", list);

        return new ModelAndView(async ? "users/list :: #mainContainerRepleace" : "users/list", "userModel", model);
    }

    /**
     * 获取 form 表单页面
     *
     * @param model
     * @return
     */
    @GetMapping("/add")
    public ModelAndView createForm(Model model) {
        model.addAttribute("user", new User(null, null, null, null));
        return new ModelAndView("users/add", "userModel", model);
    }

    /**
     * 新建用户
     *
     * @param user
     * @param authorityId
     * @return
     */
    @PostMapping
    public ResponseEntity<Response> create(User user, Long authorityId) {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(authorityId));
        user.setAuthorities(authorities);

        if (user.getId() == null) {
            user.setEncodePassword(user.getPassword()); // 加密密码
        } else {
            // 判断密码是否做了变更
            User originalUser = userService.getUserById(user.getId());
            String rawPassword = originalUser.getPassword();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodePassword = encoder.encode(user.getPassword());
            boolean isMatch = encoder.matches(rawPassword, encodePassword);
            if (isMatch) {
                user.setPassword(user.getPassword());
            } else {
                user.setEncodePassword(user.getPassword());
            }
        }

        try {
            userService.saveUser(user);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", user));
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id) {
        try {
            userService.removeUser(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }

    /**
     * 获取修改用户的界面，及数据
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = "edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return new ModelAndView("users/edit", "userModel", model);
    }
}
