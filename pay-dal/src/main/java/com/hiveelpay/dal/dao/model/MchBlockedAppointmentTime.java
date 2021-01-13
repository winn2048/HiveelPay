package com.hiveelpay.dal.dao.model;

import com.hiveelpay.common.enumm.BusinessTypeEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 商家不可预约时间
 */
public class MchBlockedAppointmentTime extends BaseDO implements Serializable {
    private String accountId;
    private String storeId;// 分店ID
    private String blockedId;
    private BusinessTypeEnum businessType;
    private Date blockedDateTime;
    private BlockedBy blockedBy;

    public static enum BlockedBy implements Serializable {
        CUSTOMER, MERCHANT
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BusinessTypeEnum getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessTypeEnum businessType) {
        this.businessType = businessType;
    }

    public Date getBlockedDateTime() {
        return blockedDateTime;
    }

    public void setBlockedDateTime(Date blockedDateTime) {
        this.blockedDateTime = blockedDateTime;
    }

    public String getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(String blockedId) {
        this.blockedId = blockedId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public BlockedBy getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(BlockedBy blockedBy) {
        this.blockedBy = blockedBy;
    }

    @Override
    public String toString() {
        return "MchBlockedAppointmentTime{" +
                "accountId='" + accountId + '\'' +
                ", businessType=" + businessType +
                ", blockedDateTime=" + blockedDateTime +
                ", id=" + id +
                ", blockedId=" + blockedId +
                ", storeId=" + storeId +
                ", createAt=" + createAt +
                ", blockedBy=" + blockedBy +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
