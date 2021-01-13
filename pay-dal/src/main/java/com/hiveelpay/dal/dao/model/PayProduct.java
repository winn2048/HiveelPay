package com.hiveelpay.dal.dao.model;

import com.hiveelpay.common.enumm.PayProductStatusEnum;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.common.enumm.ServiceLengthUnitEnum;

import java.io.Serializable;

/**
 * 支付产品
 */
public class PayProduct extends BaseDO implements Serializable {

    private String productId;//产品ID
    private String productName;//产品名称
    private String productDescription;//产品描述
    private Long amount;// 这个产品的价格 单位是 分

    private Integer quantity = 0;//产品的数量，默认是0-无限量

    private Integer serviceLength = 0;//该产品被购买以后，可以服务多久，0-产期有效
    private ServiceLengthUnitEnum serviceLengthUnit = ServiceLengthUnitEnum.DAY;// default day

    private boolean supportAutoPay;//是否支持auto pay

    private PayProductTypeEnum productType;//支付产品类型
    private PayProductStatusEnum productStatus;//支付产品的状态

    private String btPlanId;// Braintree plan id


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public Long getAmount() {
        if (amount == null) {
            amount = 0L;
        }
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public PayProductTypeEnum getProductType() {
        return productType;
    }

    public void setProductType(PayProductTypeEnum productType) {
        this.productType = productType;
    }

    public PayProductStatusEnum getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(PayProductStatusEnum productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getServiceLength() {
        return serviceLength;
    }

    public void setServiceLength(Integer serviceLength) {
        this.serviceLength = serviceLength;
    }

    public ServiceLengthUnitEnum getServiceLengthUnit() {
        return serviceLengthUnit;
    }

    public void setServiceLengthUnit(ServiceLengthUnitEnum serviceLengthUnit) {
        this.serviceLengthUnit = serviceLengthUnit;
    }

    public String getBtPlanId() {
        return btPlanId;
    }

    public void setBtPlanId(String btPlanId) {
        this.btPlanId = btPlanId;
    }

    public boolean isSupportAutoPay() {
        return supportAutoPay;
    }

    public void setSupportAutoPay(boolean supportAutoPay) {
        this.supportAutoPay = supportAutoPay;
    }

    @Override
    public String toString() {
        return "PayProduct{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", amount=" + amount +
                ", quantity=" + quantity +
                ", serviceLength=" + serviceLength +
                ", serviceLengthUnit=" + serviceLengthUnit +
                ", productType=" + productType +
                ", productStatus=" + productStatus +
                ", id=" + id +
                ", btPlanId=" + btPlanId +
                ", supportAutoPay=" + supportAutoPay +
                ", syncStatus=" + syncStatus +
                ", createAt=" + createAt +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
