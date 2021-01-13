package com.hiveelpay.boot.service.converters;

import com.braintreegateway.CreditCard;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BrainTreeCreditCardToHiveelCreditCardConverter implements Converter<CreditCard, com.hiveelpay.dal.dao.model.CreditCard> {

    @Override
    public com.hiveelpay.dal.dao.model.CreditCard convert(CreditCard source) {
        com.hiveelpay.dal.dao.model.CreditCard cc = new com.hiveelpay.dal.dao.model.CreditCard();
        cc.setToken(source.getToken());//id

        cc.setBin(source.getBin());
        cc.setCardType(source.getCardType());
        cc.setIssuingBank(source.getIssuingBank());
        cc.setCardholderName(source.getCardholderName());
        cc.setLast4(source.getLast4());
        cc.setImageUrl(source.getImageUrl());
        cc.setCustomerLocation(source.getCustomerLocation());
        cc.setExpirationMonth(source.getExpirationMonth());
        cc.setExpirationYear(source.getExpirationYear());
        cc.setExpired(source.isExpired());
        cc.setDefault(source.isDefault());
        cc.setVenmoSdk(source.isVenmoSdk());
        cc.setCommercial(source.getCommercial().name());
        cc.setDebit(source.getDebit().name());
        cc.setDurbinRegulated(source.getDurbinRegulated().name());
        cc.setHealthcare(source.getHealthcare().name());
        cc.setPayroll(source.getPayroll().name());
        cc.setPrepaid(source.getPrepaid().name());
        cc.setProductId(source.getProductId());
        cc.setCountryOfIssuance(source.getCountryOfIssuance());
        cc.setUniqueNumberIdentifier(source.getUniqueNumberIdentifier());
        cc.setCreateAt(source.getCreatedAt().getTime());
        cc.setLastUpdateAt(source.getUpdatedAt().getTime());

        return cc;
    }
}
