package com.hiveelpay.boot.ctrl.exceptions;


import com.hiveelpay.common.exceptions.IErrorCode;

public enum HiveelPayErrorCode implements IErrorCode {
    ILLEGAL_PARAMETERS(CODE_ILLEGAL_PARAMETERS, "Illegal parameters."),

    PAYMENT_METHOD_NOT_SUPPORT(2100, "Payment Method Not Support!"),
    CREATE_CUSTOMER_ERROR(2101, "Create Customer Account Error!"),
    CREATE_CUSTOMER_ADDRESS_ERROR(2102, "Create Customer Address Account Error!"),
    CREATE_PAYMENT_METHOD_ERROR(2103, "Create payment method Error!"),
    PAYMENT_METHOD_NO_ADDRESS_ERROR(2104, "Payment method must have address information"),
    INCORRECT_PAY_PRODUCT(2105, "Wrong pay product!"),
    MULTI_VALID_MEMBERSHIP_SERVICE(2106, "Multi valid membership service!"),
    NULL_BIZ_ORDER(2107, "NULL biz-roder!"),
    CUSTOMER_SERVICE_ADD_ERROR(2108, "Customer service add error!"),
    CONVERTER_ERROR(2109, "Converter error!!"),

    ;

    private Integer errorCode;
    private String errorMessage;

    HiveelPayErrorCode(Integer errorCode, String errorMessage) {
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
        return "HiveelPayErrorCode{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
