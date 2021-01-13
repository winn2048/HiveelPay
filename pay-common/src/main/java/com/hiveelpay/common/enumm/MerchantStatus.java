package com.hiveelpay.common.enumm;

import java.io.Serializable;

public enum MerchantStatus implements Serializable {
    PENDING,
    ACTIVE,
    SUSPENDED,
    UNRECOGNIZED;

    private MerchantStatus() {
    }
}
