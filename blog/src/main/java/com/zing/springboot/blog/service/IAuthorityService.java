package com.zing.springboot.blog.service;

import com.zing.springboot.blog.pojo.Authority;

public interface IAuthorityService {
    Authority getAuthorityById(Long id);
}
