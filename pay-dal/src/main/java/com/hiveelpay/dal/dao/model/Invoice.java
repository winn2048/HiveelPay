package com.hiveelpay.dal.dao.model;

import com.hiveelpay.common.enumm.InvoiceStatusEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 发票
 */
public class Invoice extends BaseDO implements Serializable {
    private String invoiceId;
    private String mchId;

    private Date dateFrom;
    private Date dateTo;


    private InvoiceStatusEnum invoiceStatus;

    private String remark;

    private Long invoiceAmount;// 发票金额

    private Date settledDate;//结算日期
    private Long settledAmount;//结算金额

    private Integer totalRecords;//该发票包含的明细记录条数
    private Long commissionAmount;//佣金价格，如果 我们要支付出去，那么这个值是负的


    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public InvoiceStatusEnum getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatusEnum invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getSettledDate() {
        return settledDate;
    }

    public void setSettledDate(Date settledDate) {
        this.settledDate = settledDate;
    }

    public Long getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Long invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Long getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(Long settledAmount) {
        this.settledAmount = settledAmount;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Long getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Long commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId='" + invoiceId + '\'' +
                ", mchId='" + mchId + '\'' +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", settledDate=" + settledDate +
                ", invoiceStatus=" + invoiceStatus +
                ", remark='" + remark + '\'' +
                ", invoiceAmount=" + invoiceAmount +
                ", settledAmount=" + settledAmount +
                ", commissionAmount=" + commissionAmount +
                ", id=" + id +
                ", createAt=" + createAt +
                ", lastUpdateAt=" + lastUpdateAt +
                '}';
    }
}
