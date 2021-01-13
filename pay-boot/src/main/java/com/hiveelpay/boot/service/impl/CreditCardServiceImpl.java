package com.hiveelpay.boot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hiveelpay.boot.service.CreditCardService;
import com.hiveelpay.dal.dao.mapper.CreditCardMapper;
import com.hiveelpay.dal.dao.mapper.CustomerAccountMapper;

@Service
public class CreditCardServiceImpl implements CreditCardService {
    @Autowired
    private CreditCardMapper creditCardMapper;
    @Autowired
    private CustomerAccountMapper customerAccountMapper;


}
