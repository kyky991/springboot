package com.zing.boot.blog.dao;

import com.zing.boot.blog.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Comment 仓库.
 */
public interface ICommentDao extends JpaRepository<Comment, Long>{
 
}
