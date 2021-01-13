package com.hiveelpay.boot.service;

import java.util.Map;

/**
 * @author: wilson
 * @date: 17/9/10
 * @description:
 */
public interface IPayChannel4AliService {

    Map doAliPayWapReq(String jsonParam);

    Map doAliPayPcReq(String jsonParam);

    Map doAliPayMobileReq(String jsonParam);

    Map doAliPayQrReq(String jsonParam);

}
