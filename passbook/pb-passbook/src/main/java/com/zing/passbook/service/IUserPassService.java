package com.zing.passbook.service;

import com.zing.passbook.vo.Pass;
import com.zing.passbook.vo.PassInfo;
import com.zing.passbook.vo.Response;

import java.util.List;

/**
 * 获取用户个人优惠券信息
 *
 * @author Zing
 * @date 2019-11-27
 */
public interface IUserPassService {

    /**
     * <h2>获取用户个人优惠券信息, 即我的优惠券功能实现</h2>
     *
     * @param userId 用户 id
     * @return {@link Response}
     */
    Response<List<PassInfo>> getUserPassInfo(Long userId) throws Exception;

    /**
     * <h2>获取用户已经消费了的优惠券, 即已使用优惠券功能实现</h2>
     *
     * @param userId 用户 id
     * @return {@link Response}
     */
    Response<List<PassInfo>> getUserUsedPassInfo(Long userId) throws Exception;

    /**
     * <h2>获取用户所有的优惠券</h2>
     *
     * @param userId 用户 id
     * @return {@link Response}
     */
    Response<List<PassInfo>> getUserAllPassInfo(Long userId) throws Exception;

    /**
     * <h2>用户使用优惠券</h2>
     *
     * @param pass {@link Pass}
     * @return {@link Response}
     */
    Response userUsePass(Pass pass);

}
