package com.zing.boot.house.web.controller;

import com.google.common.base.Objects;
import com.zing.boot.house.biz.service.IAgencyService;
import com.zing.boot.house.biz.service.IHouseService;
import com.zing.boot.house.biz.service.IMailService;
import com.zing.boot.house.biz.service.IRecommendService;
import com.zing.boot.house.common.constants.CommonConstants;
import com.zing.boot.house.common.model.Agency;
import com.zing.boot.house.common.model.House;
import com.zing.boot.house.common.model.User;
import com.zing.boot.house.common.page.PageData;
import com.zing.boot.house.common.page.PageParams;
import com.zing.boot.house.common.result.ResultMsg;
import com.zing.boot.house.web.interceptor.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/agency")
public class AgencyController {

    @Autowired
    private IAgencyService agencyService;

    @Autowired
    private IRecommendService recommendService;

    @Autowired
    private IHouseService houseService;

    @Autowired
    private IMailService mailService;

    @RequestMapping("/create")
    public String create() {
        User user = UserContext.getUser();
        if (user == null || Objects.equal(user.getEmail(), "xxx@163.com")) {
            return "redirect:/account/signin?" + ResultMsg.successMsg("请先登录").asUrlParams();
        }
        return "/user/agency/create";
    }

    @RequestMapping("/agentList")
    public String agentList(Integer pageSize, Integer pageNum, ModelMap modelMap) {
        if (pageSize == null) {
            pageSize = 6;
        }
        PageData<User> pageData = agencyService.getAllAgent(PageParams.build(pageSize, pageNum));
        List<House> houses = recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", houses);
        modelMap.put("ps", pageData);
        return "/user/agent/agentList";
    }

    @RequestMapping("/agentDetail")
    public String agentDetail(Long id, ModelMap modelMap) {
        User user = agencyService.getAgentDetail(id);
        List<House> houses = recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        House query = new House();
        query.setUserId(id);
        query.setBookmarked(false);
        PageData<House> bindHouse = houseService.queryHouse(query, new PageParams(3, 1));
        if (bindHouse != null) {
            modelMap.put("bindHouses", bindHouse.getList());
        }
        modelMap.put("recomHouses", houses);
        modelMap.put("agent", user);
        modelMap.put("agencyName", user.getAgencyName());
        return "/user/agent/agentDetail";
    }

    @RequestMapping("/agentMsg")
    public String agentMsg(Long id, String msg, String name, String email, ModelMap modelMap) {
        User user = agencyService.getAgentDetail(id);
        mailService.sendMail("咨询", "email:" + email + ",msg:" + msg, user.getEmail());
        return "redirect:/agency/agentDetail?id=" + id + "&" + ResultMsg.successMsg("留言成功").asUrlParams();
    }

    @RequestMapping("/agencyDetail")
    public String agencyDetail(Integer id, ModelMap modelMap) {
        Agency agency = agencyService.getAgency(id);
        List<House> houses = recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", houses);
        modelMap.put("agency", agency);
        return "/user/agency/agencyDetail";
    }

    @RequestMapping("/list")
    public String agencyList(ModelMap modelMap){
        List<Agency> agencies = agencyService.getAllAgency();
        List<House> houses =  recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", houses);
        modelMap.put("agencyList", agencies);
        return "/user/agency/agencyList";
    }

    public AgencyController() {
    }

    @RequestMapping("/submit")
    public String agencySubmit(Agency agency){
        User user =  UserContext.getUser();
        if (user == null || !Objects.equal(user.getEmail(), "xxx@163.com")) {//只有超级管理员可以添加
            return "redirect:/accounts/signin?" + ResultMsg.successMsg("请先登录").asUrlParams();
        }
        agencyService.add(agency);
        return "redirect:/index?" + ResultMsg.successMsg("创建成功").asUrlParams();
    }
}
