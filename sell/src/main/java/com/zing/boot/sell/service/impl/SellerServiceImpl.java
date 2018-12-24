package com.zing.boot.sell.service.impl;

import com.zing.boot.sell.pojo.SellerInfo;
import com.zing.boot.sell.repository.ISellerInfoRepository;
import com.zing.boot.sell.service.ISellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements ISellerService {

    @Autowired
    private ISellerInfoRepository sellerInfoRepository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return sellerInfoRepository.findByOpenid(openid);
    }
}
