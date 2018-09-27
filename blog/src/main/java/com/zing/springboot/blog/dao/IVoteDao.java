package com.zing.springboot.blog.dao;

import com.zing.springboot.blog.pojo.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Vote 仓库.
 */
public interface IVoteDao extends JpaRepository<Vote, Long>{
 
}
