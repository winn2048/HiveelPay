package com.hiveelpay.boot.service;

import com.hiveelpay.dal.dao.model.PaymentMethod;

public interface PaymentMethodService {
    PaymentMethod findPaymentMethodById(String paymentMethodId);
}
