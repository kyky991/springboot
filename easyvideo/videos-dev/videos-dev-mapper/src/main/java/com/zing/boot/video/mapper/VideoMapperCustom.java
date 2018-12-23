package com.zing.boot.video.mapper;

import com.zing.boot.video.pojo.Video;
import com.zing.boot.video.pojo.vo.VideoVO;
import com.zing.boot.video.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoMapperCustom extends MyMapper<Video> {

    /**
     * 条件查询所有视频列表
     *
     * @param videoDesc
     * @param userId
     * @return
     */
    List<VideoVO> queryAllVideos(@Param("videoDesc") String videoDesc, @Param("userId") String userId);

    /**
     * 查询关注的视频
     *
     * @param userId
     * @return
     */
    List<VideoVO> queryMyFollowVideos(String userId);

    /**
     * 查询点赞视频
     *
     * @param userId
     * @return
     */
    List<VideoVO> queryMyLikeVideos(@Param("userId") String userId);

    /**
     * 对视频喜欢的数量进行累加
     *
     * @param videoId
     */
    void addVideoLikeCount(String videoId);

    /**
     * 对视频喜欢的数量进行累减
     *
     * @param videoId
     */
    void reduceVideoLikeCount(String videoId);
}
