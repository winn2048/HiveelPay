package com.hiveelpay.boot.service;

import java.util.Map;

/**
 * @author: wilson
 * @date: 17/9/10
 * @description:
 */
public interface INotifyPayService {

    Map doAliPayNotify(String jsonParam);

    Map doWxPayNotify(String jsonParam);

    Map sendBizPayNotify(String jsonParam);

    String handleAliPayNotify(Map params);

    String handleWxPayNotify(String xmlResult);
}
