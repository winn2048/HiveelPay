package com.hiveelpay.dal.dao.model;

import com.hiveelpay.common.enumm.CustomerServiceStatusEnum;
import com.hiveelpay.common.enumm.PayProductTypeEnum;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class CustomerValidServices extends BaseDO implements Serializable {

    private String customerId;
    private String serviceId;
    private String serviceName;
    private String bizOrderNo;// biz order no
    private String productId;// purchased product ID

    private PayProductTypeEnum serviceType;//服务类型
    private CustomerServiceStatusEnum serviceStatus;//状态

    private Date startTime;
    private Date endTime;

    private PayProduct payProduct;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public PayProductTypeEnum getServiceType() {
        return serviceType;
    }

    public void setServiceType(PayProductTypeEnum serviceType) {
        this.serviceType = serviceType;
    }

    public CustomerServiceStatusEnum getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(CustomerServiceStatusEnum serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo;
    }

    public PayProduct getPayProduct() {
        return payProduct;
    }

    public void setPayProduct(PayProduct payProduct) {
        this.payProduct = payProduct;
    }

    @Override
    public String toString() {
        return "CustomerValidServices{" +
                "customerId='" + customerId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", productId='" + productId + '\'' +
                ", bizOrderNo='" + bizOrderNo + '\'' +
                ", serviceType=" + serviceType +
                ", serviceStatus=" + serviceStatus +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", payProduct=" + payProduct +
                ", id=" + id +
                ", createAt=" + createAt +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
