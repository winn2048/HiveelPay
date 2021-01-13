package com.hiveelpay.common.model.response;

import com.hiveelpay.common.model.vo.ProductVo;

import java.io.Serializable;

public class ProductItemResponse implements Serializable {
    private Integer code;
    private String msg;
    private ProductVo data;

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

    public ProductVo getData() {
        return data;
    }

    public void setData(ProductVo data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ProductItemResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
