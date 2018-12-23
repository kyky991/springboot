package com.zing.boot.house.web.controller;

import com.zing.boot.house.biz.service.*;
import com.zing.boot.house.common.constants.CommonConstants;
import com.zing.boot.house.common.constants.HouseUserType;
import com.zing.boot.house.common.model.*;
import com.zing.boot.house.common.page.PageData;
import com.zing.boot.house.common.page.PageParams;
import com.zing.boot.house.common.result.ResultMsg;
import com.zing.boot.house.web.interceptor.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/house")
public class HouseController {

    @Autowired
    private IHouseService houseService;

    @Autowired
    private IRecommendService recommendService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IAgencyService agencyService;

    /**
     * 1.实现分页
     * 2.支持小区搜索、类型搜索
     * 3.支持排序
     * 4.支持展示图片、价格、标题、地址等信息
     *
     * @return
     */
    @RequestMapping("/list")
    public String houseList(Integer pageSize, Integer pageNum, House query, ModelMap modelMap) {
        PageData<House> pageData = houseService.queryHouse(query, PageParams.build(pageSize, pageNum));
        List<House> hotHouses = recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", hotHouses);
        modelMap.put("ps", pageData);
        modelMap.put("vo", query);
        return "house/listing";
    }

    @RequestMapping("/toAdd")
    public String toAdd(ModelMap modelMap) {
        modelMap.put("citys", cityService.getAllCities());
        modelMap.put("communitys", houseService.getAllCommunities());
        return "/house/add";
    }

    @RequestMapping("/add")
    public String doAdd(House house) {
        User user = UserContext.getUser();
        house.setState(CommonConstants.HOUSE_STATE_UP);
        houseService.addHouse(house, user);
        return "redirect:/house/ownlist";
    }

    @RequestMapping("/ownlist")
    public String ownlist(House house, Integer pageNum, Integer pageSize, ModelMap modelMap) {
        User user = UserContext.getUser();
        house.setUserId(user.getId());
        house.setBookmarked(false);
        modelMap.put("ps", houseService.queryHouse(house, PageParams.build(pageSize, pageNum)));
        modelMap.put("pageType", "own");
        return "/house/ownlist";
    }

    /**
     * 查询房屋详情
     * 查询关联经纪人
     *
     * @param id
     * @return
     */
    @RequestMapping("/detail")
    public String houseDetail(Long id, ModelMap modelMap) {
        House house = houseService.queryOneHouse(id);
        HouseUser houseUser = houseService.getHouseUser(id);
        recommendService.increase(id);
        List<Comment> comments = commentService.getHouseComments(id, 8);
        if (houseUser.getUserId() != null && !houseUser.getUserId().equals(0)) {
            modelMap.put("agent", agencyService.getAgentDetail(houseUser.getUserId()));
        }
        List<House> rcHouses = recommendService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", rcHouses);
        modelMap.put("house", house);
        modelMap.put("commentList", comments);
        return "/house/detail";
    }

    @RequestMapping("/leaveMsg")
    public String houseMsg(UserMsg userMsg) {
        houseService.addUserMsg(userMsg);
        return "redirect:/house/detail?id=" + userMsg.getHouseId() + ResultMsg.successMsg("留言成功").asUrlParams();
    }

    //1.评分
    @ResponseBody
    @RequestMapping("/rating")
    public ResultMsg houseRate(Double rating, Long id) {
        houseService.updateRating(id, rating);
        return ResultMsg.successMsg("ok");
    }


    //2.收藏
    @ResponseBody
    @RequestMapping("/bookmark")
    public ResultMsg bookmark(Long id) {
        User user = UserContext.getUser();
        houseService.bindUser2House(id, user.getId(), true);
        return ResultMsg.successMsg("ok");
    }

    //3.删除收藏
    @ResponseBody
    @RequestMapping("/unbookmark")
    public ResultMsg unbookmark(Long id) {
        User user = UserContext.getUser();
        houseService.unbindUser2House(id, user.getId(), HouseUserType.BOOKMARK);
        return ResultMsg.successMsg("ok");
    }

    @RequestMapping("/del")
    public String delsale(Long id, String pageType) {
        User user = UserContext.getUser();
        houseService.unbindUser2House(id, user.getId(), pageType.equals("own") ? HouseUserType.SALE : HouseUserType.BOOKMARK);
        return "redirect:/house/ownlist";
    }

    //4.收藏列表
    @RequestMapping("/bookmarked")
    public String bookmarked(House house, Integer pageNum, Integer pageSize, ModelMap modelMap) {
        User user = UserContext.getUser();
        house.setBookmarked(true);
        house.setUserId(user.getId());
        modelMap.put("ps", houseService.queryHouse(house, PageParams.build(pageSize, pageNum)));
        modelMap.put("pageType", "book");
        return "/house/ownlist";
    }
}
