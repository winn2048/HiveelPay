package com.hiveelpay.dal.dao.model;

import com.hiveelpay.common.enumm.PaymentMethodTypeEnum;

import java.io.Serializable;
import java.util.Date;

public class PaymentMethod extends BaseDO implements Serializable {
    protected String token;// payment method id
    protected String customerId;
    private String customerTargetId;// customer target Id;
    protected boolean isDefault;
    protected String imageUrl;
    private PaymentMethodTypeEnum paymentMethodType;

    private PaymentMethod paymentMethod;

    private CreditCard creditCard;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerTargetId() {
        return customerTargetId;
    }

    public void setCustomerTargetId(String customerTargetId) {
        this.customerTargetId = customerTargetId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PaymentMethodTypeEnum getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(PaymentMethodTypeEnum paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "token='" + token + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerTargetId='" + customerTargetId + '\'' +
                ", isDefault=" + isDefault +
                ", imageUrl='" + imageUrl + '\'' +
                ", paymentMethodType=" + paymentMethodType +
                ", id=" + id +
                ", syncStatus=" + syncStatus +
                ", creditCard=" + creditCard +
                ", createAt=" + createAt +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
