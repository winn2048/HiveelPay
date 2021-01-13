package com.hiveelpay.common.model.vo;

import java.io.Serializable;

public class PaymentMethodVo implements Serializable {

    protected String token;// payment method id
    protected String customerId;
    private boolean isDefault;
    private String imageUrl;
    private String paymentMethodType;

    private CreditCardVo creditCard;

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

    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public CreditCardVo getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardVo creditCard) {
        this.creditCard = creditCard;
    }

    @Override
    public String toString() {
        return "PaymentMethodVo{" +
                "token='" + token + '\'' +
                ", customerId='" + customerId + '\'' +
                ", isDefault=" + isDefault +
                ", imageUrl='" + imageUrl + '\'' +
                ", paymentMethodType=" + paymentMethodType +
                '}';
    }
}
