package com.zing.boot.house.service.search;

public class HouseBucketDTO {

    /**
     * 聚合bucket的key
     */
    private String key;

    /**
     * 聚合结果值
     */
    private long count;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
