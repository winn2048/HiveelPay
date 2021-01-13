package com.hiveelpay.mgr.service;

import com.hiveelpay.dal.dao.mapper.PayOrderMapper;
import com.hiveelpay.dal.dao.model.PayOrder;
import com.hiveelpay.dal.dao.model.PayOrderExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wilson on 18/5/7.
 */
@Component
public class PayOrderService {

    @Autowired
    private PayOrderMapper payOrderMapper;

    public PayOrder selectPayOrder(String payOrderId) {
        return payOrderMapper.selectByPrimaryKey(payOrderId);
    }

    public List<PayOrder> getPayOrderList(int offset, int limit, PayOrder payOrder) {
        PayOrderExample example = new PayOrderExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        PayOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        return payOrderMapper.selectByExample(example);
    }

    public Integer count(PayOrder payOrder) {
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, payOrder);
        return payOrderMapper.countByExample(example);
    }

    void setCriteria(PayOrderExample.Criteria criteria, PayOrder payOrder) {
        if (payOrder != null) {
            if (StringUtils.isNotBlank(payOrder.getMchId())) criteria.andMchIdEqualTo(payOrder.getMchId());

            if (StringUtils.isNotBlank(payOrder.getPayOrderId()))
                criteria.andPayOrderIdEqualTo(payOrder.getPayOrderId());

            if (StringUtils.isNotBlank(payOrder.getMchOrderNo()))
                criteria.andMchOrderNoEqualTo(payOrder.getMchOrderNo());

            if (StringUtils.isNotBlank(payOrder.getChannelOrderNo()))
                criteria.andChannelOrderNoEqualTo(payOrder.getChannelOrderNo());

            if (payOrder.getStatus() != null && payOrder.getStatus() != -99)
                criteria.andStatusEqualTo(payOrder.getStatus());
        }
    }

}
