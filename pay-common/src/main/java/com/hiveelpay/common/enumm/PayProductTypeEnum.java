package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 *
 */
public enum PayProductTypeEnum implements Serializable {

    MEMBERSHIP(10, "MEMBERSHIP"),
    ADVANCING(12, "Advancing"),
    CAR_OF_DAY(14, "Car Of Day"),
    SEARCH_RESULT(20, "Search Result"),
    HIGHLIGHTING(22, "Highlighting"),
    OIL_CHANGE(30, "Oil Change"),
    PRE_INSPECTION(31, "Pre-Inspection"),
    TRADE_IN(34, "Trade-in"),

    ILLEGAL(Integer.MIN_VALUE, "错误数值"),
    ;

    private Integer val;
    private String name;


    private static final Map<Integer, PayProductTypeEnum> map = Maps.newHashMap();
    private static final Map<String, PayProductTypeEnum> map1 = Maps.newHashMap();

    static {
        for (PayProductTypeEnum payProductType : PayProductTypeEnum.values()) {
            map.put(payProductType.val, payProductType);
            map1.put(payProductType.name(), payProductType);
        }
    }

    PayProductTypeEnum(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    public static PayProductTypeEnum byValue(Integer val) {
        if (val == null) {
            return PayProductTypeEnum.ILLEGAL;
        }
        PayProductTypeEnum type = map.get(val);
        if (type == null) {
            return PayProductTypeEnum.ILLEGAL;
        }
        return type;
    }

    public static PayProductTypeEnum byName(String name) {
        PayProductTypeEnum type = map1.get(name);
        if (type == null) {
            return PayProductTypeEnum.ILLEGAL;
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
        return "PayProductType{" +
                "val=" + val +
                ", name='" + name + '\'' +
                '}';
    }
}
