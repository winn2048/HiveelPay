package com.hiveelpay.boot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.hiveelpay.boot.service.PayProductService;
import com.hiveelpay.common.enumm.PayProductStatusEnum;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.mapper.PayProductMapper;
import com.hiveelpay.dal.dao.model.PayProduct;

import java.util.Collections;
import java.util.List;

@Service
public class PayProductServiceImpl implements PayProductService {
    private static final MyLog _log = MyLog.getLog(PayProductServiceImpl.class);
    @Autowired
    private PayProductMapper payProductMapper;

    @Override
    public List<PayProduct> findAll() {
        List<PayProduct> list = payProductMapper.findAllSellingProducts(PayProductStatusEnum.SELLING);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public List<PayProduct> findProductsByType(PayProductTypeEnum payProductType) {
        List<PayProduct> list = payProductMapper.findByType(payProductType, PayProductStatusEnum.SELLING);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public PayProduct findProductByProductId(String productId) {
        return payProductMapper.findByProductId(productId);
    }
}
