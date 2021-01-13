package com.hiveelpay.boot.ctrl.converters;

import com.hiveelpay.common.model.vo.CreditCardVo;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.model.CreditCard;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class CreditCardToCreditCardVoConverter implements Converter<CreditCard, CreditCardVo> {
    private static final MyLog _log = MyLog.getLog(CreditCardToCreditCardVoConverter.class);

    @Override
    public CreditCardVo convert(CreditCard source) {
        CreditCardVo c = new CreditCardVo();
        c.setCustomerId(source.getCustomerId());
        c.setCardType(source.getCardType());
        c.setExpired(source.isExpired());
        c.setImageUrl(source.getImageUrl());
        c.setIssuingBank(source.getIssuingBank());
        c.setLast4(source.getLast4());
        return c;
    }
}
