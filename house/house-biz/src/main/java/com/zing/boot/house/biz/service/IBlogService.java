package com.zing.boot.house.biz.service;

import com.zing.boot.house.common.model.Blog;
import com.zing.boot.house.common.page.PageData;
import com.zing.boot.house.common.page.PageParams;

public interface IBlogService {

    PageData<Blog> queryBlog(Blog query, PageParams params);

    Blog queryOneBlog(int id);
}
