package com.hiveelpay.common.model.vo;

import com.hiveelpay.common.enumm.BusinessTypeEnum;

import java.io.Serializable;

public class AppointmentDocVo implements Serializable {

    private String customerId;
    private String bizOrderNo;

    private String appointmentId;

    private String toMchId;//

    private String toStoreId;

    private String firstName;
    private String lastName;

    private String appointmentDateTimeStr;//预约时间

    private String appointmentStatus;
    private String appointmentType;

    private BusinessTypeEnum businessType;

    private String email;
    private String phoneNumber;

    private String street;
    private String apt;
    private String city;
    private String state;
    private String zipcode;

    private String carMaxImgUrl;
    private String quotePrice; // dealer 报价

    private String vin;
    private String plateState;
    private Integer year;
    private String make;
    private String model;
    private Integer mileage;

    private String previousOfferPrice;

    private String titleStatus;
    private String engineType;

    private String createAt;
    private String lastUpdateAt;

    private String companyName;
    private String whereCarAndKey;
    private String remark;

    private BizOrderVo bizOrder;

    public String getQuotePrice() {
        return quotePrice;
    }

    public void setQuotePrice(String quotePrice) {
        this.quotePrice = quotePrice;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getToMchId() {
        return toMchId;
    }

    public void setToMchId(String toMchId) {
        this.toMchId = toMchId;
    }

    public String getToStoreId() {
        return toStoreId;
    }

    public void setToStoreId(String toStoreId) {
        this.toStoreId = toStoreId;
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

    public String getAppointmentDateTimeStr() {
        return appointmentDateTimeStr;
    }

    public void setAppointmentDateTimeStr(String appointmentDateTimeStr) {
        this.appointmentDateTimeStr = appointmentDateTimeStr;
    }

    public String getCarMaxImgUrl() {
        return carMaxImgUrl;
    }

    public void setCarMaxImgUrl(String carMaxImgUrl) {
        this.carMaxImgUrl = carMaxImgUrl;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getPlateState() {
        return plateState;
    }

    public void setPlateState(String plateState) {
        this.plateState = plateState;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getPreviousOfferPrice() {
        return previousOfferPrice;
    }

    public void setPreviousOfferPrice(String previousOfferPrice) {
        this.previousOfferPrice = previousOfferPrice;
    }

    public String getTitleStatus() {
        return titleStatus;
    }

    public void setTitleStatus(String titleStatus) {
        this.titleStatus = titleStatus;
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

    public BusinessTypeEnum getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessTypeEnum businessType) {
        this.businessType = businessType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWhereCarAndKey() {
        return whereCarAndKey;
    }

    public void setWhereCarAndKey(String whereCarAndKey) {
        this.whereCarAndKey = whereCarAndKey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BizOrderVo getBizOrder() {
        return bizOrder;
    }

    public void setBizOrder(BizOrderVo bizOrder) {
        this.bizOrder = bizOrder;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getApt() {
        return apt;
    }

    public void setApt(String apt) {
        this.apt = apt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    @Override
    public String toString() {
        return "AppointmentDocVo{" +
                "customerId='" + customerId + '\'' +
                ", bizOrderNo='" + bizOrderNo + '\'' +
                ", appointmentId='" + appointmentId + '\'' +
                ", toMchId='" + toMchId + '\'' +
                ", toStoreId='" + toStoreId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", appointmentDateTimeStr='" + appointmentDateTimeStr + '\'' +
                ", appointmentStatus='" + appointmentStatus + '\'' +
                ", appointmentType='" + appointmentType + '\'' +
                ", businessType=" + businessType +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", street='" + street + '\'' +
                ", apt='" + apt + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", vin='" + vin + '\'' +
                ", plateState='" + plateState + '\'' +
                ", year=" + year +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", engineType='" + engineType + '\'' +
                ", mileage=" + mileage +
                ", previousOfferPrice='" + previousOfferPrice + '\'' +
                ", titleStatus='" + titleStatus + '\'' +
                ", createAt='" + createAt + '\'' +
                ", lastUpdateAt='" + lastUpdateAt + '\'' +
                ", companyName='" + companyName + '\'' +
                ", whereCarAndKey='" + whereCarAndKey + '\'' +
                ", carMaxImgUrl='" + carMaxImgUrl + '\'' +
                ", remark='" + remark + '\'' +
                ", quotePrice='" + quotePrice + '\'' +
                ", bizOrder=" + bizOrder +
                '}';
    }
}
