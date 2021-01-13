package com.hiveelpay.boot.ctrl.converters;

import com.google.common.base.Strings;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.hiveelpay.common.enumm.BilledMethodEnum;
import com.hiveelpay.common.model.vo.ProductVo;
import com.hiveelpay.common.util.AmountUtil;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.model.PayProduct;

@Component
public class PayProductToProductVoConverter implements Converter<PayProduct, ProductVo> {
    private static final MyLog _log = MyLog.getLog(PayProductToProductVoConverter.class);

    @Override
    public ProductVo convert(PayProduct source) {
        ProductVo t = new ProductVo();
        t.setProductId(source.getProductId());
        t.setAmount(AmountUtil.convertCent2Dollar(String.valueOf(source.getAmount())));
        t.setProductName(source.getProductName());
        t.setProductDescription(source.getProductDescription());
        t.setProductType(source.getProductType().getVal());
        if (Strings.isNullOrEmpty(source.getBtPlanId())) {
            t.setBilledMethod(BilledMethodEnum.ONETIME.name());
        } else {
            t.setBilledMethod(BilledMethodEnum.MONTHLY.name());
        }
        return t;
    }
}
