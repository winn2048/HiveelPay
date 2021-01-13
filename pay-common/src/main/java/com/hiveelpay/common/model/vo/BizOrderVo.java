package com.hiveelpay.common.model.vo;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class BizOrderVo implements Serializable {
    private String customerId;//客户ID
    private String bizOrderNo;//单号

    /**
     * 产品快照
     */
    private String productId;//产品ID
    private String productName;//产品名称
    private String amount;//产品 价格 单位是$
    private String productType;//类型
    private Integer serviceLength;//该产品被购买以后，可以服务多久，0-产期有效
    private Integer serviceLengthUnit;// default day

    private String orderStatus;//订单状态

    private List<ServiceTimeVo> serviceTimeList;
    private String firstBillDate;//
    private String payAmount;//真正支付的价格
    private String paySuccessTime;//支付成功时间

    private String mchId;//收款商户ID
    private String channelId;//支付通道ID
    private String createAt;
    private String lastUpdateAt;

    private List<PayOrderVo> payOrderList = Collections.emptyList();

    private PaySubscriptionVo paySubscription;

    private AppointmentDocVo appointment;
    private BizCarVo bizCar;

    private String commissionAmount;// 佣金金额


    public AppointmentDocVo getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentDocVo appointment) {
        this.appointment = appointment;
    }

    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo;
    }

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getServiceLength() {
        return serviceLength;
    }

    public void setServiceLength(Integer serviceLength) {
        this.serviceLength = serviceLength;
    }

    public Integer getServiceLengthUnit() {
        return serviceLengthUnit;
    }

    public void setServiceLengthUnit(Integer serviceLengthUnit) {
        this.serviceLengthUnit = serviceLengthUnit;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getFirstBillDate() {
        return firstBillDate;
    }

    public void setFirstBillDate(String firstBillDate) {
        this.firstBillDate = firstBillDate;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPaySuccessTime() {
        return paySuccessTime;
    }

    public void setPaySuccessTime(String paySuccessTime) {
        this.paySuccessTime = paySuccessTime;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(String lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    public List<PayOrderVo> getPayOrderList() {
        return payOrderList;
    }

    public void setPayOrderList(List<PayOrderVo> payOrderList) {
        this.payOrderList = payOrderList;
    }

    public PaySubscriptionVo getPaySubscription() {
        return paySubscription;
    }

    public void setPaySubscription(PaySubscriptionVo paySubscription) {
        this.paySubscription = paySubscription;
    }

    public List<ServiceTimeVo> getServiceTimeList() {
        return serviceTimeList;
    }

    public void setServiceTimeList(List<ServiceTimeVo> serviceTimeList) {
        this.serviceTimeList = serviceTimeList;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BizCarVo getBizCar() {
        return bizCar;
    }

    public void setBizCar(BizCarVo bizCar) {
        this.bizCar = bizCar;
    }

    public String getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    @Override
    public String toString() {
        return "BizOrderVo{" +
                "bizOrderNo='" + bizOrderNo + '\'' +
                ", productId='" + productId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", productName='" + productName + '\'' +
                ", amount='" + amount + '\'' +
                ", productType='" + productType + '\'' +
                ", serviceLength=" + serviceLength +
                ", serviceLengthUnit=" + serviceLengthUnit +
                ", orderStatus=" + orderStatus +
                ", payAmount='" + payAmount + '\'' +
                ", paySuccessTime='" + paySuccessTime + '\'' +
                ", mchId='" + mchId + '\'' +
                ", paySubscription='" + paySubscription + '\'' +
                ", channelId='" + channelId + '\'' +
                ", createAt='" + createAt + '\'' +
                ", serviceTimeList='" + serviceTimeList + '\'' +
                ", firstBillDate='" + firstBillDate + '\'' +
                ", commissionAmount='" + commissionAmount + '\'' +
                ", appointment='" + appointment + '\'' +
                ", lastUpdateAt='" + lastUpdateAt + '\'' +
                '}';
    }
}
