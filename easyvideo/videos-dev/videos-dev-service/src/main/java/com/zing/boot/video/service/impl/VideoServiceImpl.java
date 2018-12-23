package com.zing.boot.video.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zing.boot.video.mapper.*;
import com.zing.boot.video.pojo.Comment;
import com.zing.boot.video.pojo.SearchRecord;
import com.zing.boot.video.pojo.UserLikeVideo;
import com.zing.boot.video.pojo.Video;
import com.zing.boot.video.pojo.vo.CommentVO;
import com.zing.boot.video.pojo.vo.VideoVO;
import com.zing.boot.video.service.IVideoService;
import com.zing.boot.video.utils.PagedResult;
import com.zing.boot.video.utils.TimeAgoUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements IVideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VideoMapperCustom videoMapperCustom;

    @Autowired
    private SearchRecordMapper searchRecordMapper;

    @Autowired
    private UserLikeVideoMapper userLikeVideoMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentMapperCustom commentMapperCustom;

    @Autowired
    private Sid sid;

    @Override
    public String saveVideo(Video video) {
        String id = sid.nextShort();
        video.setId(id);
        videoMapper.insertSelective(video);

        return id;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateVideo(String videoId, String coverPath) {
        Video video = new Video();
        video.setId(videoId);
        video.setCoverPath(coverPath);
        videoMapper.updateByPrimaryKeySelective(video);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideos(Video video, Integer isSaveRecord, Integer page, Integer pageSize) {
        // 保存热搜词
        String desc = video.getVideoDesc();
        String userId = video.getUserId();
        if (isSaveRecord != null && isSaveRecord == 1) {
            SearchRecord record = new SearchRecord();
            String recordId = sid.nextShort();
            record.setId(recordId);
            record.setContent(desc);
            searchRecordMapper.insert(record);
        }

        PageHelper.startPage(page, pageSize);
        List<VideoVO> list = videoMapperCustom.queryAllVideos(desc, userId);

        PageInfo<VideoVO> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<VideoVO> list = videoMapperCustom.queryMyLikeVideos(userId);

        PageInfo<VideoVO> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<VideoVO> list = videoMapperCustom.queryMyFollowVideos(userId);

        PageInfo<VideoVO> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getHotWords() {
        return searchRecordMapper.getHotWords();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userLikeVideo(String userId, String videoId, String videoCreatorId) {
        // 1. 保存用户和视频的喜欢点赞关联关系表
        String likeId = sid.nextShort();
        UserLikeVideo ulv = new UserLikeVideo();
        ulv.setId(likeId);
        ulv.setUserId(userId);
        ulv.setVideoId(videoId);
        userLikeVideoMapper.insert(ulv);

        // 2. 视频喜欢数量累加
        videoMapperCustom.addVideoLikeCount(videoId);

        // 3. 用户受喜欢数量的累加
        userMapper.addReceiveLikeCount(videoCreatorId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userDislikeVideo(String userId, String videoId, String videoCreatorId) {
        // 1. 删除用户和视频的喜欢点赞关联关系表
        Example example = new Example(UserLikeVideo.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("videoId", videoId);

        userLikeVideoMapper.deleteByExample(example);

        // 2. 视频喜欢数量累减
        videoMapperCustom.reduceVideoLikeCount(videoId);

        // 3. 用户受喜欢数量的累减
        userMapper.reduceReceiveLikeCount(videoCreatorId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComment(Comment comment) {
        String id = sid.nextShort();
        comment.setId(id);
        comment.setCreateTime(new Date());
        commentMapper.insert(comment);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult getAllComments(String videoId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<CommentVO> list = commentMapperCustom.queryComments(videoId);

        for (CommentVO c : list) {
            String timeAgo = TimeAgoUtils.format(c.getCreateTime());
            c.setTimeAgoStr(timeAgo);
        }

        PageInfo<CommentVO> pageList = new PageInfo<>(list);

        PagedResult grid = new PagedResult();
        grid.setTotal(pageList.getPages());
        grid.setRows(list);
        grid.setPage(page);
        grid.setRecords(pageList.getTotal());

        return grid;
    }
}
