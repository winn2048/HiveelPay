package com.hiveelpay.boot.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.CreditCard;
import com.braintreegateway.Customer;
import com.braintreegateway.Transaction;
import com.braintreegateway.Transaction.Status;
import com.google.common.collect.Maps;
import com.hiveelpay.boot.service.IMchInfoService;
import com.hiveelpay.boot.service.IPayChannelService;
import com.hiveelpay.boot.service.channel.hiveel.HiveelConfig;
import com.hiveelpay.common.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Map;

@Controller
public class CheckoutController extends BaseController {
    private static final MyLog _log = MyLog.getLog(CheckoutController.class);
    @Autowired
    private HiveelConfig hiveelConfig;
    @Autowired
    private BraintreeGateway gateway;
    @Autowired
    private IMchInfoService mchInfoService;
    @Autowired
    private IPayChannelService payChannelService;

    private Status[] TRANSACTION_SUCCESS_STATUSES = new Status[]{
            Transaction.Status.AUTHORIZED,
            Transaction.Status.AUTHORIZING,
            Transaction.Status.SETTLED,
            Transaction.Status.SETTLEMENT_CONFIRMED,
            Transaction.Status.SETTLEMENT_PENDING,
            Transaction.Status.SETTLING,
            Transaction.Status.SUBMITTED_FOR_SETTLEMENT
    };
//
//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String root(Model model) {
//        return "redirect:checkouts";
//    }

    /**
     * Get token
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/checkouts", method = RequestMethod.GET)
    public String checkout(Model model) {
        String clientToken = gateway.clientToken().generate();
        Map<String, String> map = Maps.newHashMap();
        map.put("clientToken", clientToken);
        model.addAttribute("clientToken", clientToken);
        return "checkouts/new";
    }

    /**
     * 生成支付订单
     *
     * @param amount
     * @param nonce
     * @param userId
     * @param paymentMethodId
     * @param mchId
     * @param channelId
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/checkouts", method = RequestMethod.POST)
    public String postForm(@RequestParam("amount") String amount,
                           @RequestParam("payment_method_nonce") String nonce,
                           @RequestParam("userId") String userId,
                           // 用户的绑卡ID
                           @RequestParam("paymentMethodId") String paymentMethodId,
                           //指定收款商户ID
                           @RequestParam("mchId") String mchId,
                           //商户的支付渠道ID
                           @RequestParam("channelId") String channelId,
                           Model model, final RedirectAttributes redirectAttributes) {

        // todo 根据mchId 查询出 reqKey 和 resKey
        //todo 校验 用户
        //todo 校验 支付方式ID
        //todo 校验 收款商户
        //todo 校验 支付渠道
        //todo 校验 金额

        JSONObject mchInfo = mchInfoService.getByMchId(mchId);
        if (mchInfo == null) {
            model.addAttribute("errorDetails", "商户不存在");
            return "checkouts/new";
        }

        String reqKey = mchInfo.getString("reqKey");// 加签key
        String resKey = mchInfo.getString("resKey");// 验签key

        // 查询商户对应的支付渠道
        JSONObject payChannel = payChannelService.getByMchIdAndChannelId(mchId, channelId);
        if (payChannel == null) {
            String errorMessage = "Can't found payChannel[channelId=" + channelId + ",mchId=" + mchId + "] record in db.";
            model.addAttribute("errorDetails", errorMessage);
            return "checkouts/new";
        }
        if (payChannel.getByte("state") != 1) {
            String errorMessage = "channel not available [channelId=" + channelId + ",mchId=" + mchId + "]";
            model.addAttribute("errorDetails", errorMessage);
            return "checkouts/new";
        }

        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchId);
        paramMap.put("mchOrderNo", HiveelID.getInstance().getRandomId("M"));           // 商户订单号
        paramMap.put("channelId", channelId);             // 支付渠道ID,H_SUBSCRIPTION
        paramMap.put("amount", AmountUtil.convertDollar2Cent(amount));                          // 支付金额,单位分
        paramMap.put("currency", "USD");                    // 币种, cny-人民币,usd-美元
        paramMap.put("clientIp", "127.0.0.1");        // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                      // 设备
        paramMap.put("subject", "pay-subject");
        paramMap.put("body", "silver-membership");
        paramMap.put("notifyUrl", "http://H_SUBSCRIPTION_notifyUrl");         // 回调URL
        paramMap.put("param1", userId);                         // 扩展参数1
        paramMap.put("param2", "customerId");                             // 扩展参数2
        paramMap.put("extra", "{" +
                "\"paymentMethodId\":\"" + paymentMethodId + "\"" +
                ",\"nonce\":\"" + nonce + "\"" +
                "}");  // 附加参数
        String reqSign = PayDigestUtil.getSign(paramMap, reqKey);
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url = hiveelConfig.getPayServerBaseUrl() + "/pay/create_order?";
        String rs = HiveelPayUtil.call4Post(url + reqData);
        System.out.println("请求支付中心下单接口,响应数据:" + rs);
        Map retMap = JSON.parseObject(rs);

        if ("SUCCESS".equals(retMap.get("retCode"))) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retMap, resKey, "sign", "payParams");
            String retSign = (String) retMap.get("sign");
            if (checkSign.equals(retSign)) {
                System.out.println("=========支付中心下单验签成功=========");
            } else {
                System.err.println("=========支付中心下单验签失败=========");
                return null;
            }
        }

        model.addAttribute("errorDetails", rs);
        return "checkouts/new";
    }

    @RequestMapping(value = "/checkouts/{transactionId}")
    public String getTransaction(@PathVariable String transactionId, Model model) {
        Transaction transaction;
        CreditCard creditCard;
        Customer customer;

        try {
            transaction = gateway.transaction().find(transactionId);
            creditCard = transaction.getCreditCard();
            customer = transaction.getCustomer();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            return "redirect:/checkouts";
        }

        model.addAttribute("isSuccess", Arrays.asList(TRANSACTION_SUCCESS_STATUSES).contains(transaction.getStatus()));
        model.addAttribute("transaction", transaction);
        model.addAttribute("creditCard", creditCard);
        model.addAttribute("customer", customer);

        return "checkouts/show";
    }
}
