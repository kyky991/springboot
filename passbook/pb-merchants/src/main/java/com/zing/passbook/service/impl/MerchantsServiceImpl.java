package com.zing.passbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.zing.passbook.constant.Constants;
import com.zing.passbook.constant.ResultCode;
import com.zing.passbook.entity.Merchants;
import com.zing.passbook.repository.MerchantsRepository;
import com.zing.passbook.service.IMerchantsService;
import com.zing.passbook.vo.CreateMerchantsRequest;
import com.zing.passbook.vo.CreateMerchantsResponse;
import com.zing.passbook.vo.PassTemplate;
import com.zing.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Zing
 * @date 2019-11-27
 */
@Slf4j
@Service
public class MerchantsServiceImpl implements IMerchantsService {

    @Autowired
    private MerchantsRepository merchantsRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response createMerchants(CreateMerchantsRequest request) {
        CreateMerchantsResponse merchantsResponse = new CreateMerchantsResponse();
        merchantsResponse.setId(-1L);

        ResultCode resultCode = request.validate();
        if (resultCode == ResultCode.SUCCESS) {
            Merchants name = merchantsRepository.findByName(request.getName());
            if (name != null) {
                return Response.build(ResultCode.DUPLICATE_NAME, merchantsResponse);
            }
            merchantsResponse.setId(merchantsRepository.save(request.toMerchants()).getId());
        }
        return Response.build(resultCode, merchantsResponse);
    }

    @Override
    public Response buildMerchantsInfoById(Long id) {
        Response<Merchants> response = new Response<>();

        Optional<Merchants> optional = merchantsRepository.findById(id);
        if (optional.isPresent()) {
            response.setData(optional.get());
        } else {
            response.setCode(ResultCode.MERCHANTS_NOT_EXIST.getCode());
            response.setMsg(ResultCode.MERCHANTS_NOT_EXIST.getDesc());
        }
        return response;
    }

    @Override
    public Response dropPassTemplate(PassTemplate template) {
        Optional<Merchants> optional = merchantsRepository.findById(template.getId());
        if (!optional.isPresent()) {
            return Response.build(ResultCode.MERCHANTS_NOT_EXIST);
        }

        String json = JSON.toJSONString(template);
        kafkaTemplate.send(Constants.TEMPLATE_TOPIC, Constants.TEMPLATE_TOPIC, json);
        log.info("template: {}", json);
        return Response.success();
    }
}
