package com.zing.mvc.video.service;

import com.zing.mvc.video.pojo.Bgm;
import com.zing.mvc.video.utils.PagedResult;

public interface IVideoService {

    void addBgm(Bgm bgm);

    PagedResult queryBgmList(Integer page, Integer pageSize);

    void deleteBgm(String id);

    PagedResult queryReportList(Integer page, Integer pageSize);

    void updateVideoStatus(String videoId, Integer status);

}
