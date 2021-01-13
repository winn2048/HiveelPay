package com.hiveelpay.boot.ctrl.exceptions;

import com.hiveelpay.common.exceptions.HiveelPayException;
import com.hiveelpay.common.exceptions.IErrorCode;

public class CustomerAPIException extends HiveelPayException {

    public CustomerAPIException(IErrorCode errorCode) {
        super(errorCode);
    }

    public CustomerAPIException(IErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }


}
