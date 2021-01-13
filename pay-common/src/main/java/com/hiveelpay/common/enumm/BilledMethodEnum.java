package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * 账单方式，一次/每月一次/每年一次
 */
public enum BilledMethodEnum implements Serializable {
    ONETIME(0, "onetime"),
    MONTHLY(1, "monthly"),

    ILLEGAL(Integer.MIN_VALUE, "ILLEGAL");

    private Integer val;
    private String name;

    BilledMethodEnum(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    private static final Map<Integer, BilledMethodEnum> map = Maps.newHashMap();

    static {
        for (BilledMethodEnum billedMethodEnum : BilledMethodEnum.values()) {
            map.put(billedMethodEnum.val, billedMethodEnum);
        }
    }

    public static BilledMethodEnum byVal(Integer val) {
        if (val == null) {
            return ILLEGAL;
        }
        BilledMethodEnum rs = map.get(val);
        if (rs == null) {
            return ILLEGAL;
        }
        return rs;
    }

    public Integer getVal() {
        return val;
    }

    public String getName() {
        return name;
    }
}
