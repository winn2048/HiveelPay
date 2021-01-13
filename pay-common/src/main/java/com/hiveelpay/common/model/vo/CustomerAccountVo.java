package com.hiveelpay.common.model.vo;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CustomerAccountVo implements Serializable {
    private String userId;


    protected String customerId;//create by HiveelPay and used  for BrainTree.
    protected String firstName;
    protected String lastName;

    private List<PaymentMethodVo> paymentMethodList = Collections.emptyList();

    private List<CustomerValidServiceVo> validServiceList = Collections.emptyList();

    private Date createAt;
    private Date lastUpdateAt;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(Date lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    public List<PaymentMethodVo> getPaymentMethodList() {
        return paymentMethodList;
    }

    public void setPaymentMethodList(List<PaymentMethodVo> paymentMethodList) {
        this.paymentMethodList = paymentMethodList;
    }

    public List<CustomerValidServiceVo> getValidServiceList() {
        return validServiceList;
    }

    public void setValidServiceList(List<CustomerValidServiceVo> validServiceList) {
        this.validServiceList = validServiceList;
    }

    @Override
    public String toString() {
        return "CustomerAccountVo{" +
                "userId='" + userId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", createAt=" + createAt +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
