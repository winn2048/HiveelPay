package com.hiveelpay.dal.dao.mapper;

import com.hiveelpay.dal.dao.model.PaymentMethod;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodMapper {

    /**
     * @param paymentMethod
     * @return
     */
    int save(@Param("paymentMethod") PaymentMethod paymentMethod);

    PaymentMethod findByToken(@Param("token") String token, @Param("customerId") String customerId);

    List<PaymentMethod> findCustomerPaymentMethods(@Param("customerId") String customerId);

    PaymentMethod findById(@Param("paymentMethodId") String paymentMethodId);

}
