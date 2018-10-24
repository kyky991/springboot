package com.zing.boot.blog.service.impl;

import com.zing.boot.blog.pojo.User;
import com.zing.boot.blog.dao.IUserDao;
import com.zing.boot.blog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

    @Autowired
    private IUserDao userDao;

    @Transactional
    @Override
    public User saveUser(User user) {
        return userDao.save(user);
    }

    @Transactional
    @Override
    public void removeUser(Long id) {
        userDao.delete(id);
    }

    @Transactional
    @Override
    public void removeUsersInBatch(List<User> users) {
        userDao.deleteInBatch(users);
    }

    @Transactional
    @Override
    public User updateUser(User user) {
        return userDao.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.findOne(id);
    }

    @Override
    public List<User> listUsers() {
        return userDao.findAll();
    }

    @Override
    public Page<User> listUsersByNameLike(String name, Pageable pageable) {
        // 模糊查询
        name = "%" + name + "%";
        return userDao.findByNameLike(name, pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByUsername(username);
    }

    @Override
    public List<User> listUsersByUsernames(Collection<String> usernames) {
        return userDao.findByUsernameIn(usernames);
    }
}
