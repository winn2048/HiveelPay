package com.hiveelpay.boot.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hiveelpay.boot.service.AppointmentDocService;
import com.hiveelpay.boot.service.BizOrderService;
import com.hiveelpay.boot.service.PaymentMethodService;
import com.hiveelpay.common.domain.RestAPIResult;
import com.hiveelpay.common.domain.ResultStatus;
import com.hiveelpay.common.enumm.BizOrderStatus;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.common.model.HiveelPage;
import com.hiveelpay.common.model.vo.AppointmentDocVo;
import com.hiveelpay.common.model.vo.BizOrderVo;
import com.hiveelpay.common.model.vo.PayOrderVo;
import com.hiveelpay.common.model.vo.PaySubscriptionVo;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.model.AppointmentDoc;
import com.hiveelpay.dal.dao.model.BizOrder;
import com.hiveelpay.dal.dao.model.PayOrder;
import com.hiveelpay.dal.dao.model.PaymentMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.hiveelpay.common.domain.ResultStatus.SUCCESS;

@RestController
@RequestMapping("/api/biz")
public class HiveelBizOrderController extends BaseController {
    private static final MyLog _log = MyLog.getLog(HiveelBizOrderController.class);

    @Autowired
    private ConversionService conversionService;
    @Autowired
    private BizOrderService bizOrderServiceImpl;
    @Autowired
    private AppointmentDocService appointmentDocServiceImpl;
    @Autowired
    private PaymentMethodService paymentMethodServiceImpl;


    /**
     * 获取用户的历史交易订单
     *
     * @param hiveelPage
     * @return
     */
    @GetMapping(value = {
            "/order/history/{userId}",
            "/order/history/{userId}/{kinds}"
    })
    public RestAPIResult<Map<String, Object>> list(HttpServletRequest request, HiveelPage hiveelPage,
                                                   @PathVariable(value = "userId") String userId,
                                                   @PathVariable(value = "kinds", required = false) String kinds) {
//        if (Strings.isNullOrEmpty(userId)) {
//            userId = getUserId(request);
//        }

        Set<PayProductTypeEnum> kindSet = Sets.newHashSet();
        if (!Strings.isNullOrEmpty(kinds)) {
            for (String kind : kinds.split(",")) {
                try {
                    kindSet.add(PayProductTypeEnum.byValue(Integer.valueOf(kind)));
                } catch (Exception ignored) {
                }
            }
        }
        List<BizOrder> list = bizOrderServiceImpl.findHistoryOrder(userId, kindSet, hiveelPage);
        if (list == null || list.isEmpty()) {
            return new RestAPIResult<>(SUCCESS, Collections.emptyMap());
        }
        Map<String, Object> mapResult = Maps.newHashMap();

        List<BizOrderVo> rsList = Lists.newArrayList();
        list.forEach(i -> {
            BizOrderVo bv = conversionService.convert(i, BizOrderVo.class);
            List<PayOrder> payOrders = i.getPayOrders();
            if (payOrders != null && !payOrders.isEmpty()) {
                List<PayOrderVo> povs = Lists.newArrayList();
                payOrders.forEach(k -> {
                    String objParams = k.getExtra();

                    if (StringUtils.isNotEmpty(objParams)) {
                        try {
                            JSONObject objParamsJson = JSON.parseObject(objParams);
                            String paymentMethodId = Optional.ofNullable(objParamsJson.getString("paymentMethodId")).orElse(null);

                            PaymentMethod paymentMethod = paymentMethodServiceImpl.findPaymentMethodById(paymentMethodId);
                            if (paymentMethod != null) {
                                k.setPaymentMethod(paymentMethod);
                            }
                        } catch (Exception e) {
                            _log.error("{}objParams参数格式错误！", e);
                        }
                    }
                    povs.add(conversionService.convert(k, PayOrderVo.class));
                });
                bv.setPayOrderList(povs);
            }

            if (i.getPaySubscription() != null) {
                bv.setPaySubscription(conversionService.convert(i.getPaySubscription(), PaySubscriptionVo.class));
            }
            /**
             * 这三类服务需要加载预约单
             */
            if (!Strings.isNullOrEmpty(i.getDocId()) && (i.getProductType().equalsIgnoreCase(PayProductTypeEnum.OIL_CHANGE.name()) ||
                    i.getProductType().equalsIgnoreCase(PayProductTypeEnum.PRE_INSPECTION.name()) ||
                    i.getProductType().equalsIgnoreCase(PayProductTypeEnum.TRADE_IN.name()))) {
                AppointmentDoc appointmentDoc = appointmentDocServiceImpl.findByDocId(i.getDocId());
                if (appointmentDoc != null) {
                    bv.setAppointment(conversionService.convert(appointmentDoc, AppointmentDocVo.class));
                }
            }
            rsList.add(bv);
        });

        mapResult.put("list", rsList);
        mapResult.put("page", hiveelPage);

        return new RestAPIResult<>(SUCCESS, mapResult);
    }

    /**
     * 发起退款
     *
     * @param bizOrderId
     * @return
     */
    @GetMapping(value = "/refund/{bizOrderId}")
    public RestAPIResult<String> refundBizOrder(@PathVariable("bizOrderId") String bizOrderId) {
        BizOrder bizOrder = bizOrderServiceImpl.findBizOrder(bizOrderId);
        if (bizOrder == null) {
            return new RestAPIResult<>(ResultStatus.FAILED, "", "Can't find order by orderId:" + Strings.nullToEmpty(bizOrderId));
        }

        if (!bizOrder.getOrderStatus().equals(BizOrderStatus.REFUNDED)) {
            return new RestAPIResult<>(ResultStatus.FAILED, "", "Already refunded!");
        }

        if (!bizOrder.getOrderStatus().equals(BizOrderStatus.CANCELED)) {
            bizOrderServiceImpl.refundOrder(bizOrder);
        }
        return new RestAPIResult<>(SUCCESS, "");
    }


}
