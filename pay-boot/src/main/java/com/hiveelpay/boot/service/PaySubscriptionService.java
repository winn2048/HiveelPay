package com.hiveelpay.boot.service;

import com.braintreegateway.Transaction;
import com.braintreegateway.WebhookNotification;
import com.hiveelpay.dal.dao.model.PaySubscription;

import java.util.List;

public interface PaySubscriptionService {
    void doWentActive(WebhookNotification webhookNotification);

    void doChargedSuccessfully(WebhookNotification webhookNotification);

    void doChargedUnSuccessfully(WebhookNotification webhookNotification);

    void doCanceled(WebhookNotification webhookNotification);

    Transaction getLastTransaction(String bizOrderNo);

    int updateStatusBySubscriptionId(String subscriptionId, String name);

    void doUnsubscription(String validBizOrderNo, String invalidBizOrderNo);

    List<PaySubscription> getAfter10MinutesSubReqs(String statusString, int times);
}
