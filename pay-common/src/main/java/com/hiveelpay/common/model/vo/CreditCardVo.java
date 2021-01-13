package com.hiveelpay.common.model.vo;

import java.io.Serializable;

public class CreditCardVo extends PaymentMethodVo implements Serializable {
    private String bin;
    private String cardType;//
    private String issuingBank;
    private String cardholderName;
    private String last4;
    private String customerLocation;

    private String expirationMonth;
    private String expirationYear;
    private boolean isExpired;
    private boolean isVenmoSdk;
    private String commercial;

    private AddressVo address;

    public AddressVo getAddress() {
        return address;
    }

    public void setAddress(AddressVo address) {
        this.address = address;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isVenmoSdk() {
        return isVenmoSdk;
    }

    public void setVenmoSdk(boolean venmoSdk) {
        isVenmoSdk = venmoSdk;
    }

    public String getCommercial() {
        return commercial;
    }

    public void setCommercial(String commercial) {
        this.commercial = commercial;
    }

    @Override
    public String toString() {
        return "CreditCardVo{" +
                "bin='" + bin + '\'' +
                ", cardType='" + cardType + '\'' +
                ", issuingBank='" + issuingBank + '\'' +
                ", cardholderName='" + cardholderName + '\'' +
                ", last4='" + last4 + '\'' +
                ", customerLocation='" + customerLocation + '\'' +
                ", expirationMonth='" + expirationMonth + '\'' +
                ", expirationYear='" + expirationYear + '\'' +
                ", isExpired=" + isExpired +
                ", isVenmoSdk=" + isVenmoSdk +
                ", commercial='" + commercial + '\'' +
                ", token='" + token + '\'' +
                ", customerId='" + customerId + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
