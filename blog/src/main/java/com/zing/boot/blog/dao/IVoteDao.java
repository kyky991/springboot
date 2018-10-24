package com.zing.boot.blog.dao;

import com.zing.boot.blog.pojo.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Vote 仓库.
 */
public interface IVoteDao extends JpaRepository<Vote, Long>{
 
}
