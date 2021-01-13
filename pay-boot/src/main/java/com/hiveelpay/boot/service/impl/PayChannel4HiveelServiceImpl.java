package com.hiveelpay.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.braintreegateway.*;
import com.google.common.base.Strings;
import com.hiveelpay.boot.service.BaseService;
import com.hiveelpay.boot.service.IPayChannel4HiveelService;
import com.hiveelpay.boot.service.PaySubscriptionService;
import com.hiveelpay.boot.service.channel.hiveel.HiveelConfig;
import com.hiveelpay.boot.service.events.PaymentTransactionDoneEvent;
import com.hiveelpay.boot.service.events.SubscriptionDoneEvent;
import com.hiveelpay.boot.service.events.SubscriptionRequestEvent;
import com.hiveelpay.common.constant.PayConstant;
import com.hiveelpay.common.domain.BaseParam;
import com.hiveelpay.common.enumm.RetEnum;
import com.hiveelpay.common.util.*;
import com.hiveelpay.dal.dao.mapper.BizOrderMapper;
import com.hiveelpay.dal.dao.mapper.PayProductMapper;
import com.hiveelpay.dal.dao.model.BizOrder;
import com.hiveelpay.dal.dao.model.PayOrder;
import com.hiveelpay.dal.dao.model.PayProduct;
import com.hiveelpay.dal.dao.model.PaySubscription;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

/**
 * @author: wilson
 * @date: 17/9/10
 * @description:
 */
@Service
public class PayChannel4HiveelServiceImpl extends BaseService implements IPayChannel4HiveelService {

    private static final MyLog _log = MyLog.getLog(PayChannel4AliServiceImpl.class);
    @Autowired
    private BraintreeGateway gateway;
    @Autowired
    private BizOrderMapper bizOrderMapper;

    @Autowired
    private HiveelConfig hiveelConfig;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private PaySubscriptionService paySubscriptionServiceImpl;
    @Autowired
    private PayProductMapper payProductMapper;


    @Override
    public Map doHiveelPayWapReq(String jsonParam) {
        return null;
    }

    /**
     * 交易 并 永久订阅
     *
     * @param jsonParam
     * @return
     */
    @Override
    public Map doHiveelPayChargeAndSubscriptionReq(String jsonParam) {
        String logPrefix = "【H_CHARGE_SUBSCRIPTION 信用卡通道 支付下单】";
        BaseParam baseParam = JsonUtil.getObjectFromJson(jsonParam, BaseParam.class);
        Map<String, Object> bizParamMap = baseParam.getBizParamMap();
        if (ObjectValidUtil.isInvalid(bizParamMap)) {
            _log.warn("{}失败, {}. jsonParam={}", logPrefix, RetEnum.RET_PARAM_NOT_FOUND.getMessage(), jsonParam);
            return RpcUtil.createFailResult(baseParam, RetEnum.RET_PARAM_NOT_FOUND);
        }
        JSONObject payOrderObj = baseParam.isNullValue("payOrder") ? null : JSONObject.parseObject(bizParamMap.get("payOrder").toString());
        PayOrder payOrder = BeanConvertUtils.map2Bean(payOrderObj, PayOrder.class);
        if (ObjectValidUtil.isInvalid(payOrder)) {
            _log.warn("{}失败, {}. jsonParam={}", logPrefix, RetEnum.RET_PARAM_INVALID.getMessage(), jsonParam);
            return RpcUtil.createFailResult(baseParam, RetEnum.RET_PARAM_INVALID);
        }


        String payOrderId = payOrder.getPayOrderId();
        Long amount = payOrder.getAmount();

//        PayChannel payChannel = super.baseSelectPayChannel(mchId, channelId);
        /**
         * BrainTree not support 0 amount, $0 auto success!
         */
        if (amount <= 0) {
            amount = 0L;
        }


        // 获取objParams参数
        String objParams = payOrder.getExtra();
        String payProductId = null;
        String paymentMethodId = null;
        String btPlanId = null;
        String nonce = null;
        String cTargetId = null;
        String addressTargetId = null;
        String invalidBizOrderNo = null;

        if (StringUtils.isNotEmpty(objParams)) {
            try {
                JSONObject objParamsJson = JSON.parseObject(objParams);

                payProductId = Optional.ofNullable(objParamsJson.getString("payProductId")).orElse(null);
                paymentMethodId = Optional.ofNullable(objParamsJson.getString("paymentMethodId")).orElse(null);
                btPlanId = Optional.ofNullable(objParamsJson.getString("btPlanId")).orElse(null);
                nonce = Optional.ofNullable(objParamsJson.getString("nonce")).orElse(null);
                cTargetId = Optional.ofNullable(objParamsJson.getString("cTargetId")).orElse(null);
                addressTargetId = Optional.ofNullable(objParamsJson.getString("addressTargetId")).orElse(null);
                invalidBizOrderNo = Optional.ofNullable(objParamsJson.getString("invalidBizOrderNo")).orElse(null);
            } catch (Exception e) {
                _log.error("{}objParams参数格式错误！", logPrefix);
            }
        }

        if (amount == 0) {
            processChargeAndSubscriptionSuccess(payOrder, payOrderId, paymentMethodId, btPlanId, invalidBizOrderNo, null);
            _log.info("###### 商户统一下单处理完成(成功) ######");
            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "支付成功", PayConstant.RETURN_VALUE_SUCCESS, null);
            map.put("payOrderId", payOrderId);
            return RpcUtil.createBizResult(baseParam, map);
        }
        if (Strings.isNullOrEmpty(paymentMethodId)) {
            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付失败", PayConstant.RETURN_VALUE_FAIL, null);
            map.put("payOrderId", payOrderId);
            map.put("msg", "支付方式不正确,empty payment method id！");
            return RpcUtil.createBizResult(baseParam, map);
        }

        BigDecimal decimalAmount = new BigDecimal(0);

        try {
            decimalAmount = new BigDecimal(AmountUtil.convertCent2Dollar(String.valueOf(amount)));
        } catch (NumberFormatException e) {
            _log.error("参数转换失败！{}", amount, e);
            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付失败", PayConstant.RETURN_VALUE_FAIL, null);
            map.put("payOrderId", payOrderId);
            map.put("msg", "支付失败！参数转换失败");
            return RpcUtil.createBizResult(baseParam, map);
        }

        TransactionRequest transactionRequest = new TransactionRequest()
                .customerId(cTargetId)
                .billingAddressId(addressTargetId)
                .amount(decimalAmount)
                .customField("product_id", payProductId)
                .customField("biz_order_no", payOrder.getMchOrderNo())
                .paymentMethodToken(paymentMethodId);

        Result<Transaction> result = gateway.transaction().sale(transactionRequest);
        if (result.isSuccess()) {
            processChargeAndSubscriptionSuccess(payOrder, payOrderId, paymentMethodId, btPlanId, invalidBizOrderNo, result);

            _log.info("###### 商户统一下单处理完成(成功) ######");
            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "支付成功", PayConstant.RETURN_VALUE_SUCCESS, null);
            map.put("payOrderId", payOrderId);
            return RpcUtil.createBizResult(baseParam, map);
        } else if (result.getTarget() != null) {
            String transNo = result.getTarget().getId();
            _log.info("######Transaction(failed!)######payOrderId:{}", payOrderId);
            super.baseUpdateStatus4Ing(payOrderId, transNo);
            _log.info("###### Transaction(failed, saved transaction NO.）transNo:{}######,payOrderId:{}", transNo, payOrderId);
            publisher.publishEvent(new PaymentTransactionDoneEvent(payOrder.getParam2(), payOrderId, payOrder.getMchOrderNo(), paymentMethodId, PaymentTransactionDoneEvent.STATUS.FAILED_SAVED_TRANSACTION));

            subscriptionReq(payOrder, paymentMethodId, btPlanId);

            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "支付失败,交易号已创建", PayConstant.RETURN_VALUE_FAIL, null);
            map.put("payOrderId", payOrderId);
            map.put("msg", "支付失败，交易号已创建，请重试！");
            return RpcUtil.createBizResult(baseParam, map);
        }
        publisher.publishEvent(new PaymentTransactionDoneEvent(payOrder.getParam2(), payOrderId, payOrder.getMchOrderNo(), paymentMethodId, PaymentTransactionDoneEvent.STATUS.FAILED));

        _log.info("###### H_CHARGE_SUBSCRIPTION 支付失败（发生异常）######");

        String errorString = "";
        for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
            errorString += "Error: " + error.getCode() + ": " + error.getMessage() + "\n";
        }
        super.baseUpdateStatus4HiveelCCFailed(payOrderId, errorString);
        _log.error("支付异常：{}", errorString);

        Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付失败", PayConstant.RETURN_VALUE_FAIL, null);
        map.put("payOrderId", payOrderId);
        map.put("msg", "支付失败！" + errorString);
        return RpcUtil.createBizResult(baseParam, map);
    }

    private void processChargeAndSubscriptionSuccess(PayOrder payOrder, String payOrderId, String paymentMethodId, String btPlanId, String invalidBizOrderNo, Result<Transaction> result) {
        _log.info("######H_CHARGE_SUBSCRIPTION->Transaction(successed!)######payOrderId:{}", payOrderId);
        if (result != null) {
            super.baseUpdateStatus4HiveelCCSuccess(payOrderId, result.getTarget().getId());
        } else {
            super.baseUpdateStatus4HiveelCCSuccess(payOrderId, PayConstant.PAY_ZERO_PAYMENT);
        }

        subscriptionReq(payOrder, paymentMethodId, btPlanId);

        if (!Strings.isNullOrEmpty(invalidBizOrderNo)) {//取消订阅单号不为空
            paySubscriptionServiceImpl.doUnsubscription(payOrder.getMchOrderNo(), invalidBizOrderNo);
        }

        publisher.publishEvent(new PaymentTransactionDoneEvent(payOrder.getParam2(), payOrderId, payOrder.getMchOrderNo(), paymentMethodId, PaymentTransactionDoneEvent.STATUS.SUCCESS));
    }

    @Override
    public Map doHiveelPayOnceReq(String jsonParam) {
        String logPrefix = "【H_ONCE 一次支付 通道 支付下单】";
        BaseParam baseParam = JsonUtil.getObjectFromJson(jsonParam, BaseParam.class);
        Map<String, Object> bizParamMap = baseParam.getBizParamMap();
        if (ObjectValidUtil.isInvalid(bizParamMap)) {
            _log.warn("{}失败, {}. jsonParam={}", logPrefix, RetEnum.RET_PARAM_NOT_FOUND.getMessage(), jsonParam);
            return RpcUtil.createFailResult(baseParam, RetEnum.RET_PARAM_NOT_FOUND);
        }
        JSONObject payOrderObj = baseParam.isNullValue("payOrder") ? null : JSONObject.parseObject(bizParamMap.get("payOrder").toString());
        PayOrder payOrder = BeanConvertUtils.map2Bean(payOrderObj, PayOrder.class);
        if (ObjectValidUtil.isInvalid(payOrder)) {
            _log.warn("{}失败, {}. jsonParam={}", logPrefix, RetEnum.RET_PARAM_INVALID.getMessage(), jsonParam);
            return RpcUtil.createFailResult(baseParam, RetEnum.RET_PARAM_INVALID);
        }

        String payOrderId = payOrder.getPayOrderId();
        Long amount = payOrder.getAmount();

//        PayChannel payChannel = super.baseSelectPayChannel(mchId, channelId);
        /**
         * BrainTree not support 0 amount, $0 auto success!
         */
        if (amount <= 0) {
            _log.info("###### H_ONCE 支付成功 ######");
            super.baseUpdateStatus4HiveelCCSuccess(payOrderId, PayConstant.PAY_ZERO_PAYMENT);

            _log.info("###### H_ONCE商户统一下单处理完成(成功) ######");
            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "支付成功", PayConstant.RETURN_VALUE_SUCCESS, null);
            map.put("payOrderId", payOrderId);
            return RpcUtil.createBizResult(baseParam, map);
        }


        // 获取objParams参数
        String objParams = payOrder.getExtra();
        String payProductId = null;
        String paymentMethodId = null;
        String cTargetId = null;
        String addressTargetId = null;

        if (StringUtils.isNotEmpty(objParams)) {
            try {
                JSONObject objParamsJson = JSON.parseObject(objParams);

                payProductId = Optional.ofNullable(objParamsJson.getString("payProductId")).orElse(null);
                paymentMethodId = Optional.ofNullable(objParamsJson.getString("paymentMethodId")).orElse(null);
                cTargetId = Optional.ofNullable(objParamsJson.getString("cTargetId")).orElse(null);
                addressTargetId = Optional.ofNullable(objParamsJson.getString("addressTargetId")).orElse(null);
            } catch (Exception e) {
                _log.error("{}objParams参数格式错误！", logPrefix);
            }
        }
        if (Strings.isNullOrEmpty(paymentMethodId)) {
            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付失败", PayConstant.RETURN_VALUE_FAIL, null);
            map.put("payOrderId", payOrderId);
            map.put("msg", "支付方式不正确！");
            return RpcUtil.createBizResult(baseParam, map);
        }

        BigDecimal decimalAmount = new BigDecimal(0);

        try {
            decimalAmount = new BigDecimal(AmountUtil.convertCent2Dollar(String.valueOf(amount)));
        } catch (NumberFormatException e) {
            _log.error("参数转换失败！{}", amount, e);
            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付失败", PayConstant.RETURN_VALUE_FAIL, null);
            map.put("payOrderId", payOrderId);
            map.put("msg", "支付失败！参数转换失败！");
            return RpcUtil.createBizResult(baseParam, map);
        }


        TransactionRequest transactionRequest = new TransactionRequest()
                .customerId(cTargetId)
                .billingAddressId(addressTargetId)
                .amount(decimalAmount)
                .customField("product_id", payProductId)
                .customField("biz_order_no", payOrder.getMchOrderNo())
                .paymentMethodToken(paymentMethodId);

        Result<Transaction> result = gateway.transaction().sale(transactionRequest);
        if (result.isSuccess()) {
            _log.info("######Transaction(successed!)######payOrderId:{}", payOrderId);
            super.baseUpdateStatus4HiveelCCSuccess(payOrderId, result.getTarget().getId());
            publisher.publishEvent(new PaymentTransactionDoneEvent(payOrder.getParam2(), payOrderId, payOrder.getMchOrderNo(), paymentMethodId, PaymentTransactionDoneEvent.STATUS.SUCCESS));
            _log.info("###### 商户统一下单处理完成(成功) ######");
            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "支付成功", PayConstant.RETURN_VALUE_SUCCESS, null);
            map.put("payOrderId", payOrderId);
            return RpcUtil.createBizResult(baseParam, map);
        } else if (result.getTarget() != null) {
            String transNo = result.getTarget().getId();
            _log.info("######Transaction(failed!)######payOrderId:{}", payOrderId);
            super.baseUpdateStatus4Ing(payOrderId, transNo);
            _log.info("###### Transaction(failed, saved transaction NO.）transNo:{}######,payOrderId:{}", transNo, payOrderId);
            publisher.publishEvent(new PaymentTransactionDoneEvent(payOrder.getParam2(), payOrderId, payOrder.getMchOrderNo(), paymentMethodId, PaymentTransactionDoneEvent.STATUS.FAILED_SAVED_TRANSACTION));

            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "支付失败,交易号已创建", PayConstant.RETURN_VALUE_FAIL, null);
            map.put("payOrderId", payOrderId);
            map.put("msg", "支付失败，交易号已创建，请重试！");
            return RpcUtil.createBizResult(baseParam, map);
        }
        publisher.publishEvent(new PaymentTransactionDoneEvent(payOrder.getParam2(), payOrderId, payOrder.getMchOrderNo(), paymentMethodId, PaymentTransactionDoneEvent.STATUS.FAILED));
        _log.info("###### H_ONCE 支付失败（发生异常）######");

        String errorString = "";
        for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
            errorString += "Error: " + error.getCode() + ": " + error.getMessage() + "\n";
        }
        super.baseUpdateStatus4HiveelCCFailed(payOrderId, errorString);
        _log.error("支付异常：{}", errorString);

        Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付失败", PayConstant.RETURN_VALUE_FAIL, null);
        map.put("payOrderId", payOrderId);
        map.put("msg", "支付失败！" + errorString);
        return RpcUtil.createBizResult(baseParam, map);
    }

    private void subscriptionReq(PayOrder payOrder, String paymentMethodId, String btPlanId) {
        String bizOrderNo = payOrder.getMchOrderNo();
        BizOrder bizOrder = bizOrderMapper.selectByBizOrderNo(bizOrderNo);
        PayProduct payProduct = payProductMapper.findByProductId(bizOrder.getProductId());

        String subscriptionId = HiveelID.getInstance().getRandomId("S");
        PaySubscription paySubscription = new PaySubscription();
        paySubscription.setPlanId(btPlanId);
        paySubscription.setPaymentMethodId(paymentMethodId);
        paySubscription.setAmount(payProduct.getAmount());
        paySubscription.setBizOrderNo(bizOrderNo);
        paySubscription.setSubscriptionId(subscriptionId);
        paySubscription.setStatus("NEW");
        super.saveSubscriptionInfo(paySubscription);
        publisher.publishEvent(new SubscriptionRequestEvent(paySubscription));
    }


    @Override
    public Map doHiveelPayPcReq(String jsonParam) {
        return null;
    }

    @Override
    public Map doHiveelPayMobileReq(String jsonParam) {
        return null;
    }

    @Override
    public Map doHiveelPayQrReq(String jsonParam) {
        return null;
    }

    /**
     * 仅仅修改订阅 或则 发起订阅
     *
     * @param jsonParam
     * @return
     */
    @Override
    public Map<String, Object> doHiveelPaySubscriptionReq(String jsonParam) {
        String logPrefix = "【H_SUBSCRIPTION 订阅通道 支付下单】";
        BaseParam baseParam = JsonUtil.getObjectFromJson(jsonParam, BaseParam.class);
        Map<String, Object> bizParamMap = baseParam.getBizParamMap();
        if (ObjectValidUtil.isInvalid(bizParamMap)) {
            _log.warn("{}失败, {}. jsonParam={}", logPrefix, RetEnum.RET_PARAM_NOT_FOUND.getMessage(), jsonParam);
            return RpcUtil.createFailResult(baseParam, RetEnum.RET_PARAM_NOT_FOUND);
        }
        JSONObject payOrderObj = baseParam.isNullValue("payOrder") ? null : JSONObject.parseObject(bizParamMap.get("payOrder").toString());
        PayOrder payOrder = BeanConvertUtils.map2Bean(payOrderObj, PayOrder.class);
        if (ObjectValidUtil.isInvalid(payOrder)) {
            _log.warn("{}失败, {}. jsonParam={}", logPrefix, RetEnum.RET_PARAM_INVALID.getMessage(), jsonParam);
            return RpcUtil.createFailResult(baseParam, RetEnum.RET_PARAM_INVALID);
        }


        String payOrderId = payOrder.getPayOrderId();
        Long amount = payOrder.getAmount();


        // 获取objParams参数
        String objParams = payOrder.getExtra();
        String payProductId = null;
        String paymentMethodId = null;
        String btPlanId = null;
        String nonce = null;
        String cTargetId = null;
        String addressTargetId = null;
        String invalidBizOrderNo = null;

        if (StringUtils.isNotEmpty(objParams)) {
            try {
                JSONObject objParamsJson = JSON.parseObject(objParams);

                payProductId = Optional.ofNullable(objParamsJson.getString("payProductId")).orElse(null);
                paymentMethodId = Optional.ofNullable(objParamsJson.getString("paymentMethodId")).orElse(null);
                btPlanId = Optional.ofNullable(objParamsJson.getString("btPlanId")).orElse(null);
                nonce = Optional.ofNullable(objParamsJson.getString("nonce")).orElse(null);
                cTargetId = Optional.ofNullable(objParamsJson.getString("cTargetId")).orElse(null);
                addressTargetId = Optional.ofNullable(objParamsJson.getString("addressTargetId")).orElse(null);
                invalidBizOrderNo = Optional.ofNullable(objParamsJson.getString("invalidBizOrderNo")).orElse(null);
            } catch (Exception e) {
                _log.error("{}objParams参数格式错误！", logPrefix);
            }
        }
        /**
         * BrainTree not support 0 amount, $0 auto success!
         */
        if (amount <= 0) {
            amount = 0L;
//            _log.info("###### H_SUBSCRIPTION 0元 支付成功 ######");
//            super.baseUpdateStatus4HiveelCCSuccess(payOrderId, "ZERO");
//            if (!Strings.isNullOrEmpty(invalidBizOrderNo)) {
//                paySubscriptionServiceImpl.doUnsubscription(payOrder.getMchOrderNo(), invalidBizOrderNo);
//            }
//            _log.info("######  商户统一下单处理完成(成功) ######");
//            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "支付成功", PayConstant.RETURN_VALUE_SUCCESS, null);
//            map.put("payOrderId", payOrderId);
//            return RpcUtil.createBizResult(baseParam, map);
        }

        if (Strings.isNullOrEmpty(paymentMethodId)) {
            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付失败", PayConstant.RETURN_VALUE_FAIL, null);
            map.put("payOrderId", payOrderId);
            map.put("msg", "支付方式不正确,empty payment method id！");
            return RpcUtil.createBizResult(baseParam, map);
        }

        BigDecimal decimalAmount = new BigDecimal(0);

        try {
            decimalAmount = new BigDecimal(AmountUtil.convertCent2Dollar(String.valueOf(amount)));
        } catch (NumberFormatException e) {
            _log.error("参数转换失败！{}", amount, e);
            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付失败", PayConstant.RETURN_VALUE_FAIL, null);
            map.put("payOrderId", payOrderId);
            map.put("msg", "支付失败！参数转换失败");
            return RpcUtil.createBizResult(baseParam, map);
        }

        String subscriptionId = HiveelID.getInstance().getRandomId("S");
        PaySubscription paySubscription = new PaySubscription();
        paySubscription.setPlanId(btPlanId);
        paySubscription.setPaymentMethodId(paymentMethodId);
        paySubscription.setAmount(amount);
        paySubscription.setBizOrderNo(payOrder.getMchOrderNo());
        paySubscription.setSubscriptionId(subscriptionId);
        paySubscription.setStatus("NEW");
        //super.saveSubscriptionInfo(paySubscription);

        BizOrder bizOrder = bizOrderMapper.selectByBizOrderNo(paySubscription.getBizOrderNo());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bizOrder.getFirstBillDate());
        calendar.add(Calendar.DATE, -1);
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
//            paySubscriptionServiceImpl.updateStatusBySubscriptionId(paySubscription.getSubscriptionId(), result.getTarget().getStatus().name());
            paySubscription.setStatus(result.getTarget().getStatus().name());
            super.saveSubscriptionInfo(paySubscription);

            _log.info("######Transaction(successed!)######payOrderId:{}", payOrderId);
            super.baseUpdateStatus4HiveelSubscriptionSuccess(payOrderId, PayConstant.SUB_SUCCESS_WAITING_TRANS_ID);
            //publisher.publishEvent(new PaymentTransactionDoneEvent(payOrder.getParam2(), payOrderId, payOrder.getMchOrderNo(), paymentMethodId, PaymentTransactionDoneEvent.STATUS.SUCCESS));
            publisher.publishEvent(new SubscriptionDoneEvent(paySubscription, true));

            if (!Strings.isNullOrEmpty(invalidBizOrderNo)) {
                paySubscriptionServiceImpl.doUnsubscription(bizOrder.getBizOrderNo(), invalidBizOrderNo);
            }
            _log.info("###### 商户统一下单处理完成(成功) ######");
            Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "支付成功", PayConstant.RETURN_VALUE_SUCCESS, null);
            map.put("payOrderId", payOrderId);
            return RpcUtil.createBizResult(baseParam, map);
        }
        String errorString = "";
        for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
            errorString += "Error: " + error.getCode() + ": " + error.getMessage() + "\n";
        }
        super.baseUpdateStatus4HiveelCCFailed(payOrderId, errorString);
        _log.error("支付异常：{}", errorString);
        publisher.publishEvent(new SubscriptionDoneEvent(paySubscription, false));
        Map<String, Object> map = HiveelPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付失败", PayConstant.RETURN_VALUE_FAIL, null);
        map.put("payOrderId", payOrderId);
        map.put("msg", "支付失败！" + errorString);
        return RpcUtil.createBizResult(baseParam, map);
    }


}
