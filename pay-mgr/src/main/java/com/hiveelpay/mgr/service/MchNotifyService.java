package com.hiveelpay.mgr.service;

import com.hiveelpay.dal.dao.mapper.MchNotifyMapper;
import com.hiveelpay.dal.dao.model.MchNotify;
import com.hiveelpay.dal.dao.model.MchNotifyExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wilson on 18/11/03.
 */
@Component
public class MchNotifyService {

    @Autowired
    private MchNotifyMapper mchNotifyMapper;

    public MchNotify selectMchNotify(String orderId) {
        return mchNotifyMapper.selectByPrimaryKey(orderId);
    }

    public List<MchNotify> getMchNotifyList(int offset, int limit, MchNotify mchNotify) {
        MchNotifyExample example = new MchNotifyExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        MchNotifyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchNotify);
        return mchNotifyMapper.selectByExample(example);
    }

    public Integer count(MchNotify mchNotify) {
        MchNotifyExample example = new MchNotifyExample();
        MchNotifyExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchNotify);
        return mchNotifyMapper.countByExample(example);
    }

    void setCriteria(MchNotifyExample.Criteria criteria, MchNotify mchNotify) {
        if (mchNotify != null) {
            if (StringUtils.isNotBlank(mchNotify.getMchId())) criteria.andMchIdEqualTo(mchNotify.getMchId());

            if (StringUtils.isNotBlank(mchNotify.getOrderId())) criteria.andOrderIdEqualTo(mchNotify.getOrderId());

            if (StringUtils.isNotBlank(mchNotify.getOrderType()))
                criteria.andOrderTypeEqualTo(mchNotify.getOrderType());

            if (StringUtils.isNotBlank(mchNotify.getMchOrderNo()))
                criteria.andMchOrderNoEqualTo(mchNotify.getMchOrderNo());

            if (mchNotify.getStatus() != null && mchNotify.getStatus() != -99)
                criteria.andStatusEqualTo(mchNotify.getStatus());
        }
    }

}
