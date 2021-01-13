package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 *
 */
public enum TradeInTypeEnum implements Serializable {

    NONE(1, "NONE"),
    CARMAX(2, "CARMAX"),

    ILLEGAL(Integer.MIN_VALUE, "错误数值"),
    ;

    private Integer val;
    private String name;


    private static final Map<Integer, TradeInTypeEnum> map = Maps.newHashMap();

    static {
        for (TradeInTypeEnum payProductType : TradeInTypeEnum.values()) {
            map.put(payProductType.val, payProductType);
        }
    }

    TradeInTypeEnum(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    public static TradeInTypeEnum byValue(Integer val) {
        if (val == null) {
            return TradeInTypeEnum.ILLEGAL;
        }
        TradeInTypeEnum type = map.get(val);
        if (type == null) {
            return TradeInTypeEnum.ILLEGAL;
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
        return "TradeInTypeEnum{" +
                "val=" + val +
                ", name='" + name + '\'' +
                '}';
    }
}
