package com.hiveelpay.mgr.service;

import com.google.common.base.Strings;
import com.hiveelpay.common.util.HiveelID;
import com.hiveelpay.dal.dao.mapper.PayProductMapper;
import com.hiveelpay.dal.dao.model.PayProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PayProductsService {


    @Autowired
    private PayProductMapper payProductMapper;


    /**
     * @param offset
     * @param limit
     * @return
     */
    public List<PayProduct> list(int offset, int limit, PayProduct payProduct) {
        List<PayProduct> list = payProductMapper.findList(offset, limit);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }

    public PayProduct selectPayProductInfo(String payProductId) {
        if (Strings.isNullOrEmpty(payProductId)) {
            return null;
        }
        return payProductMapper.findByProductId(payProductId);
    }

    public int count(PayProduct payProduct) {
        return payProductMapper.count(payProduct);
    }

    public int addPayProduct(PayProduct payProduct) {
        payProduct.setProductId(HiveelID.getInstance().getRandomId("P"));
        return payProductMapper.save(payProduct);
    }

    public int updatePayProduct(PayProduct payProduct) {
        return payProductMapper.updatePryProduct(payProduct);
    }

    public void deleteProduct(String payProductId) {
        payProductMapper.deleteByProductId(payProductId);
    }
}
