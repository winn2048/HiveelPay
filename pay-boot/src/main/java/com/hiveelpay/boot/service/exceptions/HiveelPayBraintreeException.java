package com.hiveelpay.boot.service.exceptions;

import com.hiveelpay.common.exceptions.HiveelPayException;
import com.hiveelpay.common.exceptions.IErrorCode;

public class HiveelPayBraintreeException extends HiveelPayException {

    public HiveelPayBraintreeException(IErrorCode errorCode) {
        super(errorCode);
    }

    public HiveelPayBraintreeException(IErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }


}
