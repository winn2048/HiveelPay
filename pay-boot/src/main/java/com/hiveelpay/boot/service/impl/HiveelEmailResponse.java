package com.hiveelpay.boot.service.impl;

import java.io.Serializable;

/**
 * return :  {
 * "message": "ok",
 * "code": "SUCCESS",
 * "data": true
 * }
 */
public class HiveelEmailResponse implements Serializable {
    private String message;
    private String code;
    private Boolean data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getData() {
        return data;
    }

    public void setData(Boolean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HiveelEmailResponse{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}
