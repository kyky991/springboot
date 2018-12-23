package com.zing.boot.house.biz.mapper;

import com.zing.boot.house.common.model.Blog;
import com.zing.boot.house.common.page.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface BlogMapper {

  List<Blog> selectBlog(@Param("blog") Blog query, @Param("pageParams") PageParams params);

  Long selectBlogCount(Blog query);

}
