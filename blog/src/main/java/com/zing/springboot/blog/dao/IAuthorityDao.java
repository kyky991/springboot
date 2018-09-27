package com.zing.springboot.blog.dao;

import com.zing.springboot.blog.pojo.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorityDao extends JpaRepository<Authority, Long> {
}
