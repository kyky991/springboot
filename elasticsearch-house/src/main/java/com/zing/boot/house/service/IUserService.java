package com.zing.boot.house.service;

import com.zing.boot.house.entity.User;
import com.zing.boot.house.web.dto.UserDTO;

public interface IUserService {

    User findUserByName(String username);

    ServiceResult<UserDTO> findById(Long userId);

    /**
     * 根据电话号码寻找用户
     *
     * @param telephone
     * @return
     */
    User findUserByTelephone(String telephone);

    /**
     * 通过手机号注册用户
     *
     * @param telehone
     * @return
     */
    User addUserByPhone(String telehone);

    /**
     * 修改指定属性值
     *
     * @param profile
     * @param value
     * @return
     */
    ServiceResult modifyUserProfile(String profile, String value);
}
