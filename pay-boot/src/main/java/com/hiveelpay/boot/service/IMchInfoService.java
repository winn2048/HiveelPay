package com.hiveelpay.boot.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author: wilson
 * @date: 17/9/8
 * @description:
 */
public interface IMchInfoService {

    Map selectMchInfo(String jsonParam);

    JSONObject getByMchId(String mchId);

}
