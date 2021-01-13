package com.hiveelpay.dal.dao.model;

import java.io.Serializable;

public class CreditCard extends PaymentMethod implements Serializable {

    private String addressId;
    private String billingAddressId;//address target Id
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
    private String debit;
    private String durbinRegulated;
    private String healthcare;
    private String payroll;
    private String prepaid;
    private String productId;
    private String countryOfIssuance;
    private String uniqueNumberIdentifier;

    private Address address;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(String billingAddressId) {
        this.billingAddressId = billingAddressId;
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

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getDurbinRegulated() {
        return durbinRegulated;
    }

    public void setDurbinRegulated(String durbinRegulated) {
        this.durbinRegulated = durbinRegulated;
    }

    public String getHealthcare() {
        return healthcare;
    }

    public void setHealthcare(String healthcare) {
        this.healthcare = healthcare;
    }

    public String getPayroll() {
        return payroll;
    }

    public void setPayroll(String payroll) {
        this.payroll = payroll;
    }

    public String getPrepaid() {
        return prepaid;
    }

    public void setPrepaid(String prepaid) {
        this.prepaid = prepaid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCountryOfIssuance() {
        return countryOfIssuance;
    }

    public void setCountryOfIssuance(String countryOfIssuance) {
        this.countryOfIssuance = countryOfIssuance;
    }

    public String getUniqueNumberIdentifier() {
        return uniqueNumberIdentifier;
    }

    public void setUniqueNumberIdentifier(String uniqueNumberIdentifier) {
        this.uniqueNumberIdentifier = uniqueNumberIdentifier;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "addressId='" + addressId + '\'' +
                ", billingAddressId='" + billingAddressId + '\'' +
                ", bin='" + bin + '\'' +
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
                ", debit='" + debit + '\'' +
                ", durbinRegulated='" + durbinRegulated + '\'' +
                ", healthcare='" + healthcare + '\'' +
                ", payroll='" + payroll + '\'' +
                ", prepaid='" + prepaid + '\'' +
                ", productId='" + productId + '\'' +
                ", countryOfIssuance='" + countryOfIssuance + '\'' +
                ", uniqueNumberIdentifier='" + uniqueNumberIdentifier + '\'' +
                ", token='" + token + '\'' +
                ", customerId='" + customerId + '\'' +
                ", isDefault=" + isDefault +
                ", imageUrl='" + imageUrl + '\'' +
                ", id=" + id +
                ", syncStatus=" + syncStatus +
                ", createAt=" + createAt +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
