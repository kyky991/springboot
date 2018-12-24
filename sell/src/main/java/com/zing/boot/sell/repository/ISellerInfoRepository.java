package com.zing.boot.sell.repository;

import com.zing.boot.sell.pojo.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISellerInfoRepository extends JpaRepository<SellerInfo, String> {

    SellerInfo findByOpenid(String openid);

}
