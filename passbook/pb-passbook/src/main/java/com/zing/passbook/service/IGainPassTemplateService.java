package com.zing.passbook.service;

import com.zing.passbook.vo.GainPassTemplateRequest;
import com.zing.passbook.vo.Response;

/**
 * 用户领取优惠券功能实现
 *
 * @author Zing
 * @date 2019-11-27
 */
public interface IGainPassTemplateService {

    /**
     * <h2>用户领取优惠券</h2>
     *
     * @param request {@link GainPassTemplateRequest}
     * @return {@link Response}
     */
    Response gainPassTemplate(GainPassTemplateRequest request) throws Exception;

}
