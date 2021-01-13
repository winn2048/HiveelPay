package com.hiveelpay.boot.ctrl.exceptions;

import com.hiveelpay.common.exceptions.HiveelPayException;
import com.hiveelpay.common.exceptions.IErrorCode;

public class HiveelCheckoutException extends HiveelPayException {

    public HiveelCheckoutException(IErrorCode errorCode) {
        super(errorCode);
    }

    public HiveelCheckoutException(IErrorCode errorCode, String msg) {
        super(errorCode, null, msg);
    }

    public HiveelCheckoutException(IErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }

    public HiveelCheckoutException(IErrorCode errorCode, Throwable throwable, String msg) {
        super(errorCode, throwable, msg);
    }


}
