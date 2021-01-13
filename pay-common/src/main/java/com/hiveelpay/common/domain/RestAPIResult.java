package com.hiveelpay.common.domain;

import com.hiveelpay.common.exceptions.IErrorCode;

import java.io.Serializable;

/**
 * @param <T> Object
 * @author wilson
 */
public class RestAPIResult<T extends Object> implements Serializable {
    private Integer code;
    private String msg;
    private T data;


    public RestAPIResult(IErrorCode errorCode) {
        this.code = errorCode.getErrorCode();
        this.msg = errorCode.getErrorMessage();
    }

    public RestAPIResult(IResultStatus resultStatus) {
        this.code = resultStatus.getCode();
        this.msg = resultStatus.getMessage();
    }

    public RestAPIResult(IResultStatus resultStatus, T data) {
        this.code = resultStatus.getCode();
        this.msg = resultStatus.getMessage();
        this.data = data;
    }

    public RestAPIResult(IResultStatus resultStatus, T data, String message) {
        this.code = resultStatus.getCode();
        if (message == null) {
            this.msg = resultStatus.getMessage();
        } else {
            this.msg = message;
        }
        this.data = data;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RestAPIResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
