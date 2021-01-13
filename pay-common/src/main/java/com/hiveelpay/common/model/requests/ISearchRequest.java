package com.hiveelpay.common.model.requests;

import java.io.Serializable;

public interface ISearchRequest extends Serializable {
    String getStartDateStr();

    String getEndDateStr();

    void setStartDateStr(String date2Str);

    void setEndDateStr(String date2Str);
}
