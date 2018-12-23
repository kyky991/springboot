package com.zing.mvc.video.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zing.mvc.video.enums.BGMOperatorTypeEnum;
import com.zing.mvc.video.mapper.BgmMapper;
import com.zing.mvc.video.mapper.UserReportMapperCustom;
import com.zing.mvc.video.mapper.VideoMapper;
import com.zing.mvc.video.pojo.Bgm;
import com.zing.mvc.video.pojo.BgmExample;
import com.zing.mvc.video.pojo.Video;
import com.zing.mvc.video.pojo.vo.Report;
import com.zing.mvc.video.service.IVideoService;
import com.zing.mvc.video.utils.JsonUtils;
import com.zing.mvc.video.utils.PagedResult;
import com.zing.mvc.video.web.util.ZKCurator;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoServiceImpl implements IVideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private BgmMapper bgmMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private ZKCurator zkCurator;

    @Autowired
    private UserReportMapperCustom userReportMapperCustom;

    @Override
    public void addBgm(Bgm bgm) {
        String bgmId = sid.nextShort();
        bgm.setId(bgmId);
        bgmMapper.insert(bgm);

        Map<String, String> map = new HashMap<>();
        map.put("operType", BGMOperatorTypeEnum.ADD.type);
        map.put("path", bgm.getPath());

        zkCurator.sendBgmOperator(bgmId, JsonUtils.objectToJson(map));
    }

    @Override
    public PagedResult queryBgmList(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        BgmExample example = new BgmExample();
        List<Bgm> list = bgmMapper.selectByExample(example);

        PageInfo<Bgm> pageList = new PageInfo<>(list);

        PagedResult result = new PagedResult();
        result.setTotal(pageList.getPages());
        result.setRows(list);
        result.setPage(page);
        result.setRecords(pageList.getTotal());

        return result;
    }

    @Override
    public void deleteBgm(String id) {
        Bgm bgm = bgmMapper.selectByPrimaryKey(id);

        bgmMapper.deleteByPrimaryKey(id);

        Map<String, String> map = new HashMap<>();
        map.put("operType", BGMOperatorTypeEnum.DELETE.type);
        map.put("path", bgm.getPath());

        zkCurator.sendBgmOperator(id, JsonUtils.objectToJson(map));
    }

    @Override
    public PagedResult queryReportList(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);

        List<Report> reportList = userReportMapperCustom.selectAllVideoReport();

        PageInfo<Report> pageList = new PageInfo<Report>(reportList);

        PagedResult grid = new PagedResult();
        grid.setTotal(pageList.getPages());
        grid.setRows(reportList);
        grid.setPage(page);
        grid.setRecords(pageList.getTotal());

        return grid;
    }

    @Override
    public void updateVideoStatus(String videoId, Integer status) {
        Video video = new Video();
        video.setId(videoId);
        video.setStatus(status);
        videoMapper.updateByPrimaryKeySelective(video);
    }
}
