package com.hiveelpay.boot.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.braintreegateway.BraintreeGateway;
import com.google.common.base.Strings;
import com.hiveelpay.boot.ctrl.exceptions.HiveelCheckoutException;
import com.hiveelpay.boot.ctrl.exceptions.HiveelPayErrorCode;
import com.hiveelpay.boot.ctrl.exceptions.HiveelUserNotExistsException;
import com.hiveelpay.boot.model.CustomerBindCardResult;
import com.hiveelpay.boot.service.*;
import com.hiveelpay.boot.service.channel.hiveel.HiveelConfig;
import com.hiveelpay.boot.service.events.BizTransactionDoneEvent;
import com.hiveelpay.common.constant.PayConstant;
import com.hiveelpay.common.domain.RestAPIResult;
import com.hiveelpay.common.enumm.*;
import com.hiveelpay.common.exceptions.HiveelPayException;
import com.hiveelpay.common.model.vo.PayRequestVo;
import com.hiveelpay.common.util.*;
import com.hiveelpay.dal.dao.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.hiveelpay.common.domain.ResultStatus.FAILED;
import static com.hiveelpay.common.domain.ResultStatus.SUCCESS;
import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyyHHmm;

@RestController
@RequestMapping("/api/pay")
public class HiveelCheckoutController extends BaseController {
    private static final MyLog _log = MyLog.getLog(HiveelCheckoutController.class);
    private static final Object lock = new Object();

    @Autowired
    private HiveelConfig hiveelConfig;
    @Autowired
    private BraintreeGateway gateway;
    @Autowired
    private IMchInfoService mchInfoService;
    @Autowired
    private IPayChannelService payChannelService;
    @Autowired
    private IHiveelCheckoutService hiveelCheckoutServiceImpl;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private BizOrderService bizOrderServiceImpl;
    @Autowired
    private PayProductService payProductServiceImpl;
    @Autowired
    private CustomerValidServiceService customerValidServiceServiceImpl;
    @Autowired
    private AppointmentDocService appointmentDocServiceImpl;

    /**
     * step 1 get token
     *
     * @return
     */
    @GetMapping("/token")
    public RestAPIResult<String> token() {
        String clientToken = gateway.clientToken().generate();
        return new RestAPIResult<>(SUCCESS, clientToken);
    }

    @GetMapping("/price/{productId}")
    public RestAPIResult<String> price(HttpServletRequest request, @PathVariable("productId") String productId) {
        PayProduct payProduct = payProductServiceImpl.findProductByProductId(productId);
        if (payProduct == null || (!payProduct.getProductStatus().equals(PayProductStatusEnum.SELLING))) {
            throw new HiveelPayException(HiveelPayErrorCode.INCORRECT_PAY_PRODUCT);
        }
        String userId = getUserIdNullAble(request);
        if (Strings.isNullOrEmpty(userId)) {
            return new RestAPIResult<>(SUCCESS, AmountUtil.convertCent2Dollar(String.valueOf(payProduct.getAmount())));
        }
        //todo check here
        return new RestAPIResult<>(SUCCESS, "0.0");
    }

    /**
     * 开始支付
     *
     * @param request
     * @param payRequestVo
     * @return
     */
    @PostMapping("/checkout")
    public RestAPIResult<String> checkout(HttpServletRequest request, PayRequestVo payRequestVo) {
        _log.debug("HIVEELPAY======PayRequest:{}", payRequestVo);
        String userId = getUserId(request);
        if (Strings.isNullOrEmpty(userId)) {
            throw new HiveelUserNotExistsException();
        }
        checkArgument(payRequestVo != null, "payRequest can't null!");
        payRequestVo.setInvalidBizOrderNo(null);
        checkArgument(!Strings.isNullOrEmpty(payRequestVo.getNonce()), "Please post a valid nonce!");
        checkArgument(!Strings.isNullOrEmpty(payRequestVo.getProductId()), "productId can't null!");
        checkArgument(payRequestVo.getAccountType() != null, "accountType  can't null!");
        checkArgument(payRequestVo.getPayType() != null && !payRequestVo.getPayType().equals(PaymentMethodTypeEnum.ILLEGAL), "Not support payType!");

        checkArgument(payRequestVo.getBilledMethod() != null && !payRequestVo.getBilledMethod().equals(BilledMethodEnum.ILLEGAL), "Not support this billed method.");

        checkArgument(!Strings.isNullOrEmpty(payRequestVo.getFirstName()), "firstName can't null.");
        checkArgument(!Strings.isNullOrEmpty(payRequestVo.getLastName()), "lastName can't null.");
        checkArgument(Long.valueOf(AmountUtil.convertDollar2Cent(payRequestVo.getTotalAmount())) >= 0, "total amount must >0.");
        if (payRequestVo.getCurrency() == null) {
            payRequestVo.setCurrency(CurrencyEnum.USD);
        }

        checkArgument(!Strings.isNullOrEmpty(payRequestVo.getAddr()), "Address line1 can't null");
        checkArgument(!Strings.isNullOrEmpty(payRequestVo.getCity()), "Address city can't null");
        checkArgument(!Strings.isNullOrEmpty(payRequestVo.getState()), "Address state can't null");
        checkArgument(!Strings.isNullOrEmpty(payRequestVo.getZipcode()), "Address zipcode can't null!");

        if (Strings.isNullOrEmpty(payRequestVo.getServiceTimes())) {// checkout pay checkout times
            payRequestVo.setServiceTimes(DateUtil.getCurrentTimeStr(FORMAT_MMddyyyyHHmm));
        } else {
            Arrays.stream(payRequestVo.getServiceTimes().split(";")).forEach(this::checkDateFormat);
        }

        /**
         * check product
         */
        String productId = payRequestVo.getProductId();
        PayProduct payProduct = payProductServiceImpl.findProductByProductId(productId);
        if (payProduct == null || (!payProduct.getProductStatus().equals(PayProductStatusEnum.SELLING))) {
            return new RestAPIResult<>(FAILED, "Invalidated Product!");
        }
        payRequestVo.setChannelId(payProduct.isSupportAutoPay() ? PayConstant.PAY_CHANNEL_H_CHARGE_SUBSCRIPTION : PayConstant.PAY_CHANNEL_H_ONCE);

        if (!payProduct.getProductType().equals(PayProductTypeEnum.MEMBERSHIP)) {
            //check docId not null
            checkArgument(!Strings.isNullOrEmpty(payRequestVo.getDocId()), "docId(carId) can't null!");
        }
        if (payProduct.getProductType().equals(PayProductTypeEnum.CAR_OF_DAY)) {
            checkArgument(!Strings.isNullOrEmpty(payRequestVo.getDocZipcode()), "docZipcode can't null!");
        }

        /**
         * check plan id
         */
        if ((!payRequestVo.getBilledMethod().equals(BilledMethodEnum.ONETIME)) && Strings.isNullOrEmpty(payProduct.getBtPlanId())) {//
            _log.info("Please set plan id on PayProduct.");
            return new RestAPIResult<>(FAILED, null, "Invalidated billedMethod!");
        }
        //todo check user
        CustomerAccount customerAccount = hiveelCheckoutServiceImpl.findCustomerByUserId(userId);

        /**
         * stpe1  check and save payment method
         */
        CustomerBindCardResult bindCardResult = null;
        try {
            if (customerAccount == null) {//
                payRequestVo.setPayMethodId(null);// clean payment method id
                //create customer account and save payment method
                payRequestVo.setUserId(userId);
                bindCardResult = hiveelCheckoutServiceImpl.createCustomerAccountAndCardBinding(payRequestVo);
            } else {//
                if (payProduct.getProductType().equals(PayProductTypeEnum.MEMBERSHIP)) {//会员产品
//                    bizOrderServiceImpl.checkAndEndService();
//                    bizOrderServiceImpl.updateToInservice();

                    CustomerValidServices cvs = customerValidServiceServiceImpl.findCurrentMemberShipService(customerAccount.getCustomerId());
                    if (cvs != null) {
                        final String customerId = cvs.getCustomerId();
                        final String currentProductId = cvs.getProductId();
                        final String bizOrderNo = cvs.getBizOrderNo();

                        BizOrder bizOrder = bizOrderServiceImpl.findByBizOrderNo(customerId, bizOrderNo);

                        PayProduct purchasedProduct = payProductServiceImpl.findProductByProductId(currentProductId);
                        if (purchasedProduct.getProductId().equalsIgnoreCase(payProduct.getProductId())) {//not upgrade or downgrade, we need reject the request.
                            return new RestAPIResult<>(FAILED, bizOrder.getBizOrderNo(), "The Customer,userid=" + customerAccount.getUserId() + " has already purchased membership of:" + purchasedProduct.getProductName());
                        }
//todo check 会员 降级 支付时，上一个订阅的账单是否已出？
//                        Transaction transaction = paySubscriptionService.getLastTransaction(bizOrder.getBizOrderNo());
//                        if (transaction != null) {
//
//                        }
                        if (purchasedProduct.getAmount() > payProduct.getAmount()) {//membership from high level to low level
                            List<BizOrder> serviceNotStartMembershipBizOrders = bizOrderServiceImpl.findServiceNotStartMembershipBizOrders(customerId);
                            if (serviceNotStartMembershipBizOrders.size() > 0) {
                                return new RestAPIResult<>(FAILED, null, "You already have one or more service not start biz-order,Please cancel it and then you can do downgrade.");
                            }
                            payRequestVo.setChannelId(PayConstant.PAY_CHANNEL_H_SUBSCRIPTION);
                            payRequestVo.setServiceTimes(DateUtil.date2Str(bizOrder.getServiceTimes().get(0).getServiceEndTime(), FORMAT_MMddyyyyHHmm));
                            payRequestVo.setRemark("downgrade membership:" + purchasedProduct.getProductId() + "(" + purchasedProduct.getAmount() + ")->" + payProduct.getProductId() + "(" + payProduct.getAmount() + ")");
                            payRequestVo.setInvalidBizOrderNo(bizOrderNo);
                        } else {// membership from low level to high level
                            int intervalDays = DateUtil.getIntervalDays(bizOrder.getServiceTimes().get(0).getServiceStartTime(), bizOrder.getServiceTimes().get(0).getServiceEndTime());
                            int usedDays = DateUtil.getIntervalDays(bizOrder.getServiceTimes().get(0).getServiceStartTime(), new Date());
                            long priceDifference = payProduct.getAmount() - (purchasedProduct.getAmount() - (purchasedProduct.getAmount() / intervalDays) * usedDays);
                            _log.info("#customer:{},membership upgrade,used {} cents, price difference:{}  cents. high level price:{},low level price:{}", customerId, usedDays, priceDifference, payProduct.getAmount(), purchasedProduct.getAmount());
                            if (priceDifference <= 0) {
                                priceDifference = 0;
                            }
                            payRequestVo.setRemark("upgrade membership:" + payProduct.getProductId() + "(" + payProduct.getAmount() + ")->" + purchasedProduct.getProductId() + "(" + purchasedProduct.getAmount() + ")priceDifference:" + priceDifference + ",usedDays:" + usedDays);
                            payRequestVo.setTotalAmount(AmountUtil.convertCent2Dollar(String.valueOf(priceDifference)));
                            payRequestVo.setInvalidBizOrderNo(bizOrderNo);
                            payRequestVo.setChannelId(PayConstant.PAY_CHANNEL_H_CHARGE_SUBSCRIPTION);
                        }
                    }
                } else if (
                        payProduct.getProductType().equals(PayProductTypeEnum.OIL_CHANGE) ||
                                payProduct.getProductType().equals(PayProductTypeEnum.PRE_INSPECTION) ||
                                payProduct.getProductType().equals(PayProductTypeEnum.TRADE_IN)
                ) {
                    if (Strings.isNullOrEmpty(payRequestVo.getDocId())) {
                        return new RestAPIResult<>(FAILED, null, "Please fill appointment first!");
                    }

                    AppointmentDoc appointmentDoc = appointmentDocServiceImpl.findAppointmentsById(payRequestVo.getDocId());
                    if (appointmentDoc == null) {
                        return new RestAPIResult<>(FAILED, null, "Please fill appointment first!");
                    }

                    if (!appointmentDoc.getAppointmentStatus().equals(AppointmentStatus.SAVED)) {
                        _log.warn("Invalid appointment to do payment: appointmentId:{},status:{}", appointmentDoc.getAppointmentId(), appointmentDoc.getAppointmentStatus());
                        return new RestAPIResult<>(FAILED, null, "You can't pay for this appointment:" + appointmentDoc.getAppointmentId());
                    }

                    payRequestVo.setServiceTimes(DateUtil.date2Str(appointmentDoc.getAppointmentTime(), FORMAT_MMddyyyyHHmm));
                }
                bindCardResult = hiveelCheckoutServiceImpl.updateCustomerAccountAndCardBinding(customerAccount, payRequestVo);
            }
        } catch (Exception e) {
            _log.error("", e);

            if (e instanceof HiveelPayException) {
                HiveelPayException hpe = (HiveelPayException) e;
                return new RestAPIResult<>(hpe.getErrorCode());
            }
            return new RestAPIResult<>(FAILED, null, "支付方式异常！请检查！");
        }

        final String mchId = payRequestVo.getMchId();
        final String channelId = payRequestVo.getChannelId();
        //查询商户
        JSONObject mchInfo = mchInfoService.getByMchId(mchId);
        if (mchInfo == null) {
            _log.warn("Can't find valid merchant by mchId:{}", mchId);
            return new RestAPIResult<>(FAILED, "商户不存在:" + mchId);
        }

        String reqKey = mchInfo.getString("reqKey");// 加签key
        String resKey = mchInfo.getString("resKey");// 验签key

        // 查询商户对应的支付渠道
        JSONObject payChannel = payChannelService.getByMchIdAndChannelId(mchId, channelId);
        if (payChannel == null) {
            String errorMessage = "Can't found payChannel[channelId=" + channelId + ",mchId=" + mchId + "].";
            return new RestAPIResult<>(FAILED, errorMessage);
        }
        if (payChannel.getByte("state") != 1) {
            String errorMessage = "channel not available [channelId=" + channelId + ",mchId=" + mchId + "].";
            return new RestAPIResult<>(FAILED, errorMessage);
        }

        if (payProduct.getProductType().equals(PayProductTypeEnum.MEMBERSHIP)) {
            //Membership need subscription
            //提前一天生成账单
            payRequestVo.setFirstBillDate(DateUtil.date2Str(DateUtil.addDays(DateUtil.addMonths(new Date(), 1), -1), FORMAT_MMddyyyyHHmm));
        } else {
            payRequestVo.setFirstBillDate(DateUtil.getCurrentTimeStr(FORMAT_MMddyyyyHHmm));
        }


        /**
         * step 2  create biz order
         */
        BizOrder bizOrder = null;
        try {
            bizOrder = bizOrderServiceImpl.createOrder(payProduct, bindCardResult, payRequestVo);
        } catch (Exception e) {
            _log.error("Create Biz Order error!", e);
            return new RestAPIResult<>(FAILED, null, "BizOrder下单失败！请检查！");
        }

        /**
         * step 3 支付
         */
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchId);
        paramMap.put("mchOrderNo", bizOrder.getBizOrderNo());           // 商户订单号
        paramMap.put("channelId", channelId);             // 支付渠道ID,H_SUBSCRIPTION
        paramMap.put("amount", String.valueOf(bizOrder.getPayAmount()));                          // 支付金额,单位分
        paramMap.put("currency", bizOrder.getCurrency().name());                    // 币种, cny-人民币,usd-美元
        paramMap.put("clientIp", "127.0.0.1");        // 用户地址,IP或手机号
        paramMap.put("device", "WEB");   // 设备
        paramMap.put("subject", bizOrder.getProductName());
        paramMap.put("body", bizOrder.getProductType() + "," + joinServiceTime(bizOrder.getServiceTimes()));
        paramMap.put("notifyUrl", "http://");         // 回调URL
        paramMap.put("param1", userId);   // 扩展参数1
        paramMap.put("param2", bizOrder.getCustomerId());  // 扩展参数2
        paramMap.put("extra", "{" +
                "\"payProductId\":\"" + bizOrder.getProductId() + "\"" +
                ",\"paymentMethodId\":\"" + bindCardResult.getPaymentMethod().getToken() + "\"" +
                ",\"btPlanId\":\"" + payProduct.getBtPlanId() + "\"" +
                ",\"addressTargetId\":\"" + bindCardResult.getAddress().getTargetId() + "\"" +
                ",\"nonce\":\"" + payRequestVo.getNonce() + "\"" +
                ",\"invalidBizOrderNo\":\"" + Strings.nullToEmpty(payRequestVo.getInvalidBizOrderNo()) + "\"" +
                ",\"cTargetId\":\"" + bindCardResult.getCustomerAccount().getTargetId() + "\"" +
                "}");  // 附加参数
        String reqSign = PayDigestUtil.getSign(paramMap, reqKey);
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        _log.info("请求支付中心下单接口,请求数据:{}" + reqData);
        String url = hiveelConfig.getPayServerBaseUrl() + "/pay/create_order?";
        String rs = HiveelPayUtil.call4Post(url + reqData);
        _log.info("请求支付中心下单接口,响应数据:{}" + rs);
        Map retMap = JSON.parseObject(rs);

        if ("SUCCESS".equals(retMap.get("retCode"))) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retMap, resKey, "sign", "payParams");
            String retSign = (String) retMap.get("sign");
            if (checkSign.equals(retSign)) {
                _log.info("=========支付中心下单验签成功=========");
                if (retMap.get("resCode").equals("SUCCESS") && retMap.get("payOrderId") != null) {
                    try {
                        String payOrderId = (String) retMap.get("payOrderId");
                        bizOrder.setRemark("payOrderId:" + payOrderId);
                        publisher.publishEvent(new BizTransactionDoneEvent(bizOrder, BizTransactionDoneEvent.STATUS.SUCCESS));
                    } catch (Exception e) {
                        _log.error("", e);
                        return new RestAPIResult<>(SUCCESS, bizOrder.getBizOrderNo(), "Pay success, biz-order status update failed.");
                    }
                    return new RestAPIResult<>(SUCCESS, bizOrder.getBizOrderNo(), "Pay success.");
                } else if (retMap.get("resCode").equals("FAIL")) {
                    _log.info("=========支付中心下单验签成功,支付失败，但是交易已经创建=========");
                }

                publisher.publishEvent(new BizTransactionDoneEvent(bizOrder, BizTransactionDoneEvent.STATUS.FAILED));
                return new RestAPIResult<>(FAILED, bizOrder.getBizOrderNo(), "支付中心支付失败");
            }
            _log.info("=========支付中心下单验签失败=========");
            publisher.publishEvent(new BizTransactionDoneEvent(bizOrder, BizTransactionDoneEvent.STATUS.FAILED));
            return new RestAPIResult<>(FAILED, bizOrder.getBizOrderNo(), "支付中心下单验签失败");
        }
        return new RestAPIResult<>(FAILED, bizOrder.getBizOrderNo(), "支付失败");
    }

    private void checkDateFormat(String serviceTime) {
        for (String s : serviceTime.split(",")) {
            Date date = null;
            try {
                date = DateUtil.strToDate(s, FORMAT_MMddyyyyHHmm);
            } catch (Exception e) {
                throw new HiveelCheckoutException(HiveelPayErrorCode.ILLEGAL_PARAMETERS, e, "Wrong serviceTime format.");
            }
            if (date == null) {
                throw new HiveelCheckoutException(HiveelPayErrorCode.ILLEGAL_PARAMETERS, "Wrong serviceTime format.");
            }
        }
    }

    private String joinServiceTime(List<ServiceTime> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(i -> {
            sb.append(i.getServiceStartTime());
            sb.append(",");
            sb.append(i.getServiceEndTime());
            sb.append(";");
        });
        return sb.toString();
    }
}
