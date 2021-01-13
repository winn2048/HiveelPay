package com.hiveelpay.boot.service;

import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.dal.dao.model.PayProduct;

import java.util.List;

public interface PayProductService {

    List<PayProduct> findAll();

    List<PayProduct> findProductsByType(PayProductTypeEnum payProductType);

    PayProduct findProductByProductId(String productId);
}
