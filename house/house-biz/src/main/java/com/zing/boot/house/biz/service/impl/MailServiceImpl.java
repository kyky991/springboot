package com.zing.boot.house.biz.service.impl;

import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.zing.boot.house.biz.mapper.UserMapper;
import com.zing.boot.house.biz.service.IMailService;
import com.zing.boot.house.common.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MailServiceImpl implements IMailService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String FROM;


    @Value("${domain.name}")
    private String DOMAIN_NAME;

    private final Cache<String, String> registerCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> removalNotification) {
                    String email = removalNotification.getValue();
                    User user = new User();
                    user.setEmail(email);
                    List<User> users = userMapper.selectUsersByQuery(user);
                    if (!users.isEmpty() && Objects.equal(users.get(0).getEnable(), 0)) {
                        userMapper.delete(email);
                    }
                }
            }).build();

    private final Cache<String, String> resetCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(15, TimeUnit.MINUTES).build();

    @Async
    @Override
    public void sendMail(String title, String url, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setSubject(title);
        message.setTo(email);
        message.setText(url);
        mailSender.send(message);
    }

    /**
     * 1.缓存key-email的关系 2.借助spring mail 发送邮件 3.借助异步框架进行异步操作
     *
     * @param email
     */
    @Async
    @Override
    public void registerNotify(String email) {
        String randomKey = RandomStringUtils.randomAlphabetic(10);
        registerCache.put(randomKey, email);
        String url = "http://" + DOMAIN_NAME + "/accounts/verify?key=" + randomKey;
        sendMail("激活邮件", url, email);
    }

    /**
     * 发送重置密码邮件
     *
     * @param email
     */
    @Async
    @Override
    public void resetNotify(String email) {
        String randomKey = RandomStringUtils.randomAlphabetic(10);
        registerCache.put(randomKey, email);
        String url = "http://" + DOMAIN_NAME + "/accounts/reset?key=" + randomKey;
        sendMail("密码重置邮件", url, email);
    }

    @Override
    public String getResetEmail(String key) {
        return resetCache.getIfPresent(key);
    }

    @Override
    public void invalidateRestKey(String key) {
        resetCache.invalidate(key);
    }

    @Override
    public boolean enable(String key) {
        String email = registerCache.getIfPresent(key);
        if (StringUtils.isBlank(email)) {
            return false;
        }
        User user = new User();
        user.setEmail(email);
        user.setEnable(1);
        userMapper.update(user);
        registerCache.invalidate(key);
        return true;
    }
}
