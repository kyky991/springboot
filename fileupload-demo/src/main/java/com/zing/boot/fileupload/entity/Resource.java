package com.zing.boot.fileupload.entity;

import com.zing.boot.fileupload.entity.base.TemporalEntity;

import javax.persistence.Entity;


@Entity
public class Resource extends TemporalEntity {
    private String name;
    private String type;
    private String brief;
    private String detail;
    private Long fileId;
    private Long iconId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getIconId() {
        return iconId;
    }

    public void setIconId(Long iconId) {
        this.iconId = iconId;
    }
}
