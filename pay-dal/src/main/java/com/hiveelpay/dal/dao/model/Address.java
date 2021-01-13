package com.hiveelpay.dal.dao.model;

import com.hiveelpay.common.enumm.AddressType;

import java.io.Serializable;

/**
 * 地址信息
 */
public class Address extends BaseDO implements Serializable {
    private String addressId;
    private String customerId;

    private String firstName;
    private String lastName;

    private String company;
    private AddressType addressType = AddressType.address;

    private String streetAddress;//111 Main St
    private String locality;//Chicago
    private String region;//IL
    private String postalCode;//60622

    private String countryCodeAlpha2;
    private String countryCodeAlpha3;
    private String countryCodeNumeric;
    private String countryName;
    private String extendedAddress;

    private String targetId;//

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCountryCodeAlpha2() {
        return countryCodeAlpha2;
    }

    public void setCountryCodeAlpha2(String countryCodeAlpha2) {
        this.countryCodeAlpha2 = countryCodeAlpha2;
    }

    public String getCountryCodeAlpha3() {
        return countryCodeAlpha3;
    }

    public void setCountryCodeAlpha3(String countryCodeAlpha3) {
        this.countryCodeAlpha3 = countryCodeAlpha3;
    }

    public String getCountryCodeNumeric() {
        return countryCodeNumeric;
    }

    public void setCountryCodeNumeric(String countryCodeNumeric) {
        this.countryCodeNumeric = countryCodeNumeric;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getExtendedAddress() {
        return extendedAddress;
    }

    public void setExtendedAddress(String extendedAddress) {
        this.extendedAddress = extendedAddress;
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

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId='" + addressId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", company='" + company + '\'' +
                ", addressType=" + addressType +
                ", streetAddress='" + streetAddress + '\'' +
                ", locality='" + locality + '\'' +
                ", region='" + region + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", countryCodeAlpha2='" + countryCodeAlpha2 + '\'' +
                ", countryCodeAlpha3='" + countryCodeAlpha3 + '\'' +
                ", countryCodeNumeric='" + countryCodeNumeric + '\'' +
                ", countryName='" + countryName + '\'' +
                ", extendedAddress='" + extendedAddress + '\'' +
                ", id=" + id +
                ", syncStatus=" + syncStatus +
                ", targetId=" + targetId +
                ", createAt=" + createAt +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
