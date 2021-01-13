package com.hiveelpay.mgr.service;

import com.hiveelpay.dal.dao.mapper.BizOrderMapper;
import com.hiveelpay.dal.dao.model.BizOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BizOrderService {

    @Autowired
    private BizOrderMapper bizOrderMapper;


    public BizOrder selectPayOrder(String bizOrderNo) {
        return bizOrderMapper.selectByBizOrderNo(bizOrderNo);
    }

    public List<BizOrder> getPayOrderList(int offset, int limit, BizOrder bizOrder) {
        return bizOrderMapper.searchBizOrder(offset, limit, bizOrder);
    }

    public Integer count(BizOrder bizOrder) {
        return bizOrderMapper.searchBizOrderCount(bizOrder);
    }


}
