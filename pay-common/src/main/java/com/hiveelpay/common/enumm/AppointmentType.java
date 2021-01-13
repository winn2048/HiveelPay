package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

public enum AppointmentType implements Serializable {
    REGULAR(0, "Regular"),// 正常的预约流程
    INSTANT(1, "Instant"),// 快速预约流程
    ILLEGAL(Integer.MIN_VALUE, "错误数值"),
            ;

    private static final Map<Integer, AppointmentType> map = Maps.newHashMap();

    static {
        Arrays.stream(AppointmentType.values()).forEach(i -> map.put(i.getVal(), i));
    }

    private Integer val;
    private String name;

    AppointmentType(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    public Integer getVal() {
        return val;
    }

    public String getName() {
        return name;
    }

    public static AppointmentType byVal(Integer val) {
        if(val==null){
            return AppointmentType.ILLEGAL;
        }
        AppointmentType at = map.get(val);
        if(at==null){
            return AppointmentType.ILLEGAL;
        }
        return at;
    }

    @Override
    public String toString() {
        return "AppointmentType{" +
                "val=" + val +
                ", name='" + name + '\'' +
                '}';
    }
}
