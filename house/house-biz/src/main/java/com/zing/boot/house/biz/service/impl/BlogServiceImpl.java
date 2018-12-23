package com.zing.boot.house.biz.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.zing.boot.house.biz.mapper.BlogMapper;
import com.zing.boot.house.biz.service.IBlogService;
import com.zing.boot.house.common.model.Blog;
import com.zing.boot.house.common.page.PageData;
import com.zing.boot.house.common.page.PageParams;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements IBlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public PageData<Blog> queryBlog(Blog query, PageParams params) {
        List<Blog> blogs = blogMapper.selectBlog(query, params);
        populate(blogs);
        Long count = blogMapper.selectBlogCount(query);
        return PageData.buildPage(blogs, count, params.getPageSize(), params.getPageNum());
    }

    public void populate(List<Blog> blogs) {
        if (!blogs.isEmpty()) {
            blogs.forEach(blog -> {
                String stripped = Jsoup.parse(blog.getContent()).text();
                blog.setDigest(stripped.substring(0, Math.min(stripped.length(), 40)));
                String tags = blog.getTags();
                blog.getTagList().addAll(Lists.newArrayList(Splitter.on(",").split(tags)));
            });
        }
    }

    @Override
    public Blog queryOneBlog(int id) {
        Blog query = new Blog();
        query.setId(id);
        List<Blog> blogs = blogMapper.selectBlog(query, new PageParams(1, 1));
        if (!blogs.isEmpty()) {
            return blogs.get(0);
        }
        return null;
    }
}
