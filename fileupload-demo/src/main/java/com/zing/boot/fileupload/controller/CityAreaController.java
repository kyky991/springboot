package com.zing.boot.fileupload.controller;

import com.zing.boot.fileupload.dto.ResponseEx;
import com.zing.boot.fileupload.entity.CityArea;
import com.zing.boot.fileupload.repository.CityAreaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "城市地区接口服务", description = "城市地区相关api接口文档")
@RestController
@RequestMapping("/city")
public class CityAreaController {
    @Autowired
    private CityAreaRepository cityAreaRepository;

    @ApiOperation(value = "根据上级代码获取下级城市地区", notes = "根据上级代码获取下级城市地区，code为0获取省级菜单", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "鉴权token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "上级代码", required = false, dataType = "String", paramType = "query")
    })
    @GetMapping("/findByPrevCode")
    public ResponseEx findByPrevCode(String code) {
        List<CityArea> cityAreas = cityAreaRepository.findByPrevCodeOrderByCode(code);
        return new ResponseEx().data(cityAreas);
    }

    @ApiOperation(value = "根据城市地区id或者代码获取城市地区信息", notes = "根据城市地区id或者代码获取城市地区信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "鉴权token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "城市地区id", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "城市地区code", required = false, dataType = "String", paramType = "query")
    })
    @GetMapping("/getByAttr")
    public ResponseEx getByAttr(Long id, String code) {
        Assert.isTrue(id != null || code != null, "城市地区id或者代码必须有一个不为空");
        CityArea cityArea = null;
        if (id != null) {
            cityArea = cityAreaRepository.getById(id);
        } else if (cityArea == null && code != null) {
            cityArea = cityAreaRepository.getByCode(code);
        }
        return new ResponseEx().data(cityArea);
    }
}
