package com.hiveelpay.boot.service.channel.hiveel;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: wilson
 * @date: 17/8/21
 * @description: email:
 */
@Component
@ConfigurationProperties(prefix = "config.auth")
public class AuthConfig {

   private String apiUserInfo;

    public String getApiUserInfo() {
        return apiUserInfo;
    }

    public void setApiUserInfo(String apiUserInfo) {
        this.apiUserInfo = apiUserInfo;
    }

    @Override
    public String toString() {
        return "AuthConfig{" +
                "apiUserInfo='" + apiUserInfo + '\'' +
                '}';
    }
}

