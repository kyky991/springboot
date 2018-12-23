package com.zing.boot.video.service;

import com.zing.boot.video.pojo.Comment;
import com.zing.boot.video.pojo.Video;
import com.zing.boot.video.utils.PagedResult;

import java.util.List;

public interface IVideoService {

    /**
     * 保存视频
     *
     * @param video
     * @return
     */
    String saveVideo(Video video);

    /**
     * 修改视频的封面
     *
     * @param videoId
     * @param coverPath
     */
    void updateVideo(String videoId, String coverPath);

    /**
     * 分页查询视频列表
     *
     * @param video
     * @param isSaveRecord
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult getAllVideos(Video video, Integer isSaveRecord, Integer page, Integer pageSize);

    /**
     * 查询我喜欢的视频列表
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize);

    /**
     * 查询我关注的人的视频列表
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize);

    /**
     * 获取热搜词列表
     *
     * @return
     */
    List<String> getHotWords();

    /**
     * 用户喜欢/点赞视频
     *
     * @param userId
     * @param videoId
     * @param videoCreatorId
     */
    void userLikeVideo(String userId, String videoId, String videoCreatorId);

    /**
     * 用户不喜欢/取消点赞视频
     *
     * @param userId
     * @param videoId
     * @param videoCreatorId
     */
    void userDislikeVideo(String userId, String videoId, String videoCreatorId);

    /**
     * 用户留言
     *
     * @param comment
     */
    void saveComment(Comment comment);

    /**
     * 留言分页
     *
     * @param videoId
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult getAllComments(String videoId, Integer page, Integer pageSize);
}
