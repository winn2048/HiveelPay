package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 *
 */
public enum BusinessTypeEnum implements Serializable {

    OIL_CHANGE(1, "Oil Change"),
    PRE_INSPECTION(2, "Pre-Inspection"),
    TRADE_IN(3, "Trade-in"),

    ILLEGAL(Integer.MIN_VALUE, "错误数值"),
    ;

    private Integer val;
    private String name;


    private static final Map<Integer, BusinessTypeEnum> map = Maps.newHashMap();

    static {
        for (BusinessTypeEnum payProductType : BusinessTypeEnum.values()) {
            map.put(payProductType.val, payProductType);
        }
    }

    BusinessTypeEnum(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    public static BusinessTypeEnum byValue(Integer val) {
        if (val == null) {
            return BusinessTypeEnum.ILLEGAL;
        }
        BusinessTypeEnum type = map.get(val);
        if (type == null) {
            return BusinessTypeEnum.ILLEGAL;
        }
        return type;
    }


    public Integer getVal() {
        return val;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "BusinessTypeEnum{" +
                "val=" + val +
                ", name='" + name + '\'' +
                '}';
    }
}
