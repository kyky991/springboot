package com.zing.boot.house.web.controller;

import com.zing.boot.house.biz.service.IRecommendService;
import com.zing.boot.house.common.model.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private IRecommendService recommendService;

    @RequestMapping("/index")
    public String accountsRegister(ModelMap modelMap){
        List<House> houses =  recommendService.getLatest();
        modelMap.put("recomHouses", houses);
        return "/homepage/index";
    }

    @RequestMapping("/")
    public String index(ModelMap modelMap){
        return "redirect:/index";
    }

}
