package com.zing.boot.house.biz.service.impl;

import com.google.common.collect.Lists;
import com.zing.boot.house.biz.service.ICityService;
import com.zing.boot.house.common.model.City;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements ICityService {

    @Override
    public List<City> getAllCities() {
        City city = new City();
        city.setCityCode("110000");
        city.setCityName("北京");
        city.setId(1);
        return Lists.newArrayList(city);
    }
}
