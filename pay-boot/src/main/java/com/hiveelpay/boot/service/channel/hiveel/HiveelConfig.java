package com.hiveelpay.boot.service.channel.hiveel;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: wilson
 * @date: 17/8/21
 * @description:
 */
@Component
@ConfigurationProperties(prefix = "config.hiveel")
public class HiveelConfig {

    private String payServerBaseUrl;


    public String getPayServerBaseUrl() {
        return payServerBaseUrl;
    }

    public void setPayServerBaseUrl(String payServerBaseUrl) {
        this.payServerBaseUrl = payServerBaseUrl;
    }
}

