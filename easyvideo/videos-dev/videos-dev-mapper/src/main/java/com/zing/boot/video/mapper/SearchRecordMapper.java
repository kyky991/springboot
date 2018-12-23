package com.zing.boot.video.mapper;

import com.zing.boot.video.pojo.SearchRecord;
import com.zing.boot.video.utils.MyMapper;

import java.util.List;

public interface SearchRecordMapper extends MyMapper<SearchRecord> {

    List<String> getHotWords();

}