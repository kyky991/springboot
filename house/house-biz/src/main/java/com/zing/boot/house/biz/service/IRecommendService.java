package com.zing.boot.house.biz.service;

import com.zing.boot.house.common.model.House;

import java.util.List;

public interface IRecommendService {

    void increase(Long id);

    List<Long> getHot();

    List<House> getHotHouse(Integer size);

    List<House> getLatest();

}
