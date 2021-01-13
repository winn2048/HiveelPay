package com.hiveelpay.common.util;

import com.fasterxml.uuid.Generators;
import com.google.common.base.Strings;

public class HiveelID {
    private static Object lock = new Object();

    private HiveelID() {
    }

    private static class InstanceHolder {
        private static HiveelID INSTANCE = new HiveelID();
    }

    public static HiveelID getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * @return length()=32
     */
    public String getRandomId() {
        synchronized (lock) {
            String uuid = Generators.randomBasedGenerator().generate().toString();
            uuid = uuid.replaceAll("-", "");
            return uuid.toUpperCase();
        }
    }

    public String getRandomId(String prefix) {
        return Strings.nullToEmpty(prefix).trim() + getRandomId().toUpperCase();
    }

    public String getRandomId(String prefix, String suffix) {
        return Strings.emptyToNull(prefix).trim() + getRandomId() + Strings.emptyToNull(suffix).trim().toUpperCase();
    }

    public static void main(String args[]) {
        System.out.println(getInstance().getRandomId());
        System.out.println(getInstance().getRandomId().length());
    }

}
