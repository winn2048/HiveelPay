package com.hiveelpay.common.model.vo;

import java.io.Serializable;

public class CustomerValidServiceVo implements Serializable {
    private String serviceId;
    private String customerId;
    private String serviceName;
    private String bizOrderNo;// biz order no
    private String productId;// purchased product ID
    private Integer serviceType;//服务类型
    private Integer serviceStatus;//

    private String startTime;
    private String endTime;

    protected String createAt;
    protected String lastUpdateAt;

    private ProductVo product;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(Integer serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(String lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    public ProductVo getProduct() {
        return product;
    }

    public void setProduct(ProductVo product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "CustomerValidServiceVo{" +
                "serviceId='" + serviceId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", bizOrderNo='" + bizOrderNo + '\'' +
                ", productId='" + productId + '\'' +
                ", serviceType=" + serviceType +
                ", serviceStatus=" + serviceStatus +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", createAt='" + createAt + '\'' +
                ", product='" + product + '\'' +
                ", lastUpdateAt='" + lastUpdateAt + '\'' +
                '}';
    }
}
