package com.zing.boot.video.mapper;

import com.zing.boot.video.pojo.Comment;
import com.zing.boot.video.pojo.vo.CommentVO;
import com.zing.boot.video.utils.MyMapper;

import java.util.List;

public interface CommentMapperCustom extends MyMapper<Comment> {

    List<CommentVO> queryComments(String videoId);
}
