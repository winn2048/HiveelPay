package com.hiveelpay.boot.service;

import com.hiveelpay.common.enumm.CustomerServiceStatusEnum;
import com.hiveelpay.common.enumm.PayProductTypeEnum;
import com.hiveelpay.dal.dao.model.BizOrder;
import com.hiveelpay.dal.dao.model.CustomerValidServices;
import com.hiveelpay.dal.dao.model.PayProduct;

import java.util.List;
import java.util.Set;

public interface CustomerValidServiceService {
    void mergeService(String customerId);

    CustomerValidServices findCurrentMemberShipService(String customerId);

    List<CustomerValidServices> findByCustomerId(String customerId, PayProductTypeEnum payProductType, Set<CustomerServiceStatusEnum> statusEnumSet);

    List<CustomerValidServices> createCustomerService(BizOrder bizOrder, PayProduct payProduct);

    List<CustomerValidServices> findByCustomerIdIncludeProduct(String customerId, PayProductTypeEnum productType, Set<CustomerServiceStatusEnum> statusEnumSet);
}
