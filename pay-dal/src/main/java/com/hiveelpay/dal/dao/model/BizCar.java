package com.hiveelpay.dal.dao.model;

import com.hiveelpay.common.enumm.SyncStatus;

import java.io.Serializable;
import java.util.Date;

public class BizCar implements Serializable {
    private String bizOrderNo;
    private String carId;
    private String zipcode;
    private SyncStatus syncStatus;//数据同步的状态
    private Byte notifyCount;
    private Date createAt;
    private Date lastUpdateAt;

    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public SyncStatus getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Byte getNotifyCount() {
        return notifyCount;
    }

    public void setNotifyCount(Byte notifyCount) {
        this.notifyCount = notifyCount;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(Date lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "BizCar{" +
                "bizOrderNo='" + bizOrderNo + '\'' +
                ", carId='" + carId + '\'' +
                ", syncStatus=" + syncStatus +
                ", notifyCount=" + notifyCount +
                ", createAt=" + createAt +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
