package com.hiveelpay.boot.service.events;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Subscription;
import com.braintreegateway.SubscriptionRequest;
import com.google.common.base.Strings;
import com.hiveelpay.common.util.AmountUtil;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.mapper.BizOrderMapper;
import com.hiveelpay.dal.dao.mapper.PayProductMapper;
import com.hiveelpay.dal.dao.mapper.PaySubscriptionMapper;
import com.hiveelpay.dal.dao.model.BizOrder;
import com.hiveelpay.dal.dao.model.PayProduct;
import com.hiveelpay.dal.dao.model.PaySubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;

@Component
public class SubscriptionRequestEventListener {
    private static final MyLog _log = MyLog.getLog(SubscriptionRequestEventListener.class);

    @Autowired
    private BraintreeGateway gateway;
    @Autowired
    private PaySubscriptionMapper paySubscriptionMapper;
    @Autowired
    private BizOrderMapper bizOrderMapper;
    @Autowired
    private PayProductMapper payProductMapper;

    /**
     * 发起支付订阅  after  H_CHARGE_SUBSCRIPTION
     *
     * @param subscriptionRequestEvent
     */
    @EventListener
    public void listen(SubscriptionRequestEvent subscriptionRequestEvent) {
        _log.info("SubscriptionRequest:{}", subscriptionRequestEvent);

        PaySubscription paySubscription = subscriptionRequestEvent.getPaySubscription();

        BizOrder bizOrder = bizOrderMapper.selectByBizOrderNo(paySubscription.getBizOrderNo());
        PayProduct payProduct = payProductMapper.findByProductId(bizOrder.getProductId());

        /**
         * subscription just use the Pay-Product price.
         */
        BigDecimal decimalAmount = new BigDecimal(AmountUtil.convertCent2Dollar(String.valueOf(payProduct.getAmount())));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bizOrder.getFirstBillDate());
//        calendar.add(Calendar.DATE, -1);

        SubscriptionRequest request = new SubscriptionRequest()
                .id(paySubscription.getSubscriptionId())
                .planId(paySubscription.getPlanId())
                .paymentMethodToken(paySubscription.getPaymentMethodId())
                .firstBillingDate(calendar)
                .neverExpires(true)
                .price(decimalAmount);
        Result<Subscription> result = gateway.subscription().create(request);
        if (result.isSuccess()) {
            _log.info("SubscriptionRequest:successed! target:{}", result.getTarget());
            paySubscriptionMapper.updateStatusBySubscriptionId(paySubscription.getSubscriptionId(), result.getTarget().getStatus().name());
        } else {
            StringBuilder sb = new StringBuilder(result.getMessage());
            result.getErrors().getAllValidationErrors().forEach(i -> sb.append(i.getCode()).append(":").append(i.getMessage()).append("\n").append(","));
            paySubscription.setErrorMsg(sb.toString());

            _log.error("Post subscription error!, paySubscription:{}", paySubscription);
            String errMsg = Strings.nullToEmpty(sb.toString());
            if (errMsg.length() >= 600) {
                errMsg = errMsg.substring(0, 599);
            }
            paySubscriptionMapper.updateErrorMsg(paySubscription.getSubscriptionId(), result.getTarget() == null ? null : result.getTarget().getStatus().name(), errMsg);
        }

    }
}
