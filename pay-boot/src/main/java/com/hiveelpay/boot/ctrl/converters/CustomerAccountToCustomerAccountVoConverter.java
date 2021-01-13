package com.hiveelpay.boot.ctrl.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.hiveelpay.common.model.vo.CustomerAccountVo;
import com.hiveelpay.dal.dao.model.CustomerAccount;

@Component
public class CustomerAccountToCustomerAccountVoConverter implements Converter<CustomerAccount, CustomerAccountVo> {

    @Override
    public CustomerAccountVo convert(CustomerAccount source) {
        CustomerAccountVo ca = new CustomerAccountVo();
        ca.setUserId(source.getUserId());
        ca.setCustomerId(source.getCustomerId());
        ca.setFirstName(source.getFirstName());
        ca.setLastName(source.getLastName());
        ca.setCreateAt(source.getCreateAt());
        ca.setLastUpdateAt(source.getLastUpdateAt());

        return ca;
    }
}
