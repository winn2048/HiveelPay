package com.hiveelpay.boot.service.events;

import org.springframework.context.ApplicationEvent;
import com.hiveelpay.dal.dao.model.PaySubscription;

import java.io.Serializable;

public class SubscriptionDoneEvent extends ApplicationEvent implements Serializable {
    private PaySubscription paySubscription;
    private boolean subscriptionReq;// subscription result, true-success, false-failed

    public SubscriptionDoneEvent(PaySubscription paySubscription, boolean subscriptionReq) {
        super(paySubscription);
        this.paySubscription = paySubscription;
        this.subscriptionReq = subscriptionReq;
    }

    public PaySubscription getPaySubscription() {
        return paySubscription;
    }

    public boolean isSubscriptionReq() {
        return subscriptionReq;
    }

    @Override
    public String toString() {
        return "SubscriptionRequestEvent{" +
                "paySubscription=" + paySubscription +
                "subscriptionReq=" + subscriptionReq +
                '}';
    }
}
