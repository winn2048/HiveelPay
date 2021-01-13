package com.hiveelpay.boot.ctrl.converters;

import com.google.common.base.Strings;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.hiveelpay.boot.ctrl.exceptions.HiveelPayErrorCode;
import com.hiveelpay.common.enumm.PaymentMethodTypeEnum;
import com.hiveelpay.common.exceptions.HiveelPayException;
import com.hiveelpay.common.model.vo.AddressVo;
import com.hiveelpay.common.model.vo.CreditCardVo;
import com.hiveelpay.common.model.vo.PaymentMethodVo;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.model.Address;
import com.hiveelpay.dal.dao.model.CreditCard;
import com.hiveelpay.dal.dao.model.PaymentMethod;

@Component
public class PaymentMethodToPaymentMethodVoConverter implements Converter<PaymentMethod, PaymentMethodVo> {
    private static final MyLog _log = MyLog.getLog(PaymentMethodToPaymentMethodVoConverter.class);

    @Override
    public PaymentMethodVo convert(PaymentMethod paymentMethod) {
        try {
            PaymentMethodVo pm = new PaymentMethodVo();
            pm.setCustomerId(paymentMethod.getCustomerId());
            pm.setToken(paymentMethod.getToken());
            pm.setDefault(paymentMethod.isDefault());
            pm.setImageUrl(paymentMethod.getImageUrl());
            pm.setPaymentMethodType(paymentMethod.getPaymentMethodType().name());

            if (paymentMethod.getPaymentMethodType().equals(PaymentMethodTypeEnum.CREDITCARD) && (paymentMethod.getCreditCard() != null)) {
                CreditCard source = paymentMethod.getCreditCard();
                CreditCardVo c = new CreditCardVo();
                c.setToken(source.getToken());
                c.setBin(source.getBin());
                c.setLast4(source.getLast4());
//                c.setCustomerId(source.getCustomerId());
                c.setCardType(source.getCardType());
                c.setExpired(source.isExpired());
                c.setImageUrl(source.getImageUrl());
                c.setIssuingBank(source.getIssuingBank());
                c.setLast4(source.getLast4());
                c.setExpired(source.isExpired());
                c.setExpirationMonth(source.getExpirationMonth());
                c.setExpirationYear(source.getExpirationYear());
                c.setCommercial(source.getCommercial());
                c.setCardholderName(Strings.nullToEmpty(source.getCardholderName()));
//                c.setPaymentMethodType(paymentMethod.getPaymentMethodType().name());


                if (source.getAddress() != null) {
                    AddressVo addressVo = convertAddressToAddressVo(source.getAddress());
                    c.setAddress(addressVo);
                }
                pm.setCreditCard(c);
            }

            return pm;
        } catch (Exception e) {
            e.printStackTrace();
            _log.error("", e);
            throw new HiveelPayException(HiveelPayErrorCode.CONVERTER_ERROR, e);
        }
    }

    private AddressVo convertAddressToAddressVo(Address address) {
        AddressVo av = new AddressVo();
        av.setAddressId(address.getAddressId());
        av.setCustomerId(address.getCustomerId());
        av.setFirstName(address.getFirstName());
        av.setLastName(address.getLastName());
        av.setCompany(address.getCompany());
        av.setAddressTyp(address.getAddressType().name());
        av.setStreetAddress(address.getStreetAddress());
        av.setLocality(address.getLocality());
        av.setRegion(address.getRegion());
        av.setPostalCode(address.getPostalCode());
        av.setCountryCodeAlpha2(address.getCountryCodeAlpha2());
        av.setCountryCodeAlpha3(address.getCountryCodeAlpha3());
        av.setCountryCodeNumeric(address.getCountryCodeNumeric());
        av.setCountryName(address.getCountryName());
        av.setExtendedAddress(address.getExtendedAddress());
        return av;
    }
}
