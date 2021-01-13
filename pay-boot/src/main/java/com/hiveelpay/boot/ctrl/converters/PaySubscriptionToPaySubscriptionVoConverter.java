package com.hiveelpay.boot.ctrl.converters;

import com.hiveelpay.common.model.vo.PaySubscriptionVo;
import com.hiveelpay.common.util.AmountUtil;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.dal.dao.model.PaySubscription;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyyHHmm;

/**
 *
 */
@Component
public class PaySubscriptionToPaySubscriptionVoConverter implements Converter<PaySubscription, PaySubscriptionVo> {

    @Override
    public PaySubscriptionVo convert(PaySubscription paySubscription) {
        PaySubscriptionVo ps = new PaySubscriptionVo();
        ps.setSubscriptionId(paySubscription.getSubscriptionId());
        ps.setAmount(AmountUtil.convertCent2Dollar(String.valueOf(paySubscription.getAmount())));
        ps.setCreateAt(DateUtil.date2Str(paySubscription.getCreateAt(), FORMAT_MMddyyyyHHmm));
        ps.setLastUpdateAt(DateUtil.date2Str(paySubscription.getLastUpdateAt(), FORMAT_MMddyyyyHHmm));
        ps.setPlanId(paySubscription.getPlanId());
        ps.setStatus(paySubscription.getStatus());
        return ps;
    }
}
