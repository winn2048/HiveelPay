package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * 支付产品的状态
 */
public enum CustomerServiceStatusEnum implements Serializable {

    INIT(0, "已初始化，服务未开始"),
    IN_SERVICE(1, "服务中"),
    EXPIRED(-1, "过期"),
    USED(-2, "已使用"),
    CANCELED(-3, "取消了"),

    ILLEGAL(Integer.MIN_VALUE, "错误数值"),
    ;

    private Integer val;
    private String name;


    private static final Map<Integer, CustomerServiceStatusEnum> map = Maps.newHashMap();

    static {
        for (CustomerServiceStatusEnum payProductStatus : CustomerServiceStatusEnum.values()) {
            map.put(payProductStatus.val, payProductStatus);
        }
    }

    CustomerServiceStatusEnum(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    public static CustomerServiceStatusEnum byValue(Integer val) {
        if (val == null) {
            return CustomerServiceStatusEnum.ILLEGAL;
        }
        CustomerServiceStatusEnum type = map.get(val);
        if (type == null) {
            return CustomerServiceStatusEnum.ILLEGAL;
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
