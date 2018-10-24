package com.zing.boot.blog.service.impl;

import javax.transaction.Transactional;

import com.zing.boot.blog.pojo.Vote;
import com.zing.boot.blog.service.IVoteService;
import com.zing.boot.blog.dao.IVoteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Vote 服务.
 */
@Service
public class VoteServiceImpl implements IVoteService {

    @Autowired
    private IVoteDao voteDao;

    @Override
    @Transactional
    public void removeVote(Long id) {
        voteDao.delete(id);
    }

    @Override
    public Vote getVoteById(Long id) {
        return voteDao.findOne(id);
    }

}
