package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * 支付产品的状态
 */
public enum PayProductStatusEnum implements Serializable {

    SAVED(1, "已保存"),
    SELLING(2, "可以购买"),
    LOCKED(3, "已锁定"),
    OUT_OF_ORDER(4, "不再服务了"),
    SOLD(5, "卖完了"),

    ILLEGAL(Integer.MIN_VALUE, "错误数值"),
    ;

    private Integer val;
    private String name;


    private static final Map<Integer, PayProductStatusEnum> map = Maps.newHashMap();

    static {
        for (PayProductStatusEnum payProductStatus : PayProductStatusEnum.values()) {
            map.put(payProductStatus.val, payProductStatus);
        }
    }

    PayProductStatusEnum(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    public static PayProductStatusEnum byValue(Integer val) {
        if (val == null) {
            return PayProductStatusEnum.ILLEGAL;
        }
        PayProductStatusEnum type = map.get(val);
        if (type == null) {
            return PayProductStatusEnum.ILLEGAL;
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
        return "PayProductStatus{" +
                "val=" + val +
                ", name='" + name + '\'' +
                '}';
    }
}
