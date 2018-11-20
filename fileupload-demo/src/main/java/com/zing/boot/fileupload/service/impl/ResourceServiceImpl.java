package com.zing.boot.fileupload.service.impl;

import com.zing.boot.fileupload.repository.ResourceFileRepository;
import com.zing.boot.fileupload.repository.ResourceRepository;
import com.zing.boot.fileupload.entity.Resource;
import com.zing.boot.fileupload.entity.ResourceFile;
import com.zing.boot.fileupload.service.IResourceService;
import com.zing.boot.fileupload.service.impl.BaseServiceImpl;
import com.zing.boot.fileupload.vo.FileSaveInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional(rollbackFor = Exception.class)
@Service
public class ResourceServiceImpl extends BaseServiceImpl implements IResourceService {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private ResourceFileRepository resourceFileRepository;

    @PersistenceContext
    private EntityManager em;

    /**
     * 保存资源及相关联的操作
     **/
    @Override
    public void saveUpload(Resource resource, FileSaveInfo info) {
        //保存上传文件实体
        ResourceFile resourceFile = new ResourceFile(info.getName(), info.getSaveName(), info.getType(), info.getFix(), info.getPath(), info.getRelativePath(), info.getContentType(), info.getSize(), info.getMd5());
        resourceFile.setType("resource");
        ResourceFile saveFile = resourceFileRepository.save(resourceFile);
        //保存资源实体
        resource.setFileId(saveFile.getId());
        Resource saveResource = resourceRepository.save(resource);
    }

    /**
     * 上传文件已存在，不需要保存文件的情况创建资源
     **/
    @Override
    public void saveUpload(Resource resource) {
        Assert.notNull(resource.getFileId(), "文件关系必须存在");

        resourceRepository.save(resource);
    }


}
