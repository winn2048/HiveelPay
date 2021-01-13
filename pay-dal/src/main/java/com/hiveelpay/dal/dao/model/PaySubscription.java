package com.hiveelpay.dal.dao.model;

import java.io.Serializable;

/**
 * 支付订阅关系存储表
 */
public class PaySubscription extends BaseDO implements Serializable {

    private String bizOrderNo;
    private String planId;
    private String paymentMethodId;
    private Long amount;
    private Integer subReqCount;//订阅失败，重试的次数

    private String subscriptionId;
    private String status;

    private String errorMsg;

    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getSubReqCount() {
        return subReqCount;
    }

    public void setSubReqCount(Integer subReqCount) {
        this.subReqCount = subReqCount;
    }

    @Override
    public String toString() {
        return "PaySubscription{" +
                "bizOrderNo='" + bizOrderNo + '\'' +
                ", planId='" + planId + '\'' +
                ", paymentMethodId='" + paymentMethodId + '\'' +
                ", amount=" + amount +
                ", subscriptionId='" + subscriptionId + '\'' +
                ", status='" + status + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", id=" + id +
                ", createAt=" + createAt +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
