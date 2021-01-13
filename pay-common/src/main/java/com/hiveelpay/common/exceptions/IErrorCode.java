package com.hiveelpay.common.exceptions;

import java.io.Serializable;

/**
 * interface of  hiveel biz error
 */
public interface IErrorCode extends Serializable {
    Integer CODE_ILLEGAL_PARAMETERS = 2000;

    Integer getErrorCode();

    String getErrorMessage();
}
