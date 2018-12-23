package com.zing.boot.house.biz.service;

import com.zing.boot.house.common.model.User;

import java.util.List;

public interface IUserService {

    List<User> getUsers();

    boolean addAccount(User account);

    boolean enable(String key);

    User auth(String username, String password);

    List<User> getUserByQuery(User user);

    void updateUser(User updateUser, String email);

    User getUserById(Long id);

    void resetNotify(String username);

    User reset(String key,String password);

    User getUserByEmail(String email);

    String getResetEmail(String key);
}
