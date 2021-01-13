package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * 数据同步的状态
 */
public enum SyncStatus implements Serializable {
    SAVED(0, "saved"),
    SYNC_FAILED(3, "failed"),
    SYNC_SUCCESSED(1, "success"),
    ILLEGAL(Integer.MIN_VALUE, "ILLEGAL");

    private Integer val;
    private String name;


    private static final Map<Integer, SyncStatus> map = Maps.newHashMap();

    static {
        for (SyncStatus syncStatus : SyncStatus.values()) {
            map.put(syncStatus.val, syncStatus);
        }
    }

    SyncStatus(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    public static SyncStatus valueOf(Integer val) {
        if (val == null) {
            return ILLEGAL;
        }
        SyncStatus ss = map.get(val);
        if (ss == null) {
            return ILLEGAL;
        }
        return ss;
    }

    public Integer getVal() {
        return val;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SyncStatus{" +
                "val=" + val +
                ", name='" + name + '\'' +
                '}';
    }
}
