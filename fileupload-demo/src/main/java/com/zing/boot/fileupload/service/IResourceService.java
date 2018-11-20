package com.zing.boot.fileupload.service;

import com.zing.boot.fileupload.entity.Resource;
import com.zing.boot.fileupload.vo.FileSaveInfo;

public interface IResourceService extends IBaseService {
    void saveUpload(Resource resource, FileSaveInfo info);

    void saveUpload(Resource resource);
}
