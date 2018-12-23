package com.zing.boot.house.biz.service.impl;

import com.google.common.collect.Lists;
import com.zing.boot.house.biz.mapper.UserMapper;
import com.zing.boot.house.biz.service.IMailService;
import com.zing.boot.house.biz.service.IUserService;
import com.zing.boot.house.common.model.User;
import com.zing.boot.house.common.utils.BeanHelper;
import com.zing.boot.house.common.utils.FileUtils;
import com.zing.boot.house.common.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IMailService mailService;

    @Value("${file.path:}")
    private String FILE_PATH;

    @Value("${file.prefix}")
    private String IMG_PREFIX;

    @Override
    public List<User> getUsers() {
        return userMapper.selectUsers();
    }

    /**
     * 1.插入数据库，非激活;密码加盐md5;保存头像文件到本地 2.生成key，绑定email 3.发送邮件给用户
     *
     * @param account
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addAccount(User account) {
        account.setPasswd(HashUtils.encryptPassword(account.getPasswd()));
        List<String> list = FileUtils.getImgPaths(FILE_PATH, Lists.newArrayList(account.getAvatarFile()));
        if (!list.isEmpty()) {
            account.setAvatar(list.get(0));
        }
        BeanHelper.setDefaultProp(account, User.class);
        BeanHelper.onInsert(account);
        account.setEnable(0);
        userMapper.insert(account);
        mailService.registerNotify(account.getEmail());
        return true;
    }

    @Override
    public boolean enable(String key) {
        return mailService.enable(key);
    }

    @Override
    public User auth(String username, String password) {
        User user = new User();
        user.setEmail(username);
        user.setPasswd(HashUtils.encryptPassword(password));
        user.setEnable(1);
        List<User> list = getUserByQuery(user);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<User> getUserByQuery(User user) {
        List<User> list = userMapper.selectUsersByQuery(user);
        list.forEach(u -> u.setAvatar(IMG_PREFIX + u.getAvatar()));
        return list;
    }

    @Override
    public void updateUser(User updateUser, String email) {
        updateUser.setEmail(email);
        BeanHelper.onUpdate(updateUser);
        userMapper.update(updateUser);
    }

    @Override
    public User getUserById(Long id) {
        User queryUser = new User();
        queryUser.setId(id);
        List<User> users = getUserByQuery(queryUser);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public void resetNotify(String username) {
        mailService.resetNotify(username);
    }

    /**
     * 重置密码操作
     *
     * @param key
     * @param password
     * @return
     */
    @Override
    public User reset(String key, String password) {
        String email = getResetEmail(key);
        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setPasswd(HashUtils.encryptPassword(password));
        userMapper.update(updateUser);
        mailService.invalidateRestKey(key);
        return getUserByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) {
        User queryUser = new User();
        queryUser.setEmail(email);
        List<User> users = getUserByQuery(queryUser);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public String getResetEmail(String key) {
        String email = "";
        try {
            email =  mailService.getResetEmail(key);
        } catch (Exception ignore) {
        }
        return email;
    }
}
