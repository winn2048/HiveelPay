package com.hiveelpay.boot.service;

import java.util.Map;

/**
 * @author: wilson
 * @date: 17/9/10
 * @description:
 */
public interface IPayChannel4HiveelService {

    Map doHiveelPayWapReq(String jsonParam);

    Map<String, Object> doHiveelPayChargeAndSubscriptionReq(String jsonParam);

    Map<String, Object> doHiveelPayOnceReq(String jsonParam);

    Map doHiveelPayPcReq(String jsonParam);

    Map doHiveelPayMobileReq(String jsonParam);

    Map doHiveelPayQrReq(String jsonParam);

    Map<String, Object> doHiveelPaySubscriptionReq(String jsonParam);
}
