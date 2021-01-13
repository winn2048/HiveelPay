package com.hiveelpay.boot.jobs;

import com.braintreegateway.BraintreeGateway;
import com.hiveelpay.boot.service.BizOrderService;
import com.hiveelpay.boot.service.PaySubscriptionService;
import com.hiveelpay.boot.service.events.SubscriptionRequestEvent;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.mapper.CreditCardMapper;
import com.hiveelpay.dal.dao.model.BizOrder;
import com.hiveelpay.dal.dao.model.PaySubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 支付autoPay Jobs
 */
@Component
public class AutoPayJobs {
    private static final MyLog _log = MyLog.getLog(AutoPayJobs.class);

    @Autowired
    private CreditCardMapper creditCardMapper;
    @Autowired
    private BraintreeGateway gateway;
    @Autowired
    private PaySubscriptionService paySubscriptionServiceImpl;
    @Autowired
    private BizOrderService bizOrderServiceImpl;
    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * 处理 提交超过10分钟还没 发起订阅的请求
     * Pay Subscription 'NEW'状态的处理
     */
    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 5)//5分钟
    public void processPaySubscriptionNew() {
        List<PaySubscription> paySubscriptionList = paySubscriptionServiceImpl.getAfter10MinutesSubReqs("NEW", 6);
        if (paySubscriptionList == null || paySubscriptionList.isEmpty()) {
            _log.info("There is no new subscription request.");
            return;
        }
        paySubscriptionList.forEach(i -> publisher.publishEvent(new SubscriptionRequestEvent(i)));
    }

    /**
     * 支付时取消订阅失败，这里补上取消订阅操作
     */
    @Async
    @Scheduled(fixedDelay = 1000 * 60 * 5)//
    public void checkUnsubscriptionBizOrders() {
        List<BizOrder> list = bizOrderServiceImpl.findHasInvalidBizOrderNos();
        list.forEach(i -> paySubscriptionServiceImpl.doUnsubscription(i.getBizOrderNo(), i.getInvalidBizOrderNo()));
    }
}
