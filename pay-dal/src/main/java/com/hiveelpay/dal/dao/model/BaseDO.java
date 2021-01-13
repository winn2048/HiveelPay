package com.hiveelpay.dal.dao.model;

import com.hiveelpay.common.enumm.SyncStatus;

import java.io.Serializable;
import java.util.Date;

public class BaseDO implements Serializable {
    protected Long id;

    protected SyncStatus syncStatus;//数据同步的状态

    protected Date createAt;
    protected Date lastUpdateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public SyncStatus getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    @Override
    public String toString() {
        return "BaseDO{" +
                "id=" + id +
                "syncStatus=" + syncStatus +
                ", createAt=" + createAt +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
