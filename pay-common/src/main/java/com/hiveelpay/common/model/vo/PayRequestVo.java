package com.hiveelpay.common.model.vo;

import com.hiveelpay.common.enumm.*;

import java.io.Serializable;

/**
 * 支付请求参数
 */
public class PayRequestVo implements Serializable {
    private String userId;
    private String productId;
    private AccountType accountType = AccountType.CUSTOMER;//默认customer
    private PaymentMethodTypeEnum payType = PaymentMethodTypeEnum.CREDITCARD;// 默认信用卡
    private BilledMethodEnum billedMethod = BilledMethodEnum.MONTHLY;//默认月付
    /**
     * 关联文档信息
     */

    private String docId;//
    private String docZipcode;

    private String payMethodId;//绑定的支付方式的ID
    /**
     * 账单信息
     */
    private String firstName;
    private String lastName;

    private String addr;
    private String addrSuite;
    private String city;
    private String state;
    private String zipcode;
    private AddressType addressType = AddressType.billing;

    private CurrencyEnum currency;


    /*
      支付信息
     */
    private String clientToken;
    private String nonce;
    private String mchId;//收款商户ID
    private String channelId;//支付通道ID

    private String totalAmount;//总金额，$美刀

    private String invalidBizOrderNo;//需要失效的业务单号

    private String serviceTimes;//产品服务时间,格式：MMddyyyyHHmm
    private String remark;//备注- saved to biz-order
    private String firstBillDate;// 账单日
//    private String carIds;// 多个车ID用英文','分隔

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BilledMethodEnum getBilledMethod() {
        return billedMethod;
    }

    public void setBilledMethod(BilledMethodEnum billedMethod) {
        this.billedMethod = billedMethod;
    }

    public String getPayMethodId() {
        return payMethodId;
    }

    public void setPayMethodId(String payMethodId) {
        this.payMethodId = payMethodId;
    }

    public void setInvalidBizOrderNo(String invalidBizOrderNo) {
        this.invalidBizOrderNo = invalidBizOrderNo;
    }

    public String getInvalidBizOrderNo() {
        return invalidBizOrderNo;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAddrSuite() {
        return addrSuite;
    }

    public void setAddrSuite(String addrSuite) {
        this.addrSuite = addrSuite;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public PaymentMethodTypeEnum getPayType() {
        return payType;
    }

    public void setPayType(PaymentMethodTypeEnum payType) {
        this.payType = payType;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
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

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public String getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(String serviceTimes) {
        this.serviceTimes = serviceTimes;
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


    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }


    public String getFirstBillDate() {
        return firstBillDate;
    }

    public void setFirstBillDate(String firstBillDate) {
        this.firstBillDate = firstBillDate;
    }


    public String getDocZipcode() {
        return docZipcode;
    }

    public void setDocZipcode(String docZipcode) {
        this.docZipcode = docZipcode;
    }

    @Override
    public String toString() {
        return "PayRequestVo{" +
                "userId='" + userId + '\'' +
                ", productId='" + productId + '\'' +
                ", accountType=" + accountType +
                ", payType=" + payType +
                ", billedMethod=" + billedMethod +
                ", docId='" + docId + '\'' +
                ", payMethodId='" + payMethodId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", addr='" + addr + '\'' +
                ", addrSuite='" + addrSuite + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", docZipcode='" + docZipcode + '\'' +
                ", addressType=" + addressType +
                ", currency=" + currency +
                ", clientToken='" + clientToken + '\'' +
                ", nonce='" + nonce + '\'' +
                ", mchId='" + mchId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", invalidBizOrderNo='" + invalidBizOrderNo + '\'' +
                ", serviceTimes='" + serviceTimes + '\'' +
                ", remark='" + remark + '\'' +
                ", firstBillDate='" + firstBillDate + '\'' +
                '}';
    }
}
