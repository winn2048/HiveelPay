package com.hiveelpay.dal.dao.mapper;

import com.hiveelpay.dal.dao.model.CreditCard;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardMapper {
    /**
     * @param creditCard
     * @return
     */
    int save(@Param("creditCard") CreditCard creditCard);

    /**
     * @param token
     * @param customerId
     * @return
     */
    CreditCard findByToken(@Param("token") String token, @Param("customerId") String customerId);
}
