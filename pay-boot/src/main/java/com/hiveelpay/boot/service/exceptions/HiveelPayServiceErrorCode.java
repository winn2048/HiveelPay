package com.hiveelpay.boot.service.exceptions;


import com.hiveelpay.common.exceptions.IErrorCode;

public enum HiveelPayServiceErrorCode implements IErrorCode {
    ILLEGAL_PARAMETERS(CODE_ILLEGAL_PARAMETERS, "Illegal parameters."),

    NO_USER(2020, "No User!"),

    BRAINTREE_CREATE_CUSTOMER_ERROR(2100, "Create customer error!"),
    BRAINTREE_ADDRESS_CREATE_ERROR(2101, "Create address error!"),
    BRAINTREE_CREDITCARD_CREATE_ERROR(2102, "Create creditcard error!"),

    DATA_SAVE_ERROR(2400, "Save data error!"),
    DATA_SAVE_CUSTOMER_ERROR(2401, "Save data customer error!"),
    DATA_SAVE_ADDRESS_ERROR(2402, "Save data address error!"),
    DATA_SAVE_CREDIRECTCARD_ERROR(2403, "Save data creditcard error!"),
    DATA_SAVE_BIZ_ORDER_ERROR(2404, "Save BizOrder  error!"),
    DATA_SAVE_PAYMENT_METHOD_ERROR(2405, "Save PaymentMethod  error!"),

    DATA_UPDATE_ERROR(2406, "Update data error!"),
    DATA_UPDATE_CUSTOMER_ERROR(2407, "Update data customer error!"),
    DATA_UPDATE_ADDRESS_ERROR(2408, "Update data address error!"),
    DATA_UPDATE_CREDIRECTCARD_ERROR(2409, "Update data creditcard error!"),

    CARD_EXPIRED(2450, "Card expired!"),

    MULTI_VALID_BIZ_ORDER_ERROR(2500, "Multi valid biz order error!"),
    CANCEL_APPOINTMENT_ERROR(2520, "Cancel appointment error!"),
    INVALID_APPOINTMENT_ERROR(2521, "Invalid appointment error!"),
    ;

    private Integer errorCode;
    private String errorMessage;

    HiveelPayServiceErrorCode(Integer errorCode, String errorMessage) {
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
