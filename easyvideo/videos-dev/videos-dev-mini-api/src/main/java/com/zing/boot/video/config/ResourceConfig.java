package com.zing.boot.video.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "com.zing.boot.video")
@PropertySource("classpath:resource.properties")
public class ResourceConfig {

    private String zookeeperServer;
    private String bgmServer;
    private String fileSpace;

    private String code2SessionUrl;
    private String appId;
    private String secret;

    public String getZookeeperServer() {
        return zookeeperServer;
    }

    public void setZookeeperServer(String zookeeperServer) {
        this.zookeeperServer = zookeeperServer;
    }

    public String getBgmServer() {
        return bgmServer;
    }

    public void setBgmServer(String bgmServer) {
        this.bgmServer = bgmServer;
    }

    public String getFileSpace() {
        return fileSpace;
    }

    public void setFileSpace(String fileSpace) {
        this.fileSpace = fileSpace;
    }

    public String getCode2SessionUrl() {
        return code2SessionUrl;
    }

    public void setCode2SessionUrl(String code2SessionUrl) {
        this.code2SessionUrl = code2SessionUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
