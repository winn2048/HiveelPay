package com.hiveelpay.common.model.requests;

import com.hiveelpay.common.enumm.BizOrderStatus;

import java.io.Serializable;

public class BizOrderSearchRequest implements ISearchRequest, Serializable {
    private String mchId;
    private String keyWord;

    private BizOrderStatus orderStatus;

    private String productType;

    private String startDateStr;//开始时间
    private String endDateStr;//截止时间

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
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

    public BizOrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(BizOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        return "BizOrderSearchRequest{" +
                "mchId='" + mchId + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", startDateStr='" + startDateStr + '\'' +
                ", productType='" + productType + '\'' +
                ", endDateStr='" + endDateStr + '\'' +
                '}';
    }
}
