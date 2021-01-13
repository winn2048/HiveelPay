package com.hiveelpay.common.model.vo;

import java.io.Serializable;

public class BizCarVo implements Serializable {
    private String bizOrderNo;
    private String carId;
    private String zipcode;


    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


    @Override
    public String toString() {
        return "BizCarVo{" +
                "bizOrderNo='" + bizOrderNo + '\'' +
                ", carId='" + carId + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}
