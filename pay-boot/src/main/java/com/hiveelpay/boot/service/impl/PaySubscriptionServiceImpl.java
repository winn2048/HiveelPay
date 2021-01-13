package com.hiveelpay.boot.service.impl;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.NotFoundException;
import com.google.common.base.Strings;
import com.hiveelpay.boot.service.PaySubscriptionService;
import com.hiveelpay.boot.service.events.CheckServiceEndEvent;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.common.util.HiveelID;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.mapper.*;
import com.hiveelpay.dal.dao.model.BizOrder;
import com.hiveelpay.dal.dao.model.PayProduct;
import com.hiveelpay.dal.dao.model.PaySubscription;
import com.hiveelpay.dal.dao.model.ServiceTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PaySubscriptionServiceImpl implements PaySubscriptionService {
    private static final MyLog _log = MyLog.getLog(PaySubscriptionServiceImpl.class);
    @Autowired
    private PaySubscriptionMapper paySubscriptionMapper;
    @Autowired
    private BizOrderMapper bizOrderMapper;
    @Autowired
    private PayProductMapper payProductMapper;
    @Autowired
    private BraintreeGateway gateway;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private ServiceTimeMapper serviceTimeMapper;

    @Autowired
    private CustomerValidServicesMapper customerValidServicesMapper;

    @Override
    public void doWentActive(WebhookNotification webhookNotification) {
        Subscription subscription = webhookNotification.getSubscription();
        String sId = subscription.getId();
        updateStatus(sId, subscription.getStatus().name());
    }

    @Override
    public void doChargedSuccessfully(WebhookNotification webhookNotification) {
        Subscription subscription = webhookNotification.getSubscription();
        String sId = subscription.getId();
        updateStatus(sId, subscription.getStatus().name());

        PaySubscription paySubscription = paySubscriptionMapper.findBySubscriptionId(sId);

        String bizOrderNo = paySubscription.getBizOrderNo();
        BizOrder bizOrder = bizOrderMapper.selectByBizOrderNo(bizOrderNo);
        //todo auto pay charged successfully
    }

    @Override
    public void doChargedUnSuccessfully(WebhookNotification webhookNotification) {
        Subscription subscription = webhookNotification.getSubscription();
        String sId = subscription.getId();
        updateStatus(sId, subscription.getStatus().name());
    }

    @Override
    public void doCanceled(WebhookNotification webhookNotification) {
        Subscription subscription = webhookNotification.getSubscription();
        String sId = subscription.getId();
        PaySubscription paySubscription = paySubscriptionMapper.findBySubscriptionId(sId);
        if (paySubscription.getStatus().equalsIgnoreCase(Subscription.Status.CANCELED.name())) {
            return;
        }
        paySubscriptionMapper.updateStatusBySubscriptionId(sId, Subscription.Status.CANCELED.name());
        bizOrderMapper.updateEmptyInvalidBizOrderNoByBizOrderNo(paySubscription.getBizOrderNo());
    }

    @Override
    public Transaction getLastTransaction(String bizOrderNo) {
        PaySubscription paySubscription = paySubscriptionMapper.findByBizOrderNo(bizOrderNo);
        if (paySubscription == null) {
            return null;
        }
        final String subscriptionId = paySubscription.getSubscriptionId();
        try {
            Subscription subscription = gateway.subscription().find(subscriptionId);
            List<Transaction> transactionList = subscription.getTransactions();
            if (transactionList == null || transactionList.isEmpty()) {
                return null;
            }

            transactionList.sort((o1, o2) -> {
                long sub = o1.getCreatedAt().getTimeInMillis() - o2.getCreatedAt().getTimeInMillis();
                if (sub > 0) {
                    return -1;
                }
                if (sub < 0) {
                    return 1;
                }
                return 0;
            });
            return transactionList.get(0); // get last one
        } catch (Exception e) {
            e.printStackTrace();
            _log.error("", e);
        }
        return null;
    }

    @Override
    public int updateStatusBySubscriptionId(String subscriptionId, String name) {
        return paySubscriptionMapper.updateStatusBySubscriptionId(subscriptionId, name);
    }

    private void fillServiceTimes(BizOrder bizOrder) {
        if (bizOrder == null) {
            return;
        }
        List<ServiceTime> list = serviceTimeMapper.queryByBizOrderNo(bizOrder.getBizOrderNo());
        if (list == null || list.isEmpty()) {
            return;
        }
        bizOrder.setServiceTimes(list);
    }

    @Transactional
    @Override
    public void doUnsubscription(String validBizOrderNo, String invalidBizOrderNo) {
        _log.info("Start to cancel auto-pay biz-order:{}", invalidBizOrderNo);

        PaySubscription paySubscription = paySubscriptionMapper.findByBizOrderNo(invalidBizOrderNo);
        if (paySubscription == null) {
            _log.info("Can't find invalid biz-order:{}", invalidBizOrderNo);
            return;
        }
        BizOrder validBizOrder = bizOrderMapper.selectByBizOrderNo(validBizOrderNo);
        fillServiceTimes(validBizOrder);
        BizOrder inValidBizOrder = bizOrderMapper.selectByBizOrderNo(invalidBizOrderNo);
        fillServiceTimes(inValidBizOrder);

        if (paySubscription.getStatus().equalsIgnoreCase(Subscription.Status.CANCELED.name())) {
            if (validBizOrder != null && (!Strings.isNullOrEmpty(validBizOrder.getInvalidBizOrderNo()))) {
                bizOrderMapper.updateEmptyInvalidBizOrderNo(validBizOrderNo);
            }
            _log.info("Biz-order:{}, has been canceled auto-pay!", invalidBizOrderNo);
            return;
        }
        final String subscriptionId = paySubscription.getSubscriptionId();

        try {
            Subscription subscription = gateway.subscription().find(subscriptionId);
            if (subscription.getStatus().equals(Subscription.Status.CANCELED)) {
                doCancelSuccess(validBizOrder, inValidBizOrder, null);
                return;
            }

            Result<Subscription> result = gateway.subscription().cancel(subscriptionId);
            if (result.isSuccess()) {
                doCancelSuccess(validBizOrder, inValidBizOrder, result);
            }
        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                paySubscriptionMapper.updateStatusBySubscriptionId(subscriptionId, "NOT_FOUND");
            }
            e.printStackTrace();
            _log.error("", e);
        }


    }

    private void doCancelSuccess(BizOrder validBizOrder, BizOrder inValidBizOrder, Result<Subscription> result) {
        if (result != null) {
            paySubscriptionMapper.updateStatusBySubscriptionId(result.getTarget().getId(), result.getTarget().getStatus().name());
            _log.info("Successfully canceled auto-pay biz-order:{}", result.getTarget().getStatus().name());
        }
        bizOrderMapper.updateEmptyInvalidBizOrderNo(validBizOrder.getBizOrderNo());

        PayProduct validProduct = payProductMapper.findByProductId(validBizOrder.getProductId());
        PayProduct inValidProduct = payProductMapper.findByProductId(inValidBizOrder.getProductId());

        if (validProduct.getProductType().equals(PayProductTypeEnum.MEMBERSHIP) && inValidProduct.getProductType().equals(PayProductTypeEnum.MEMBERSHIP)) {
            if (validProduct.getAmount() > inValidProduct.getAmount()) {// membership from low level to high level
                final Date now = new Date();
                serviceTimeMapper.updateServiceEndTime(inValidBizOrder.getBizOrderNo(), now);
                customerValidServicesMapper.updateServiceEndTime(inValidBizOrder.getBizOrderNo(), now);

//                bizOrderMapper.updateServiceEndTime(inValidBizOrder.getBizOrderNo());
                publisher.publishEvent(new CheckServiceEndEvent(HiveelID.getInstance().getRandomId()));
            }
        }
    }

    @Override
    public List<PaySubscription> getAfter10MinutesSubReqs(String statusString, int times) {
        return paySubscriptionMapper.getAfter10MinutesSubReqs(statusString, times);
    }

    private void updateStatus(String sid, String statusName) {
        paySubscriptionMapper.updateStatusBySubscriptionId(sid, statusName);
    }
}
