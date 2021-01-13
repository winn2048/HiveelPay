package com.hiveelpay.common.domain;

import java.io.Serializable;

public enum ResultStatus implements IResultStatus, Serializable {
    SUCCESS(CODE_SUCCESS, "SUCCESS"),
    FAILED(CODE_FAILED, "FAILED");

    private Integer code;
    private String msg;

    ResultStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public String toString() {
        return "{'code':" + this.getCode() + ",'msg':'" + this.getMessage() + "'}";
    }
}
