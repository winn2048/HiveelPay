package com.hiveelpay.dal.dao.model;

import com.hiveelpay.common.enumm.AccountType;
import com.hiveelpay.common.enumm.MerchantStatus;

import java.io.Serializable;
import java.util.Date;

public class CustomerAccount extends BaseDO implements Serializable {

    private String userId;// created by UserCenter, after user registered we can get a userId.

    /**
     * customer need
     */
    protected String customerId;//create by HiveelPay and used  for BrainTree.
    protected String firstName;
    protected String lastName;
    private String company;
    protected String email;
    private String fax;
    protected String phone;
    private String website;

    private String targetId;

    private AccountType accountType;//账号类型

    /**
     * merchant(dealer) need
     *
     * @return
     */

    private String ssn;
    private Date dateOfBirth;//yyyy-MM-dd

    private String legalName;
    private String dbaName;// doing business as name...
    private String taxId;
    private MerchantStatus merchantStatus = MerchantStatus.PENDING;


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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getDbaName() {
        return dbaName;
    }

    public void setDbaName(String dbaName) {
        this.dbaName = dbaName;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public MerchantStatus getMerchantStatus() {
        return merchantStatus;
    }

    public void setMerchantStatus(MerchantStatus merchantStatus) {
        this.merchantStatus = merchantStatus;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    @Override
    public String toString() {
        return "CustomerAccount{" +
                "userId='" + userId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", company='" + company + '\'' +
                ", email='" + email + '\'' +
                ", fax='" + fax + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", accountType=" + accountType +
                ", ssn='" + ssn + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", legalName='" + legalName + '\'' +
                ", dbaName='" + dbaName + '\'' +
                ", taxId='" + taxId + '\'' +
                ", merchantStatus=" + merchantStatus +
                ", targetId=" + targetId +
                ", id=" + id +
                ", createAt=" + createAt +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
