package com.hiveelpay.common.model.vo;

import com.hiveelpay.common.enumm.BusinessTypeEnum;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 */
public class MchBlockedAppointmentTimeVo implements Serializable {
    private String accountId;
    private String storeId;// 分店ID
    private BusinessTypeEnum businessType;
    private String dateStr;// MMddyyyy
    private String[] times;// HHmm

    private String blockedBy;

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

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String[] getTimes() {
        return times;
    }

    public void setTimes(String[] times) {
        this.times = times;
    }


    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(String blockedBy) {
        this.blockedBy = blockedBy;
    }

    @Override
    public String toString() {
        return "MchBlockedAppointmentTimeVo{" +
                "accountId='" + accountId + '\'' +
                ", businessType=" + businessType +
                ", dateStr='" + dateStr + '\'' +
                ", storeId='" + storeId + '\'' +
                ", times='" + times + '\'' +
                ", blockedBy='" + blockedBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MchBlockedAppointmentTimeVo that = (MchBlockedAppointmentTimeVo) o;
        return Objects.equals(accountId, that.accountId) &&
                Objects.equals(storeId, that.storeId) &&
                businessType == that.businessType &&
                Objects.equals(dateStr, that.dateStr) &&
                Arrays.equals(times, that.times) &&
                Objects.equals(blockedBy, that.blockedBy);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(accountId, storeId, businessType, dateStr, blockedBy);
        result = 31 * result + Arrays.hashCode(times);
        return result;
    }
}
