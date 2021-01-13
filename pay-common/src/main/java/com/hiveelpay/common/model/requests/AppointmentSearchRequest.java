package com.hiveelpay.common.model.requests;

import com.hiveelpay.common.enumm.AppointmentStatus;
import com.hiveelpay.common.enumm.BusinessTypeEnum;
import com.hiveelpay.common.enumm.TradeInTypeEnum;

import java.io.Serializable;

/**
 *
 */
public class AppointmentSearchRequest implements ISearchRequest, Serializable {

    private String mchId;//商户ID
    private String keyWords;//关键词
    private String storeId;// 店的ID
    private AppointmentStatus appointmentStatus;// 状态
    private BusinessTypeEnum businessType;//businessType
    private TradeInTypeEnum tradeInType;
    private String startDateStr;//预约开始时间
    private String endDateStr;//预约截止时间

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public BusinessTypeEnum getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessTypeEnum businessType) {
        this.businessType = businessType;
    }

    public TradeInTypeEnum getTradeInType() {
        return tradeInType;
    }

    public void setTradeInType(TradeInTypeEnum tradeInType) {
        this.tradeInType = tradeInType;
    }

    @Override
    public String toString() {
        return "AppointmentSearchRequest{" +
                "mchId='" + mchId + '\'' +
                ", keyWords='" + keyWords + '\'' +
                ", storeId='" + storeId + '\'' +
                ", appointmentStatus=" + appointmentStatus +
                ", businessType=" + businessType +
                ", startDateStr='" + startDateStr + '\'' +
                ", endDateStr='" + endDateStr + '\'' +
                '}';
    }
}
