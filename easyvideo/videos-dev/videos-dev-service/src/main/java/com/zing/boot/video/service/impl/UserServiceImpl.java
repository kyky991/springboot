package com.zing.boot.video.service.impl;

import com.zing.boot.video.mapper.UserFanMapper;
import com.zing.boot.video.mapper.UserLikeVideoMapper;
import com.zing.boot.video.mapper.UserMapper;
import com.zing.boot.video.mapper.UserReportMapper;
import com.zing.boot.video.pojo.User;
import com.zing.boot.video.pojo.UserFan;
import com.zing.boot.video.pojo.UserLikeVideo;
import com.zing.boot.video.pojo.UserReport;
import com.zing.boot.video.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper usersMapper;

    @Autowired
    private UserFanMapper userFanMapper;

    @Autowired
    private UserLikeVideoMapper userLikeVideoMapper;

    @Autowired
    private UserReportMapper userReportMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {

        User user = new User();
        user.setUsername(username);

        User result = usersMapper.selectOne(user);

        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUserForLogin(String username, String password) {
        Example userExample = new Example(User.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", password);
        User result = usersMapper.selectOneByExample(userExample);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(User user) {
        String userId = sid.nextShort();
        user.setId(userId);
        usersMapper.insert(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserInfo(User user) {
        Example userExample = new Example(User.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", user.getId());
        usersMapper.updateByExampleSelective(user, userExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUserInfo(String userId) {
        Example userExample = new Example(User.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id", userId);
        User user = usersMapper.selectOneByExample(userExample);
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserLikeVideo(String userId, String videoId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)) {
            return false;
        }

        Example example = new Example(UserLikeVideo.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("videoId", videoId);

        List<UserLikeVideo> list = userLikeVideoMapper.selectByExample(example);

        if (list != null && list.size() > 0) {
            return true;
        }

        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUserFanRelation(String userId, String fanId) {
        String reId = sid.nextShort();

        UserFan userFan = new UserFan();
        userFan.setId(reId);
        userFan.setUserId(userId);
        userFan.setFanId(fanId);

        userFanMapper.insert(userFan);

        usersMapper.addFansCount(userId);
        usersMapper.addFollowsCount(fanId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserFanRelation(String userId, String fanId) {
        Example example = new Example(UserFan.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("fanId", fanId);

        userFanMapper.deleteByExample(example);

        usersMapper.reduceFansCount(userId);
        usersMapper.reduceFollowsCount(fanId);
    }

    @Override
    public boolean queryIfFollow(String userId, String fanId) {
        Example example = new Example(UserFan.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("fanId", fanId);

        List<UserFan> list = userFanMapper.selectByExample(example);

        if (list != null && !list.isEmpty() && list.size() > 0) {
            return true;
        }

        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void reportUser(UserReport userReport) {
        String urId = sid.nextShort();
        userReport.setId(urId);
        userReport.setCreateDate(new Date());

        userReportMapper.insert(userReport);
    }
}
