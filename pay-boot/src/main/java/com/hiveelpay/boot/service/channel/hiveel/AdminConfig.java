package com.hiveelpay.boot.service.channel.hiveel;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: wilson
 * @date: 17/8/21
 * @description:
 */
@Component
@ConfigurationProperties(prefix = "config.admin")
public class AdminConfig {


    /**
     * 联系人，格式是："wilson:9099908811,Iris:1111111111"
     */
    private String contacts;

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

}

