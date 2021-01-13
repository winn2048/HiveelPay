package com.hiveelpay.common.model.response;

import com.hiveelpay.common.model.vo.ProductVo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ProductListResponse implements Serializable {
    private Integer code;
    private String msg;
    private Map<String, List<ProductVo>> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, List<ProductVo>> getData() {
        return data;
    }

    public void setData(Map<String, List<ProductVo>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ProductListResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
