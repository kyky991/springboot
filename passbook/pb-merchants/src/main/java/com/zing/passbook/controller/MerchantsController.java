package com.zing.passbook.controller;

import com.alibaba.fastjson.JSON;
import com.zing.passbook.service.IMerchantsService;
import com.zing.passbook.vo.CreateMerchantsRequest;
import com.zing.passbook.vo.PassTemplate;
import com.zing.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zing
 * @date 2019-11-27
 */
@Slf4j
@RestController
@RequestMapping("/merchants")
public class MerchantsController {

    @Autowired
    private IMerchantsService merchantsService;

    @PostMapping("/create")
    public Response createMerchants(@RequestBody CreateMerchantsRequest request) {
        log.info("CreateMerchants: {}", JSON.toJSONString(request));
        return merchantsService.createMerchants(request);
    }

    @GetMapping("/{id}")
    public Response buildMerchantsInfo(@PathVariable Integer id) {
        log.info("BuildMerchantsInfo: {}", id);
        return merchantsService.buildMerchantsInfoById(id);
    }

    @PostMapping("/drop")
    public Response dropPassTemplate(@RequestBody PassTemplate passTemplate) {
        log.info("DropPassTemplate: {}", passTemplate);
        return merchantsService.dropPassTemplate(passTemplate);
    }
}
