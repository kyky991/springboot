package com.zing.passbook.service;

import com.zing.passbook.vo.CreateMerchantsRequest;
import com.zing.passbook.vo.PassTemplate;
import com.zing.passbook.vo.Response;

/**
 * @author Zing
 * @date 2019-11-27
 */
public interface IMerchantsService {

    /**
     * <h2>创建商户服务</h2>
     *
     * @param request {@link CreateMerchantsRequest} 创建商户请求
     * @return {@link Response}
     */
    public Response createMerchants(CreateMerchantsRequest request);

    /**
     * <h2>根据 id 构造商户信息</h2>
     *
     * @param id 商户 id
     * @return {@link Response}
     */
    public Response buildMerchantsInfoById(Long id);

    /**
     * <h2>投放优惠券</h2>
     *
     * @param template {@link PassTemplate} 优惠券对象
     * @return {@link Response}
     */
    public Response dropPassTemplate(PassTemplate template);

}
