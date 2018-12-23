package com.zing.boot.video.service;

import com.zing.boot.video.pojo.User;
import com.zing.boot.video.pojo.UserReport;

public interface IUserService {

    /**
     * 判断用户名是否存在
     *
     * @param username
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 用户登录，根据用户名和密码查询用户
     *
     * @param username
     * @param password
     * @return
     */
    User queryUserForLogin(String username, String password);

    /**
     * 保存用户(用户注册)
     *
     * @param user
     */
    void saveUser(User user);

    /**
     * 用户修改信息
     *
     * @param user
     */
    void updateUserInfo(User user);

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    User queryUserInfo(String userId);

    /**
     * 查询用户是否喜欢点赞视频
     *
     * @param userId
     * @param videoId
     * @return
     */
    boolean isUserLikeVideo(String userId, String videoId);

    /**
     * 增加用户和粉丝的关系
     *
     * @param userId
     * @param fanId
     */
    void saveUserFanRelation(String userId, String fanId);

    /**
     * 删除用户和粉丝的关系
     *
     * @param userId
     * @param fanId
     */
    void deleteUserFanRelation(String userId, String fanId);

    /**
     * 查询用户是否关注
     *
     * @param userId
     * @param fanId
     * @return
     */
    boolean queryIfFollow(String userId, String fanId);

    /**
     * 举报用户
     *
     * @param userReport
     */
    void reportUser(UserReport userReport);
}
