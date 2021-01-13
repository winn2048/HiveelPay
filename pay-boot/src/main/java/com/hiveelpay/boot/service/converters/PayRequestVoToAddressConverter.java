package com.hiveelpay.boot.service.converters;

import com.hiveelpay.common.util.MySeq;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.hiveelpay.common.enumm.AddressType;
import com.hiveelpay.common.model.vo.PayRequestVo;
import com.hiveelpay.common.util.HiveelID;
import com.hiveelpay.dal.dao.model.Address;

@Component
public class PayRequestVoToAddressConverter implements Converter<PayRequestVo, Address> {
    @Override
    public Address convert(PayRequestVo payRequestVo) {
        if (payRequestVo == null) {
            return null;
        }
        Address address = new Address();
        String addressId = MySeq.getAppointmentId();//new address

        address.setAddressId(addressId);//save address
//        address.setCustomerId(customerId);
        address.setAddressType(payRequestVo.getAddressType() == null ? AddressType.billing : payRequestVo.getAddressType());
        address.setFirstName(payRequestVo.getFirstName().trim());
        address.setLastName(payRequestVo.getLastName().trim());

        address.setStreetAddress(payRequestVo.getAddr().trim());
        address.setExtendedAddress(payRequestVo.getAddrSuite());
        address.setLocality(payRequestVo.getCity().trim());
        address.setPostalCode(payRequestVo.getZipcode().trim());
        address.setRegion(payRequestVo.getState().trim());

        address.setCountryName("United States of America");
        address.setCountryCodeAlpha2("US");
        address.setCountryCodeAlpha3("USA");
        address.setCountryCodeNumeric("840");//United States of America

        return address;
    }
}
