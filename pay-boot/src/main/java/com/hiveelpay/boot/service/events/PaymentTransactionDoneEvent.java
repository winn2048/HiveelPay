package com.hiveelpay.boot.service.events;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * 支付完成事件
 */
public class PaymentTransactionDoneEvent extends ApplicationEvent implements Serializable {

    private String customerId;
    private String payOrderId;
    private String bizOrderId;
    private String paymentMethodId;
    private STATUS status;


    public PaymentTransactionDoneEvent(String customerId, String payOrderId, String bizOrderId, String paymentMethodId, STATUS status) {
        super(customerId + payOrderId + bizOrderId + paymentMethodId + status.name());
        this.customerId = customerId;
        this.payOrderId = payOrderId;
        this.bizOrderId = bizOrderId;
        this.paymentMethodId = paymentMethodId;
        this.status = status;
    }

    public static enum STATUS {
        SUCCESS, FAILED, FAILED_SAVED_TRANSACTION
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public String getBizOrderId() {
        return bizOrderId;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public STATUS getStatus() {
        return status;
    }

    public String getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "PaymentTransactionDoneEvent{" +
                "payOrderId='" + payOrderId + '\'' +
                ", bizOrderId='" + bizOrderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", paymentMethodId='" + paymentMethodId + '\'' +
                ", status=" + status +
                '}';
    }
}
