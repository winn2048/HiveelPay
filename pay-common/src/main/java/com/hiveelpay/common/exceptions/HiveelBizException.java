package com.hiveelpay.common.exceptions;

public class HiveelBizException extends RuntimeException {
    private IErrorCode errorCode;
    private Throwable throwable;

    private String msg;

    public HiveelBizException(IErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
        this.msg = errorCode.getErrorMessage();
    }

    public HiveelBizException(IErrorCode errorCode, Throwable throwable) {
        super(errorCode.toString(), throwable);
        this.errorCode = errorCode;
        this.throwable = throwable;
        this.msg = errorCode.getErrorMessage();
    }

    public HiveelBizException(IErrorCode errorCode, Throwable throwable, String msg) {
        super(errorCode.toString(), throwable);
        this.errorCode = errorCode;
        this.throwable = throwable;
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
