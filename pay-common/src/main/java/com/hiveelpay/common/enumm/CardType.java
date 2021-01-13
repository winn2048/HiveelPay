package com.hiveelpay.common.enumm;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 *
 */
public enum CardType implements Serializable {

    American_Express(1, "American Express"),
    Carte_Blanche(2, "Carte Blanche"),
    China_UnionPay(3, "China UnionPay"),

    Diners_Club(4, "Diners Club"),
    Discover(5, "Discover"),
    JCB(6, "JCB"),
    Laser(7, "Laser"),
    Maestro(8, "Maestro"),
    MasterCard(9, "MasterCard"),
    Solo(10, "Solo"),
    Switch(11, "Switch"),
    Visa(12, "Visa"),
    Unknown(13, "Unknown"),

    ILLEGAL(Integer.MIN_VALUE, "错误数值"),;

    private Integer val;
    private String name;


    private static final Map<Integer, CardType> map = Maps.newHashMap();

    static {
        for (CardType payProductType : CardType.values()) {
            map.put(payProductType.val, payProductType);
        }
    }

    CardType(Integer val, String name) {
        this.val = val;
        this.name = name;
    }

    public static CardType byValue(Integer val) {
        if (val == null) {
            return CardType.ILLEGAL;
        }
        CardType type = map.get(val);
        if (type == null) {
            return CardType.ILLEGAL;
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
        return "CardType{" +
                "val=" + val +
                ", name='" + name + '\'' +
                '}';
    }
}
