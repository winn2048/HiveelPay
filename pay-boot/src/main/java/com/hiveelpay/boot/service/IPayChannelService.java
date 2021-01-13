package com.hiveelpay.boot.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author: wilson
 * @date: 17/9/8
 * @description:
 */
public interface IPayChannelService {

    Map selectPayChannel(String jsonParam);

    JSONObject getByMchIdAndChannelId(String mchId, String channelId);
}
