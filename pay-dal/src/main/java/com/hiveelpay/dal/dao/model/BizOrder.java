package com.hiveelpay.dal.dao.model;

import com.google.common.collect.Lists;
import com.hiveelpay.common.enumm.BizOrderStatus;
import com.hiveelpay.common.enumm.CurrencyEnum;
import com.hiveelpay.common.enumm.ServiceLengthUnitEnum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 客户订单
 */
public class BizOrder extends BaseDO implements Serializable {
    private String customerId;//客户ID
    private String bizOrderNo;//单号
    /**
     * 产品快照
     */
    private String productId;//产品ID
    private String productName;//产品名称
    private Long amount;//产品 价格 单位是分
    private String productType;//类型
    private Integer serviceLength;//该产品被购买以后，可以服务多久，0-产期有效
    private ServiceLengthUnitEnum serviceLengthUnit;// default day

    private BizOrderStatus orderStatus;//订单状态

    private List<ServiceTime> serviceTimes;//bizOrder的服务时间
    private Date firstBillDate;//第一个账单日
    private Long payAmount;//真正支付的价格
    private Date paySuccessTime;//支付成功时间

    private Long commissionAmount;//佣金金额

    private String mchId;//收款商户ID
    private String channelId;//支付通道ID

    private CurrencyEnum currency;//币种

    private String remark;// 备注
    private String invalidBizOrderNo;//需要失效的业务订单号

    private String docId;

    private List<PayOrder> payOrders = Lists.newArrayList();

    private PaySubscription paySubscription;

    private AppointmentDoc appointment;
    private BizCar bizCar;


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
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

    public ServiceLengthUnitEnum getServiceLengthUnit() {
        return serviceLengthUnit;
    }

    public void setServiceLengthUnit(ServiceLengthUnitEnum serviceLengthUnit) {
        this.serviceLengthUnit = serviceLengthUnit;
    }

    public BizOrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(BizOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getPaySuccessTime() {
        return paySuccessTime;
    }

    public void setPaySuccessTime(Date paySuccessTime) {
        this.paySuccessTime = paySuccessTime;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
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

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyEnum currency) {
        this.currency = currency;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInvalidBizOrderNo() {
        return invalidBizOrderNo;
    }

    public void setInvalidBizOrderNo(String invalidBizOrderNo) {
        this.invalidBizOrderNo = invalidBizOrderNo;
    }

    public List<PayOrder> getPayOrders() {
        return payOrders;
    }

    public void setPayOrders(List<PayOrder> payOrders) {
        this.payOrders = payOrders;
    }

    public PaySubscription getPaySubscription() {
        return paySubscription;
    }

    public void setPaySubscription(PaySubscription paySubscription) {
        this.paySubscription = paySubscription;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public AppointmentDoc getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentDoc appointment) {
        this.appointment = appointment;
    }


    public List<ServiceTime> getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(List<ServiceTime> serviceTimes) {
        this.serviceTimes = serviceTimes;
    }

    public Date getFirstBillDate() {
        return firstBillDate;
    }

    public void setFirstBillDate(Date firstBillDate) {
        this.firstBillDate = firstBillDate;
    }

    public BizCar getBizCar() {
        return bizCar;
    }

    public void setBizCar(BizCar bizCar) {
        this.bizCar = bizCar;
    }

    public Long getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Long commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    @Override
    public String toString() {
        return "BizOrder{" +
                "customerId='" + customerId + '\'' +
                ", bizOrderNo='" + bizOrderNo + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                ", productType='" + productType + '\'' +
                ", serviceLength=" + serviceLength +
                ", serviceLengthUnit=" + serviceLengthUnit +
                ", orderStatus=" + orderStatus +
                ", serviceTimes=" + serviceTimes +
                ", firstBillDate=" + firstBillDate +
                ", payAmount=" + payAmount +
                ", commissionAmount=" + commissionAmount +
                ", paySuccessTime=" + paySuccessTime +
                ", mchId='" + mchId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", currency=" + currency +
                ", remark='" + remark + '\'' +
                ", invalidBizOrderNo='" + invalidBizOrderNo + '\'' +
                ", docId='" + docId + '\'' +
                ", payOrders=" + payOrders +
                ", paySubscription=" + paySubscription +
                ", appointment=" + appointment +
                ", bizCar=" + bizCar +
                ", id=" + id +
                ", createAt=" + createAt +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
