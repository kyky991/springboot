package com.zing.boot.blog.service.impl;

import com.zing.boot.blog.dao.IAuthorityDao;
import com.zing.boot.blog.pojo.Authority;
import com.zing.boot.blog.service.IAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements IAuthorityService {

    @Autowired
    private IAuthorityDao authorityDao;

    @Override
    public Authority getAuthorityById(Long id) {
        return authorityDao.findOne(id);
    }
}
