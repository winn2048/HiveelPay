package com.hiveelpay.common.domain;

import java.io.Serializable;

/**
 * @author wilson
 */
public interface IResultStatus extends Serializable {

    Integer CODE_SUCCESS = 1000;
    Integer CODE_FAILED = 1001;


    Integer getCode();

    String getMessage();
}
