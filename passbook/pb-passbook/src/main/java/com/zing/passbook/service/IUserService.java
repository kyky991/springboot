package com.zing.passbook.service;

import com.zing.passbook.vo.Response;
import com.zing.passbook.vo.User;

/**
 * 用户服务: 创建 User 服务
 *
 * @author Zing
 * @date 2019-11-27
 */
public interface IUserService {

    /**
     * <h2>创建用户</h2>
     *
     * @param user {@link User}
     * @return {@link Response}
     */
    Response createUser(User user) throws Exception;

}
