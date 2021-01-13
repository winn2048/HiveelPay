package com.hiveelpay.boot.service.channel.hiveel;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: wilson
 * @date: 17/8/21
 * @description:
 */
@Component
@ConfigurationProperties(prefix = "config.search")
public class SearchConfig {

    private String carUpdate;

    public String getCarUpdate() {
        return carUpdate;
    }

    public void setCarUpdate(String carUpdate) {
        this.carUpdate = carUpdate;
    }
}

