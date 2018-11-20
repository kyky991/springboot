package com.zing.boot.fileupload.entity;

import com.zing.boot.fileupload.entity.base.FileEntity;

import javax.persistence.Entity;


@Entity
public class ResourceFile extends FileEntity {
    private String md5Value;

    public ResourceFile() {

    }

    public ResourceFile(String name, String saveName, String type, String fix, String path, String relativePath, String contentType, Long size, String md5Value) {
        setName(name);
        setSaveName(saveName);
        setType(type);
        setFix(fix);
        setPath(path);
        setContentType(contentType);
        setSize(size);
        setRelativePath(relativePath);
        this.md5Value = md5Value;
    }

    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }
}
