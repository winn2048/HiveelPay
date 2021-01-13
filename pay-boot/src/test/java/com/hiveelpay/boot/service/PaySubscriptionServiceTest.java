package com.hiveelpay.boot.service;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Transaction;
import com.braintreegateway.WebhookNotification;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.hiveelpay.boot.PayBootAppliaction;
import com.hiveelpay.common.util.MyLog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayBootAppliaction.class)
public class PaySubscriptionServiceTest {

    private static final MyLog _log = MyLog.getLog(PaySubscriptionServiceTest.class);
    @Autowired
    private BraintreeGateway gateway;

    @Autowired
    private PaySubscriptionService paySubscriptionServiceImpl;


    @Test
    public void subscriptionWentActiveTest() {
        HashMap<String, String> notificationMap = gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_ACTIVE, "S4338062d99304431b8062d9930043147");
        WebhookNotification webhookNotification = gateway.webhookNotification().parse(
                notificationMap.get("bt_signature"),
                notificationMap.get("bt_payload")
        );
        paySubscriptionServiceImpl.doWentActive(webhookNotification);
    }

    @Test
    public void subscriptionChargedSuccessfully() {
        HashMap<String, String> notificationMap = gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_CHARGED_SUCCESSFULLY, "S4338062d99304431b8062d9930043147");
        WebhookNotification webhookNotification = gateway.webhookNotification().parse(
                notificationMap.get("bt_signature"),
                notificationMap.get("bt_payload")
        );
        paySubscriptionServiceImpl.doChargedSuccessfully(webhookNotification);
    }

    @Test
    public void subscriptionChargedUnSuccessfully() {
        HashMap<String, String> notificationMap = gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_CHARGED_UNSUCCESSFULLY, "S4338062d99304431b8062d9930043147");
        WebhookNotification webhookNotification = gateway.webhookNotification().parse(
                notificationMap.get("bt_signature"),
                notificationMap.get("bt_payload")
        );
        paySubscriptionServiceImpl.doChargedUnSuccessfully(webhookNotification);
    }

    @Test
    public void subscriptionCanceled() {
        HashMap<String, String> notificationMap = gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_CANCELED, "S4338062d99304431b8062d9930043147");
        WebhookNotification webhookNotification = gateway.webhookNotification().parse(
                notificationMap.get("bt_signature"),
                notificationMap.get("bt_payload")
        );
        paySubscriptionServiceImpl.doCanceled(webhookNotification);
    }


    @Test
    public void findLastTransaction() {
        Transaction transaction = paySubscriptionServiceImpl.getLastTransaction("Beaa400793e2c4479a400793e2c74794a");
        _log.info("{}", transaction);

        List<Calendar> list = Lists.newArrayList();

        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(1l);
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(2l);

        list.add(c1);
        list.add(c2);

        list.sort((o1, o2) -> {
            if (o1.getTimeInMillis() - o2.getTimeInMillis() > 0) {
                return -1;
            }
            if (o1.getTimeInMillis() - o2.getTimeInMillis() < 0) {
                return 1;
            }
            return 0;
        });
        _log.info("--------list.sorted:{}", list);
    }

}
