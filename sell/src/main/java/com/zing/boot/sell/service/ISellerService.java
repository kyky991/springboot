package com.zing.boot.sell.service;

import com.zing.boot.sell.pojo.SellerInfo;

public interface ISellerService {

    SellerInfo findSellerInfoByOpenid(String openid);
}
