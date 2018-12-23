package com.zing.boot.house.web.controller;

import com.zing.boot.house.biz.service.ICommentService;
import com.zing.boot.house.common.model.User;
import com.zing.boot.house.web.interceptor.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @RequestMapping(value = "/leaveComment", method = {RequestMethod.POST, RequestMethod.GET})
    public String leaveComment(String content, Long houseId) {
        User user = UserContext.getUser();
        Long userId = user.getId();
        commentService.addHouseComment(houseId, content, userId);
        return "redirect:/house/detail?id=" + houseId;
    }

    @RequestMapping(value = "/leaveBlogComment", method = {RequestMethod.POST, RequestMethod.GET})
    public String leaveBlogComment(String content, Integer blogId) {
        User user = UserContext.getUser();
        Long userId = user.getId();
        commentService.addBlogComment(blogId, content, userId);
        return "redirect:/blog/detail?id=" + blogId;
    }

}
