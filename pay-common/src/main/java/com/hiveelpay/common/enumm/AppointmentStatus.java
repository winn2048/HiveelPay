package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

public enum AppointmentStatus implements Serializable {
    PENDING(0, "待审核状态"),
    SAVED(1, "已保存"),
    INVALID(-1, "无效的"),//预约时间到达还未支付 或者 支付失败的
    VALID(7, "有效的"),//支付成功
    CANCELED(4, "取消了"),// 客户或者客服发起 取消
    CANCEL_AND_REFUNDED(3, "取消了并且退款了"),// 客户取消 并且已退款
    SERVICE_DONE(8, "服务完成"),//服务时间已过，自动设置为服务完成

    ILLEGAL(Integer.MIN_VALUE, "错误数值"),
    ;

    private Integer val;
    private String name;


    private static final Map<Integer, AppointmentStatus> map = Maps.newHashMap();

    static {
        for (AppointmentStatus appointmentStatus : AppointmentStatus.values()) {
            map.put(appointmentStatus.val, appointmentStatus);
        }
    }

    AppointmentStatus(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    public static AppointmentStatus byValue(Integer val) {
        if (val == null) {
            return AppointmentStatus.ILLEGAL;
        }
        AppointmentStatus type = map.get(val);
        if (type == null) {
            return AppointmentStatus.ILLEGAL;
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
        return "AppointmentStatus{" +
                "val=" + val +
                ", name='" + name + '\'' +
                '}';
    }
}
