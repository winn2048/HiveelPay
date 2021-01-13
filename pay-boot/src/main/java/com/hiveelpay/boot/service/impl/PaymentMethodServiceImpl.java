package com.hiveelpay.boot.service.impl;

import com.hiveelpay.boot.service.PaymentMethodService;
import com.hiveelpay.common.enumm.PaymentMethodTypeEnum;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.mapper.CreditCardMapper;
import com.hiveelpay.dal.dao.mapper.PaymentMethodMapper;
import com.hiveelpay.dal.dao.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private static final MyLog _log = MyLog.getLog(PaymentMethodServiceImpl.class);


    @Autowired
    private PaymentMethodMapper paymentMethodMapper;
    @Autowired
    private CreditCardMapper creditCardMapper;

    @Override
    public PaymentMethod findPaymentMethodById(String paymentMethodId) {
        PaymentMethod paymentMethod = paymentMethodMapper.findById(paymentMethodId);
        if (paymentMethod != null) {
            if (paymentMethod.getPaymentMethodType().equals(PaymentMethodTypeEnum.CREDITCARD)) {
                return creditCardMapper.findByToken(paymentMethod.getToken(), paymentMethod.getCustomerId());
            }
        }
        return null;
    }
}
