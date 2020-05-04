package com.zing.springboot.service.impl;

import com.zing.springboot.service.ISplitService;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SplitServiceImpl implements ISplitService {

    @Override
    public List<String> split(String value) {
        return Stream.of(StringUtils.split(value, ",")).collect(Collectors.toList());
    }
}
