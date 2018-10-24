package com.zing.boot.blog.service.impl;

import javax.transaction.Transactional;

import com.zing.boot.blog.dao.ICommentDao;
import com.zing.boot.blog.pojo.Comment;
import com.zing.boot.blog.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Comment 服务.
 */
@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private ICommentDao commentDao;

    @Override
    @Transactional
    public void removeComment(Long id) {
        commentDao.delete(id);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentDao.findOne(id);
    }

}
