package com.zing.boot.fileupload.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class ResponsePageList {
    private List<Object> list;
    private ResponsePage pages;
    private String totalCount;

    public ResponsePageList() {
    }

    public ResponsePageList(List<Object> list, ResponsePage pages, String totalCount) {
        this.list = list;
        this.pages = pages;
        this.totalCount = totalCount;
    }

    public ResponsePageList(List<Object> list, ResponsePage pages) {
        this.list = list;
        this.pages = pages;
        this.totalCount = this.pages.getCount() + "";
    }

    public ResponsePageList(Page page) {
        this.list = page.getContent();
        ResponsePage res = new ResponsePage();
        res.setPerPage(page.getSize());
        res.setPageCount(page.getTotalPages());
        res.setCurrentPage(page.getNumber() + 1);
        res.setCount(page.getTotalElements());
        this.pages = res;
        this.totalCount = this.pages.getCount() + "";
    }


    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public ResponsePage getPages() {
        return pages;
    }

    public void setPages(ResponsePage pages) {
        this.pages = pages;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }
}
