package com.zing.boot.video.mapper;

import com.zing.boot.video.pojo.User;
import com.zing.boot.video.utils.MyMapper;

public interface UserMapper extends MyMapper<User> {

    /**
     * 用户受喜欢数累加
     *
     * @param userId
     */
    void addReceiveLikeCount(String userId);

    /**
     * 用户受喜欢数累减
     *
     * @param userId
     */
    void reduceReceiveLikeCount(String userId);

    /**
     * 增加粉丝数
     *
     * @param userId
     */
    void addFansCount(String userId);

    /**
     * 增加关注数
     *
     * @param userId
     */
    void addFollowsCount(String userId);

    /**
     * 减少粉丝数
     *
     * @param userId
     */
    void reduceFansCount(String userId);

    /**
     * 减少关注数
     *
     * @param userId
     */
    void reduceFollowsCount(String userId);

}