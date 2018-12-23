package com.zing.boot.house.biz.service;

import com.zing.boot.house.common.model.Comment;

import java.util.List;

public interface ICommentService {

    void addHouseComment(Long houseId, String content, Long userId);

    List<Comment> getHouseComments(long houseId, int size);

    List<Comment> getBlogComments(long blogId, int size);

    void addBlogComment(int blogId, String content, Long userId);

}
