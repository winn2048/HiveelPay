package com.hiveelpay.boot.service;

import java.util.Map;

/**
 * @author: wilson
 * @date: 17/9/9
 * @description:
 */
public interface IPayChannel4WxService {

    Map doWxPayReq(String jsonParam);

}
