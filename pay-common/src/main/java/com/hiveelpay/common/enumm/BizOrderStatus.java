package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 *
 */
public enum BizOrderStatus implements Serializable {

    SAVED(1, "已保存"),
    EXPIRED(-1, "超时未支付"),
    /**
     * 支付
     */
    PAY_START(2, "开始支付"),
    //    PAYING(3, "支付中"),
    PAY_SUCCESSED(6, "支付成功"),
    PAY_FAILED(-4, "支付失败"),
    /**
     * 服务
     */
    SERVICE_SETUPED(8, "服务已生成"),
    CANCELED(-9, "服务取消"),
    SERVICE_ING(9, "服务中"),
    REFUNDED(-10, "已退款"),
    SERVICE_END(11, "订单服务终止"),

    ILLEGAL(Integer.MIN_VALUE, "错误数值"),
    ;

    private Integer val;
    private String name;


    private static final Map<Integer, BizOrderStatus> map = Maps.newHashMap();

    static {
        for (BizOrderStatus payProductType : BizOrderStatus.values()) {
            map.put(payProductType.val, payProductType);
        }
    }

    BizOrderStatus(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    public static BizOrderStatus byValue(Integer val) {
        if (val == null) {
            return BizOrderStatus.ILLEGAL;
        }
        BizOrderStatus type = map.get(val);
        if (type == null) {
            return BizOrderStatus.ILLEGAL;
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
        return "BizOrderStatus{" +
                "val=" + val +
                ", name='" + name + '\'' +
                '}';
    }
}
