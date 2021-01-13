package com.hiveelpay.boot.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author: wilson
 * @date: 17/9/8
 * @description:
 */
public interface IPayOrderService {

    Map createPayOrder(String jsonParam);

    Map selectPayOrder(String jsonParam);

    Map selectPayOrderByMchIdAndPayOrderId(String jsonParam);

    Map selectPayOrderByMchIdAndMchOrderNo(String jsonParam);

    Map updateStatus4Ing(String jsonParam);

    Map updateStatus4Success(String jsonParam);

    Map updateStatus4Complete(String jsonParam);

    Map updateNotify(String jsonParam);

    int createPayOrder(JSONObject payOrder);

    JSONObject queryPayOrder(String mchId, String payOrderId, String mchOrderNo, String executeNotify);

    String doWxPayReq(String tradeType, JSONObject payOrder, String resKey);

    String doAliPayReq(String channelId, JSONObject payOrder, String resKey);

    /**
     * hiveel pay
     * @param channelId
     * @param payOrder
     * @param resKey
     * @return
     */
    String doHiveelPayReq(String channelId, JSONObject payOrder, String resKey);


}
