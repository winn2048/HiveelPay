package com.hiveelpay.common.model.requests;

import com.hiveelpay.common.enumm.PayProductTypeEnum;

import java.io.Serializable;

/**
 * 调用 搜索引擎 更新车服务的 请求 封装
 */
public class SearchEngineUpdateCarServiceRequest implements Serializable {
    private String carId;
    private PayProductTypeEnum productType;
    private String serviceStartDate;
    private String serviceEndDate;
    private String datePattern;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public PayProductTypeEnum getProductType() {
        return productType;
    }

    public void setProductType(PayProductTypeEnum productType) {
        this.productType = productType;
    }

    public String getServiceStartDate() {
        return serviceStartDate;
    }

    public void setServiceStartDate(String serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    public String getServiceEndDate() {
        return serviceEndDate;
    }

    public void setServiceEndDate(String serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    @Override
    public String toString() {
        return "SearchEngineUpdateCarServiceRequest{" +
                "carId='" + carId + '\'' +
                ", productType=" + productType +
                ", serviceStartDate='" + serviceStartDate + '\'' +
                ", serviceEndDate='" + serviceEndDate + '\'' +
                ", datePattern='" + datePattern + '\'' +
                '}';
    }
}
