package com.zing.boot.house.biz.service;

public interface IMailService {

    void sendMail(String title, String url, String email);

    void registerNotify(String email);

    void resetNotify(String email);

    String getResetEmail(String key);

    void invalidateRestKey(String key);

    boolean enable(String key);

}
