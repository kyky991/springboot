package com.zing.boot.house.biz.service;

import com.zing.boot.house.common.model.Agency;
import com.zing.boot.house.common.model.User;
import com.zing.boot.house.common.page.PageData;
import com.zing.boot.house.common.page.PageParams;

import java.util.List;

public interface IAgencyService {

    User getAgentDetail(Long userId);

    PageData<User> getAllAgent(PageParams pageParams);

    Agency getAgency(Integer id);

    List<Agency> getAllAgency();

    int add(Agency agency);

    void sendMsg(User agent, String msg, String name, String email);
}
