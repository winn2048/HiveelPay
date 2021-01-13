package com.hiveelpay.common.model.vo;

import java.io.Serializable;

public class ServiceTimeVo implements Serializable {
    private String bizOrderNo;

    private String serviceStartTime;// 开始时间
    private String serviceEndTime;// 失效时间

    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo;
    }

    public String getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(String serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public String getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(String serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    @Override
    public String toString() {
        return "ServiceTimeVo{" +
                "bizOrderNo='" + bizOrderNo + '\'' +
                ", serviceStartTime='" + serviceStartTime + '\'' +
                ", serviceEndTime='" + serviceEndTime + '\'' +
                '}';
    }
}
