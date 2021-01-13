package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * 发票状态
 */
public enum InvoiceStatusEnum implements Serializable {
    NEW_SAVED(1, "已保存"),//新生成，待结算
    SETTLED(8, "已结算"),

    ILLEGAL(Integer.MIN_VALUE, "错误数值"),
    ;

    private Integer val;
    private String name;


    private static final Map<Integer, InvoiceStatusEnum> map = Maps.newHashMap();

    static {
        for (InvoiceStatusEnum invoiceStatus : InvoiceStatusEnum.values()) {
            map.put(invoiceStatus.val, invoiceStatus);
        }
    }

    InvoiceStatusEnum(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    public static InvoiceStatusEnum byValue(Integer val) {
        if (val == null) {
            return InvoiceStatusEnum.ILLEGAL;
        }
        InvoiceStatusEnum type = map.get(val);
        if (type == null) {
            return InvoiceStatusEnum.ILLEGAL;
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
        return "InvoiceStatusEnum{" +
                "val=" + val +
                ", name='" + name + '\'' +
                '}';
    }
}
