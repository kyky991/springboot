package com.zing.boot.blog.dao;

import com.zing.boot.blog.pojo.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorityDao extends JpaRepository<Authority, Long> {
}
