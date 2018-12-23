package com.zing.boot.house.biz.service;

import com.zing.boot.house.common.constants.HouseUserType;
import com.zing.boot.house.common.model.*;
import com.zing.boot.house.common.page.PageData;
import com.zing.boot.house.common.page.PageParams;

import java.util.List;

public interface IHouseService {

    PageData<House> queryHouse(House query, PageParams pageParams);

    List<House> queryAndSetImg(House query, PageParams pageParams);

    List<Community> getAllCommunities();

    void addHouse(House house, User user);

    void bindUser2House(Long houseId, Long userId, boolean collect);

    HouseUser getHouseUser(Long houseId);

    House queryOneHouse(Long id);

    void addUserMsg(UserMsg userMsg);

    void updateRating(Long id, Double rating);

    void unbindUser2House(Long id, Long userId, HouseUserType type);

}
