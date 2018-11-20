package com.zing.boot.fileupload.controller;

import com.zing.boot.fileupload.repository.ResourceFileRepository;
import com.zing.boot.fileupload.common.CodeUtils;
import com.zing.boot.fileupload.common.FileUtil;
import com.zing.boot.fileupload.dto.ResponseEx;
import com.zing.boot.fileupload.entity.Resource;
import com.zing.boot.fileupload.entity.ResourceFile;
import com.zing.boot.fileupload.service.IResourceService;
import com.zing.boot.fileupload.vo.FileSaveInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Api(tags = "资源", description = "资源相关api文档接口列表")
@RestController
@RequestMapping("/resource")
public class ResourceController extends BaseUploadController {

    @Autowired
    private IResourceService resourceService;

    @Autowired
    private ResourceFileRepository resourceFileRepository;

    @ApiOperation(value = "资源上传", notes = "资源上传接口，swagger不能模拟分片功能，需百度的webuploader或者plupload等支持分片功能的上传组件支持", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "鉴权token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "资源标题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "resourceType", value = "资源类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "brief", value = "资源备注说明", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "guid", value = "临时文件名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "md5value", value = "客户端生成md5值", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "chunks", value = "总分片数", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "chunk", value = "当前分片序号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "上传文件名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "iconMd5value", value = "头像文件的md5值（需先调用上传封面接口，再调用此接口）", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping("/upload")
    public ResponseEx upload(
            String title,
            String resourceType,
            String brief,
            String guid,
            String md5value,
            String chunks,
            String chunk,
            String name,
            @RequestParam(value = "file", required = false) MultipartFile file,
            String iconMd5value) {
        Assert.notNull(title, "资源文件标题不能为空");
        Assert.notNull(resourceType, "资源文件类型不能为空");
        String uploadFolderPath = FileUtil.getResourcePath();
        Resource resource = new Resource();
        resource.setBrief(brief);
        resource.setName(title);
        resource.setType(resourceType);
        if (file == null) {
            Assert.notNull(md5value, "MD5值不能为空");
            ResourceFile resourceFile = resourceFileRepository.findFirstByMd5Value(md5value);
            Assert.notNull(resourceFile, "资源文件不存在！");
            resource.setFileId(resourceFile.getId());
            ResourceFile iconFile = resourceFileRepository.findFirstByMd5Value(iconMd5value);
            if (iconFile != null) {
                resource.setIconId(iconFile.getId());
            }
            resourceService.saveUpload(resource);
        } else {
            FileSaveInfo info = fileUpload(guid, md5value, chunks, chunk, name, file, uploadFolderPath);
            ResourceFile iconFile = resourceFileRepository.findFirstByMd5Value(iconMd5value);
            if (iconFile != null) {
                resource.setIconId(iconFile.getId());
            }
            if (info != null) {
                info.setType("resource");
                resourceService.saveUpload(resource, info);
            }
        }
        return new ResponseEx();
    }

    @ApiOperation(value = "根据md5值判断资源文件是否存在", notes = "根据md5值判断资源文件是否存在", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "鉴权token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "md5value", value = "文件md5值", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/findResourceFileExistsByMd5")
    public ResponseEx findResourceFileExists(String md5value) {
        ResourceFile resourceFile = resourceFileRepository.findFirstByMd5Value(md5value);
        return new ResponseEx().data(Collections.singletonMap("exist", resourceFile != null));
    }

    @ApiOperation(value = "资源封面上传", notes = "资源封面上传", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "鉴权token", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "iconMd5value", value = "头像文件的md5值（需先调用上传封面接口，再调用此接口）", required = false, dataType = "String", paramType = "query")
    })
    @PostMapping("/uploadResourceIcon")
    public ResponseEx uploadResourceIcon(@RequestParam(value = "file", required = true) MultipartFile file, String iconMd5value) {
        String fileName = CodeUtils.random(file.getOriginalFilename());
        int temp = fileName.lastIndexOf(".");
        if (temp != -1) {
            fileName = fileName.substring(0, temp);
        }
        ResourceFile resourceFile = resourceFileRepository.findFirstByMd5Value(iconMd5value);
        if (resourceFile != null) {
            return new ResponseEx().data(Collections.singletonMap("id", resourceFile.getId()));
        } else {
            FileSaveInfo info = fileUpload(fileName, iconMd5value, null, null, CodeUtils.random(file.getOriginalFilename()), file, FileUtil.getResourcePath());
            resourceFile = new ResourceFile(info.getName(), info.getSaveName(), info.getType(), info.getFix(), info.getPath(), info.getRelativePath(), info.getContentType(), info.getSize(), info.getMd5());
            resourceFile.setType("icon");
            ResourceFile saveFile = resourceFileRepository.save(resourceFile);
            return new ResponseEx().data(Collections.singletonMap("id", saveFile.getId()));
        }
    }


    @GetMapping("/image")
    public void image(String id, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(id)) {
            responseIcon(request, response, null, "image/png", ResourceController.class.getResourceAsStream("/static/fm.png"));
        } else {
            ResourceFile resourceFile = resourceFileRepository.getById(Long.parseLong(id));
            if (resourceFile == null) {
                responseIcon(request, response, null, "image/png", ResourceController.class.getResourceAsStream("/static/fm.png"));
            } else {
                String path = resourceFile.getPath();
                String contentType = resourceFile.getContentType();
                responseIcon(request, response, path, contentType, ResourceController.class.getResourceAsStream("/static/fm.png"));
            }
        }
    }

    public String getImageUri() {
        return "/resource/image?id=";
    }


    public String getStreamUri() {
        return "/resource/stream?fileId=";
    }

    public String getDownloadUri() {
        return "/resource/download?id=";
    }


    @RequestMapping("/stream")
    public void bigFileDownload(HttpServletRequest request, HttpServletResponse response, Long fileId) throws IOException {
        ResourceFile file = resourceFileRepository.getById(fileId);
        Assert.notNull(file, "文件不存在");
        responseInputstream(request, response, file.getPath(), file.getContentType());
    }

    @GetMapping("/download")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, Long id) {
        ResourceFile file = resourceFileRepository.getById(id);
        Assert.notNull(file, "文件不存在");
        download(request, response, file.getPath(), file.getSaveName());
    }


}
