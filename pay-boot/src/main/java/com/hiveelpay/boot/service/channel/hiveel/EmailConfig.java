package com.hiveelpay.boot.service.channel.hiveel;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: wilson
 * @date: 17/8/21
 * @description: email:
 * api: "http://hiveel3.hiveel-mysql.173.254.196.10.xip.io/api/email"
 * errorApi: "http://hiveel3:8080/error/email"
 * apiKey: "hiveel"
 */
@Component
@ConfigurationProperties(prefix = "config.email")
public class EmailConfig {

    private boolean switchOn;
    private String api;
    private String errorApi;
    private String apiKey;
    private String appointmentViewUrl;


    public String getApi() {
        return api;
    }

    public void setSwitchOn(boolean switchOn) {
        this.switchOn = switchOn;
    }

    public boolean isSwitchOn() {
        return switchOn;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getErrorApi() {
        return errorApi;
    }

    public void setErrorApi(String errorApi) {
        this.errorApi = errorApi;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAppointmentViewUrl() {
        return appointmentViewUrl;
    }

    public void setAppointmentViewUrl(String appointmentViewUrl) {
        this.appointmentViewUrl = appointmentViewUrl;
    }

    @Override
    public String toString() {
        return "EmailConfig{" +
                "api='" + api + '\'' +
                ", errorApi='" + errorApi + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", appointmentViewUrl='" + appointmentViewUrl + '\'' +
                '}';
    }
}

