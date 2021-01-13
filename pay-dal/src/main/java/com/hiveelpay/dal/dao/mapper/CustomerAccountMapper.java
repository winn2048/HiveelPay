package com.hiveelpay.dal.dao.mapper;

import com.hiveelpay.dal.dao.model.CustomerAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAccountMapper {
    int save(@Param("customerAccount") CustomerAccount customerAccount);

    CustomerAccount findByUserId(@Param("userId") String userId);

    int updateCustomerAccount(@Param("customerAccount") CustomerAccount customerAccount);

    String findCustomerIdbyUserId(@Param("userId") String userId);

}
