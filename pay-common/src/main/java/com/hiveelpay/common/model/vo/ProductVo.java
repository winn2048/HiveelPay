package com.hiveelpay.common.model.vo;

import com.hiveelpay.common.enumm.BilledMethodEnum;

import java.io.Serializable;

/**
 * 支付的产品
 */
public class ProductVo implements Serializable {
    private String productId;
    private Integer productType;

    private String productName;
    private String productDescription;

    private String amount;//$
    private String billedMethod;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBilledMethod() {
        return billedMethod;
    }

    public void setBilledMethod(String billedMethod) {
        this.billedMethod = billedMethod;
    }

    @Override
    public String toString() {
        return "ProductVo{" +
                "productId='" + productId + '\'' +
                ", productType=" + productType +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", amount='" + amount + '\'' +
                ", billedMethod='" + billedMethod + '\'' +
                '}';
    }
}
