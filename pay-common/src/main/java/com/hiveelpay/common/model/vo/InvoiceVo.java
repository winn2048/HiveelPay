package com.hiveelpay.common.model.vo;

import java.io.Serializable;

/**
 * 发票
 */
public class InvoiceVo implements Serializable {
    private String invoiceId;
    private String mchId;
    private String dateFrom;
    private String dateTo;
    private String invoiceStatus;
    private String remark;
    private String invoiceAmount;// 发票金额
    private String settledDate;//结算日期
    private String settledAmount;//结算金额
    private String commissionAmount;//佣金金额
    private Integer totalRecords;//该发票包含的明细记录条数
    private String createAt;
    private String lastUpdateAt;

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

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getSettledDate() {
        return settledDate;
    }

    public void setSettledDate(String settledDate) {
        this.settledDate = settledDate;
    }

    public String getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(String settledAmount) {
        this.settledAmount = settledAmount;
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

    public String getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    @Override
    public String toString() {
        return "InvoiceVo{" +
                "invoiceId='" + invoiceId + '\'' +
                ", mchId='" + mchId + '\'' +
                ", dateFrom='" + dateFrom + '\'' +
                ", dateTo='" + dateTo + '\'' +
                ", invoiceStatus='" + invoiceStatus + '\'' +
                ", remark='" + remark + '\'' +
                ", invoiceAmount=" + invoiceAmount +
                ", settledDate=" + settledDate +
                ", settledAmount=" + settledAmount +
                ", commissionAmount=" + commissionAmount +
                ", createAt='" + createAt + '\'' +
                ", lastUpdateAt='" + lastUpdateAt + '\'' +
                '}';
    }
}
