package com.hiveelpay.boot.service;

import com.hiveelpay.dal.dao.model.Address;
import com.hiveelpay.dal.dao.model.CustomerAccount;
import com.hiveelpay.dal.dao.model.CustomerValidServices;
import com.hiveelpay.dal.dao.model.PaymentMethod;

import java.util.List;

public interface CustomerService {

    void createAccount(CustomerAccount account, Address address);

    CustomerAccount findCustomerByUserId(String userId);

    List<PaymentMethod> findPaymentMethods(String customerId);

    List<CustomerValidServices> findValidServices(String customerId);

    Boolean checkUserBuy(String productId, String userId);
}
