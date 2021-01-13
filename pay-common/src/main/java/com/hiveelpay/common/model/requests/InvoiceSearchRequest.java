package com.hiveelpay.common.model.requests;

import com.hiveelpay.common.enumm.InvoiceStatusEnum;

import java.io.Serializable;

public class InvoiceSearchRequest implements Serializable {
    private String mchId;
    private InvoiceStatusEnum invoiceStatus;

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public void setInvoiceStatus(InvoiceStatusEnum invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public InvoiceStatusEnum getInvoiceStatus() {
        return invoiceStatus;
    }

    @Override
    public String toString() {
        return "InvoiceSearchRequest{" +
                "mchId='" + mchId + '\'' +
                " , invoiceStatus='" + invoiceStatus + '\'' +
                '}';
    }
}
