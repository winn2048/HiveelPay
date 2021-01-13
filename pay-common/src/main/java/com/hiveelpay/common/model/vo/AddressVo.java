package com.hiveelpay.common.model.vo;

import java.io.Serializable;

public class AddressVo implements Serializable {
    private String addressId;
    private String customerId;

    private String firstName;
    private String lastName;

    private String company;
    private String addressTyp;

    private String streetAddress;//111 Main St
    private String locality;//Chicago
    private String region;//IL
    private String postalCode;//60622

    private String countryCodeAlpha2;
    private String countryCodeAlpha3;
    private String countryCodeNumeric;
    private String countryName;
    private String extendedAddress;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddressTyp() {
        return addressTyp;
    }

    public void setAddressTyp(String addressTyp) {
        this.addressTyp = addressTyp;
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

    @Override
    public String toString() {
        return "AddressVo{" +
                "addressId='" + addressId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", company='" + company + '\'' +
                ", addressTyp='" + addressTyp + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", locality='" + locality + '\'' +
                ", region='" + region + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", countryCodeAlpha2='" + countryCodeAlpha2 + '\'' +
                ", countryCodeAlpha3='" + countryCodeAlpha3 + '\'' +
                ", countryCodeNumeric='" + countryCodeNumeric + '\'' +
                ", countryName='" + countryName + '\'' +
                ", extendedAddress='" + extendedAddress + '\'' +
                '}';
    }
}
