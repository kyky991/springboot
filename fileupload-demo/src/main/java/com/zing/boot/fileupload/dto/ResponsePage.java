package com.zing.boot.fileupload.dto;

public class ResponsePage {
    private int currentPage;
    private int pageCount;
    private int perPage;
    private long count;

    public ResponsePage() {
    }

    public ResponsePage(int currentPage, int pageCount, int perPage, int count) {
        this.currentPage = currentPage;
        this.pageCount = pageCount;
        this.perPage = perPage;
        this.count = count;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
