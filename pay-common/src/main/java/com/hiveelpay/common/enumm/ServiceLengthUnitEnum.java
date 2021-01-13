package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * 服务时长的单位
 */
public enum ServiceLengthUnitEnum implements Serializable {
    HOUR(2, "HOUR"),
    DAY(4, "DAY"),
    MONTH(6, "MONTH"),

    ILLEGAL(Integer.MIN_VALUE, "ILLEGAL");

    private Integer val;
    private String name;

    ServiceLengthUnitEnum(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    private static final Map<Integer, ServiceLengthUnitEnum> map = Maps.newHashMap();

    static {
        for (ServiceLengthUnitEnum billedMethodEnum : ServiceLengthUnitEnum.values()) {
            map.put(billedMethodEnum.val, billedMethodEnum);
        }
    }

    public static ServiceLengthUnitEnum byVal(Integer val) {
        if (val == null) {
            return ServiceLengthUnitEnum.ILLEGAL;
        }
        ServiceLengthUnitEnum rs = map.get(val);
        if (rs == null) {
            return ServiceLengthUnitEnum.ILLEGAL;
        }
        return rs;
    }

    public Integer getVal() {
        return val;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ServiceLengthUnitEnum{" +
                "val=" + val +
                ", name='" + name + '\'' +
                '}';
    }
}
