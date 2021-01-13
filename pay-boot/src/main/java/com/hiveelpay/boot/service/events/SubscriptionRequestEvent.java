package com.hiveelpay.boot.service.events;

import org.springframework.context.ApplicationEvent;
import com.hiveelpay.dal.dao.model.PaySubscription;

import java.io.Serializable;

public class SubscriptionRequestEvent extends ApplicationEvent implements Serializable {
    private PaySubscription paySubscription;

    public SubscriptionRequestEvent(PaySubscription paySubscription) {
        super(paySubscription);
        this.paySubscription = paySubscription;
    }

    public PaySubscription getPaySubscription() {
        return paySubscription;
    }

    @Override
    public String toString() {
        return "SubscriptionRequestEvent{" +
                "paySubscription=" + paySubscription +
                '}';
    }
}
