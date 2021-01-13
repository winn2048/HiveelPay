package com.hiveelpay.common.exceptions;

import com.google.common.base.Strings;

public class HiveelPayException extends RuntimeException {
    private IErrorCode errorCode;
    private Throwable throwable;

    private String msg;

    public HiveelPayException(IErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
        this.msg = errorCode.getErrorMessage();
    }

    public HiveelPayException(IErrorCode errorCode, Throwable throwable) {
        super(errorCode.toString(), throwable);
        this.errorCode = errorCode;
        this.throwable = throwable;
        this.msg = errorCode.getErrorMessage();
    }

    public HiveelPayException(IErrorCode errorCode, Throwable throwable, String msg) {
        super(errorCode.toString(), throwable);
        this.errorCode = errorCode;
        this.throwable = throwable;
        this.msg = msg;
    }

    public HiveelPayException(IErrorCode errorCode, String msg) {
        super(errorCode.toString() + "." + Strings.nullToEmpty(msg));
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getMsg() {
        return msg;
    }

}
