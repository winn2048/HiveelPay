package com.hiveelpay.common.exceptions;


public enum ErrorCode implements IErrorCode {
    ILLEGAL_PARAMETERS(CODE_ILLEGAL_PARAMETERS, "Illegal parameters."),
    DATE_FMT_ERROR(2003, "Date FMT error!"),
    CREDITCARD_EXP_DATE_FMT_ERROR(2004, "CreditCard experication date string to date FMT error!"),
    USER_NOT_EXISTS(2005, "User Not Exists."),
    ;

    private Integer errorCode;
    private String errorMessage;

    ErrorCode(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "ErrorCode{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
