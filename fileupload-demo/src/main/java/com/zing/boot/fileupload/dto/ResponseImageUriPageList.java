package com.zing.boot.fileupload.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class ResponseImageUriPageList extends ResponsePageList {
    private String imageUri;

    public ResponseImageUriPageList() {

    }

    public ResponseImageUriPageList(List<Object> list, ResponsePage pages, String totalCount) {
        setList(list);
        setPages(pages);
        setTotalCount(totalCount);
    }

    public ResponseImageUriPageList(List<Object> list, ResponsePage pages) {
        setList(list);
        setPages(pages);
        setTotalCount(getPages().getCount() + "");
    }

    public ResponseImageUriPageList(Page page, String imageUri) {
        setList(page.getContent());
        ResponsePage res = new ResponsePage();
        res.setPerPage(page.getSize());
        res.setPageCount(page.getTotalPages());
        res.setCurrentPage(page.getNumber() + 1);
        res.setCount(page.getTotalElements());
        setPages(res);
        setTotalCount(getPages().getCount() + "");
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
