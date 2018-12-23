package com.zing.mvc.video.mapper;

import com.zing.mvc.video.pojo.UserReport;
import com.zing.mvc.video.pojo.UserReportExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserReportMapper {
    int countByExample(UserReportExample example);

    int deleteByExample(UserReportExample example);

    int deleteByPrimaryKey(String id);

    int insert(UserReport record);

    int insertSelective(UserReport record);

    List<UserReport> selectByExample(UserReportExample example);

    UserReport selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") UserReport record, @Param("example") UserReportExample example);

    int updateByExample(@Param("record") UserReport record, @Param("example") UserReportExample example);

    int updateByPrimaryKeySelective(UserReport record);

    int updateByPrimaryKey(UserReport record);
}