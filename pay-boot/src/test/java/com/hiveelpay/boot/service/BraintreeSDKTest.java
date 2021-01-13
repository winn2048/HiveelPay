package com.hiveelpay.boot.service;

import com.braintreegateway.*;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.hiveelpay.boot.PayBootAppliaction;
import com.hiveelpay.common.util.HiveelID;
import com.hiveelpay.common.util.MyLog;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayBootAppliaction.class)
public class BraintreeSDKTest {
    private static final MyLog _log = MyLog.getLog(BraintreeSDKTest.class);
    @Autowired
    private BraintreeGateway gateway;


    @Test
    public void customerCreateTest() {
        String customerId = HiveelID.getInstance().getRandomId("T");
        CustomerRequest request = new CustomerRequest()
                .id(customerId)
                .firstName("Mark")
                .lastName("Jones")
                .customerId(customerId)
                .deviceData("WEB")
                .creditCard()
                .paymentMethodNonce("tokencc_bf_vxhc6h_6ppfss_m8cpgd_627373_3d7")
                .customerId(customerId)

                .cardholderName("Jones Mark")
                .number("4111111111111111")
                .cvv("100")
                .expirationDate("02/19")
                .billingAddress()
                .firstName("Mark")
                .lastName("Jones")
                .streetAddress("1820 Jellick AVE")
                .locality("Rowland Heights")
                .region("CA")
                .postalCode("91748")
                .done()

                .done();
        Result<Customer> result = gateway.customer().create(request);
        if (result.isSuccess()) {
            _log.info("Customer create  success!");
        } else {
            _log.error("Customer create  error!");
        }
    }

    @Test
    public void creditCardVerify() {
        CreditCardVerificationRequest request = new CreditCardVerificationRequest()
                .creditCard()
                .cardholderName("Wilson Wang")
                .number("4111111111111111")
                .cvv("100")
                .expirationDate("02/19")
                .done();
        Result<CreditCardVerification> result = gateway.creditCardVerification().create(request);
        if (result.isSuccess()) {
            _log.info("CreditCard verification success!");
            _log.info("result:{}", result);
        } else {
            _log.info("CreditCard verification failed!");
        }
//        gateway.creditCardVerification().create();
    }

    @Test
    public void creditCardCreate() {
        CreditCardRequest request = new CreditCardRequest()
                .customerId("254348803")
                .cvv("100")
                .number("4111111111111111")
                .expirationDate("02/19");
        Result<CreditCard> result = gateway.creditCard().create(request);
        if (result.isSuccess()) {
            _log.info("CreditCard create success!");
        }
    }


    @Test
    public void subscription() {
//        Result<PaymentMethodNonce> paymentMethodNonceResult = gateway.paymentMethodNonce().create(HiveelID.getInstance().getRandomId());

        String token = HiveelID.getInstance().getRandomId();


        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest()
                .customerId("254348803")
                .billingAddressId("qw")
                .token(token)
                .paymentMethodNonce("tokencc_bf_wh9nkz_khxfsr_kvxjqz_tmkb66_t46")//Nonce create by front end.
//                .number("4111111111111111")
//                .token(token)
//                .cvv("100")
                ;
        Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(paymentMethodRequest);
        _log.info("---------------------------{}", paymentMethodResult.getMessage());
        _log.info("____________________________{}", new Gson().toJson(paymentMethodResult.getTarget()));

        Assert.assertTrue(paymentMethodResult.isSuccess());
        if (paymentMethodResult.isSuccess()) {

            String paymentMethodToken = paymentMethodResult.getTarget().getToken();
            SubscriptionRequest request = new SubscriptionRequest()
                    .planId("27ym")
                    .paymentMethodToken(paymentMethodToken)
                    .id(HiveelID.getInstance().getRandomId("P"))
                    .firstBillingDate(Calendar.getInstance())
                    .price(new BigDecimal(10));
            Result<Subscription> result = gateway.subscription().create(request);

            _log.info("---------------------------{}", result.getMessage());
            if (result.isSuccess()) {
            }
            Assert.assertTrue(result.isSuccess());
        }
    }

    @Test
    public void subscriptionFind() {
        Subscription subscription = gateway.subscription().find("Sd2f5c93d5d1e4c62b5c93d5d1edc62ea");
        List<SubscriptionStatusEvent> subscriptionStatusEvents = subscription.getStatusHistory();
        subscriptionStatusEvents.forEach(i -> {
            _log.info("______________{}", new Gson().toJson(i));
        });
    }


    @Test
    public void plans() {
        List<Plan> plans = gateway.plan().all();
        if (!plans.isEmpty()) {
            plans.forEach(i -> {
                _log.info("_________{}", new Gson().toJson(i));
            });
        }

    }

    @Test
    public void findByPlandIdTest() {
        Subscription subscription = gateway.subscription().find("Sbf596e423d8944c9996e423d8974c922");
        _log.info("{}", subscription);
    }


    @Test
    public void webhookNotification() {
        HashMap<String, String> sampleNotification = gateway.webhookTesting().sampleNotification(WebhookNotification.Kind.SUBSCRIPTION_WENT_ACTIVE, "S4338062d99304431b8062d9930043147");
        WebhookNotification webhookNotification = gateway.webhookNotification().parse(
                sampleNotification.get("bt_signature"),
                sampleNotification.get("bt_payload")
        );
        String id = webhookNotification.getSubscription().getId();

    }


    @Test
    public void findTrans() {
        Transaction transaction = gateway.transaction().find("bhpv76py");
        _log.info("transaction:{}", new Gson().toJson(transaction));
    }


    @Test
    public void calcenSubscription() {
        Subscription subscription = gateway.subscription().find("S86a55ace915c4b18a55ace915cdb183ca");
        _log.info("subscription:{}", subscription);
        Result<Subscription> result = gateway.subscription().cancel("S86a55ace915c4b18a55ace915cdb183c");
        _log.info("result:{}", result);
    }
}
