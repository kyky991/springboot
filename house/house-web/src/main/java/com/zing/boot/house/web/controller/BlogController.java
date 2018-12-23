package com.zing.boot.house.web.controller;

import com.zing.boot.house.biz.service.IBlogService;
import com.zing.boot.house.biz.service.ICommentService;
import com.zing.boot.house.biz.service.IRecommendService;
import com.zing.boot.house.common.constants.CommonConstants;
import com.zing.boot.house.common.model.Blog;
import com.zing.boot.house.common.model.Comment;
import com.zing.boot.house.common.model.House;
import com.zing.boot.house.common.page.PageData;
import com.zing.boot.house.common.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private IBlogService blogService;

    @Autowired
    private IRecommendService recommendService;

    @Autowired
    private ICommentService commentService;

    @RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
    public String list(Integer pageSize, Integer pageNum, Blog query, ModelMap modelMap) {
        PageData<Blog> ps = blogService.queryBlog(query, PageParams.build(pageSize, pageNum));
        List<House> houses = recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", houses);
        modelMap.put("ps", ps);
        return "/blog/listing";
    }

    @RequestMapping(value = "/detail", method = {RequestMethod.POST, RequestMethod.GET})
    public String blogDetail(int id, ModelMap modelMap) {
        Blog blog = blogService.queryOneBlog(id);
        List<Comment> comments = commentService.getBlogComments(id, 8);
        List<House> houses = recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", houses);
        modelMap.put("blog", blog);
        modelMap.put("commentList", comments);
        return "/blog/detail";
    }

}
