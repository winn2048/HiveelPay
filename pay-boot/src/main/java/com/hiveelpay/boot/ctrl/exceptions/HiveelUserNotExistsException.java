package com.hiveelpay.boot.ctrl.exceptions;

import com.hiveelpay.common.exceptions.ErrorCode;
import com.hiveelpay.common.exceptions.HiveelPayException;
import com.hiveelpay.common.exceptions.IErrorCode;

public class HiveelUserNotExistsException extends HiveelPayException {

    public HiveelUserNotExistsException() {
        super(ErrorCode.USER_NOT_EXISTS);
    }

    public HiveelUserNotExistsException(IErrorCode errorCode) {
        super(errorCode);
    }

    public HiveelUserNotExistsException(IErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }


}
