package com.zing.springboot.blog.service;

import com.zing.springboot.blog.pojo.Vote;

/**
 * Vote 服务接口.
 */
public interface IVoteService {
	/**
	 * 根据id获取 Vote
	 * @param id
	 * @return
	 */
	Vote getVoteById(Long id);
	/**
	 * 删除Vote
	 * @param id
	 * @return
	 */
	void removeVote(Long id);
}
