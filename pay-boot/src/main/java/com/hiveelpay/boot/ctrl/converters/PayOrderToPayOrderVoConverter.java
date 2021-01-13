package com.hiveelpay.boot.ctrl.converters;

import com.hiveelpay.common.enumm.PaymentMethodTypeEnum;
import com.hiveelpay.common.model.vo.CreditCardVo;
import com.hiveelpay.common.model.vo.PayOrderVo;
import com.hiveelpay.common.model.vo.PaymentMethodVo;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.dal.dao.model.CreditCard;
import com.hiveelpay.dal.dao.model.PayOrder;
import com.hiveelpay.dal.dao.model.PaymentMethod;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyyHHmm;

@Component
public class PayOrderToPayOrderVoConverter implements Converter<PayOrder, PayOrderVo> {
    @Override
    public PayOrderVo convert(PayOrder source) {
        try {
            PayOrderVo pv = new PayOrderVo();
            pv.setPayOrderId(source.getPayOrderId());
            pv.setAmount(source.getAmount());
            pv.setCurrency(source.getCurrency());
            pv.setStatus(source.getStatus());
            pv.setSubject(source.getSubject());
            pv.setExpireTime(source.getExpireTime());
            pv.setPaySuccTime(source.getPaySuccTime());
            pv.setCreateTime(DateUtil.date2Str(source.getCreateTime(), FORMAT_MMddyyyyHHmm));
            pv.setUpdateTime(DateUtil.date2Str(source.getUpdateTime(), FORMAT_MMddyyyyHHmm));
            if (source.getPaymentMethod() != null) {
                PaymentMethod p = source.getPaymentMethod();

                PaymentMethodVo pp = new PaymentMethodVo();
                pp.setToken(p.getToken());
                pp.setImageUrl(p.getImageUrl());
//            pp.setPaymentMethodType(p.getPaymentMethodType().name());

                if (p instanceof CreditCard) {
                    CreditCard c = (CreditCard) p;
                    CreditCardVo creditCardVo = new CreditCardVo();
                    creditCardVo.setToken(c.getToken());
                    pp.setPaymentMethodType(PaymentMethodTypeEnum.CREDITCARD.name());
                    creditCardVo.setPaymentMethodType(PaymentMethodTypeEnum.CREDITCARD.name());
//                    creditCardVo.setPaymentMethodType(c.getPaymentMethodType().name());
                    creditCardVo.setBin(c.getBin());
                    creditCardVo.setLast4(c.getLast4());
                    creditCardVo.setImageUrl(c.getImageUrl());
                    creditCardVo.setCardType(c.getCardType());
                    creditCardVo.setDefault(c.isDefault());
                    creditCardVo.setExpired(c.isExpired());
                    creditCardVo.setExpirationMonth(c.getExpirationMonth());
                    creditCardVo.setExpirationYear(c.getExpirationYear());
                    creditCardVo.setCustomerLocation(c.getCustomerLocation());
                    creditCardVo.setCustomerId(c.getCustomerId());
                    pp.setCreditCard(creditCardVo);
                }
                pv.setPaymentMethod(pp);
            }
            return pv;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
