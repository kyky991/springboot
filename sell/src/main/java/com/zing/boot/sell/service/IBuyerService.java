package com.zing.boot.sell.service;

import com.zing.boot.sell.dto.OrderDTO;

public interface IBuyerService {

    //查询一个订单
    OrderDTO findOrderOne(String openid, String orderId);

    //取消订单
    OrderDTO cancelOrder(String openid, String orderId);
}
