package com.hiveelpay.dal.dao.model;

import com.hiveelpay.common.enumm.AppointmentStatus;
import com.hiveelpay.common.enumm.AppointmentType;
import com.hiveelpay.common.enumm.BusinessTypeEnum;
import com.hiveelpay.common.enumm.EngineType;

import java.io.Serializable;
import java.util.Date;

/**
 * 预约申请
 */
public class AppointmentDoc extends BaseDO implements Serializable {

    private String customerId;//
    private String bizOrderNo;//bizOrderID
    private String appointmentId;

    private String invoiceId;//发票ID

    private String toMchId;//

    private String toStoreId;

    private String firstName;
    private String lastName;

    private Date appointmentTime;//预约时间

    private AppointmentStatus appointmentStatus;
    private BusinessTypeEnum businessType;
    private AppointmentType appointmentType;// 用哪种方式预约的。


    private String email;
    private String phoneNumber;

    private String street;
    private String apt;
    private String city;
    private String state;
    private String zipcode;


    private String vin;
    private String plateState;
    private Integer year;
    private String make;
    private String model;
    private Integer mileage;

    private String carMaxImgUrl; //
    private Long quotePrice; // dealer 报价

    private String previousOfferPrice;
    private EngineType engineType;

    private String titleStatus;

    private String companyName;
    private String whereCarAndKey;
    private String remark;

    public Long getQuotePrice() {
        return quotePrice;
    }

    public void setQuotePrice(Long quotePrice) {
        this.quotePrice = quotePrice;
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

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        if (phoneNumber == null) {
            return phoneNumber;
        }
        phoneNumber = phoneNumber.replaceAll("\\+", "")
                .replaceAll("-", "")
                .replaceAll("_", "")
                .replaceAll(" ", "")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "").trim();
        if (phoneNumber.length() > 10) {
            phoneNumber = phoneNumber.substring(phoneNumber.length() - 10);
        }
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

    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public BusinessTypeEnum getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessTypeEnum businessType) {
        this.businessType = businessType;
    }

    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
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


    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public String getCarMaxImgUrl() {
        return carMaxImgUrl;
    }

    public void setCarMaxImgUrl(String carMaxImgUrl) {
        this.carMaxImgUrl = carMaxImgUrl;
    }


    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    @Override
    public String toString() {
        return "AppointmentDoc{" +
                "customerId='" + customerId + '\'' +
                ", bizOrderNo='" + bizOrderNo + '\'' +
                ", appointmentId='" + appointmentId + '\'' +
                ", invoiceId='" + invoiceId + '\'' +
                ", toMchId='" + toMchId + '\'' +
                ", toStoreId='" + toStoreId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", appointmentTime=" + appointmentTime +
                ", appointmentStatus=" + appointmentStatus +
                ", appointmentType=" + appointmentType +
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
                ", companyName='" + companyName + '\'' +
                ", whereCarAndKey='" + whereCarAndKey + '\'' +
                ", remark='" + remark + '\'' +
                ", carMaxImgUrl='" + carMaxImgUrl + '\'' +
                ", id=" + id +
                ", quotePrice=" + quotePrice +
                ", createAt=" + createAt +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
