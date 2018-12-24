package com.zing.boot.sell.service;

import com.zing.boot.sell.dto.OrderDTO;

/**
 * 推送消息
 */
public interface IPushMessageService {

    /**
     * 订单状态变更消息
     * @param orderDTO
     */
    void orderStatus(OrderDTO orderDTO);

}
