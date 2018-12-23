package com.zing.mvc.video.mapper;

import com.zing.mvc.video.pojo.Video;
import com.zing.mvc.video.pojo.VideoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoMapper {
    int countByExample(VideoExample example);

    int deleteByExample(VideoExample example);

    int deleteByPrimaryKey(String id);

    int insert(Video record);

    int insertSelective(Video record);

    List<Video> selectByExample(VideoExample example);

    Video selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Video record, @Param("example") VideoExample example);

    int updateByExample(@Param("record") Video record, @Param("example") VideoExample example);

    int updateByPrimaryKeySelective(Video record);

    int updateByPrimaryKey(Video record);
}