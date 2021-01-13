package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * 支付方式，行用卡、paypal
 */
public enum PaymentMethodTypeEnum implements Serializable {
    CREDITCARD(1, "CreditCard"),
    PAYPAL(2, "PayPal"),

    ILLEGAL(Integer.MIN_VALUE, "ILLEGAL");

    private Integer val;
    private String name;

    PaymentMethodTypeEnum(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    private static final Map<Integer, PaymentMethodTypeEnum> map = Maps.newHashMap();

    static {
        for (PaymentMethodTypeEnum paymentMethodTypeEnum : PaymentMethodTypeEnum.values()) {
            map.put(paymentMethodTypeEnum.val, paymentMethodTypeEnum);
        }
    }

    public static PaymentMethodTypeEnum byVal(Integer val) {
        if (val == null) {
            return ILLEGAL;
        }
        PaymentMethodTypeEnum rs = map.get(val);
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
