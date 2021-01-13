package com.hiveelpay.boot.ctrl.converters;

import com.google.common.base.Strings;
import com.hiveelpay.common.model.vo.InvoiceVo;
import com.hiveelpay.common.util.AmountUtil;
import com.hiveelpay.common.util.DateUtil;
import com.hiveelpay.dal.dao.model.Invoice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.hiveelpay.common.util.DateUtil.FORMAT_MMddyyyyHHmm;

@Component
public class InvoiceVoToInvoiceConverter implements Converter<InvoiceVo, Invoice> {
    @Override
    public Invoice convert(InvoiceVo source) {
        Invoice v = new Invoice();
        v.setInvoiceId(source.getInvoiceId());
        if (!Strings.isNullOrEmpty(source.getMchId())) {
            v.setMchId(source.getMchId());
        }
        if (!Strings.isNullOrEmpty(source.getDateFrom())) {
            v.setDateFrom(DateUtil.strToDate(source.getDateFrom(), FORMAT_MMddyyyyHHmm));
        }
        if (!Strings.isNullOrEmpty(source.getDateTo())) {
            v.setDateTo(DateUtil.strToDate(source.getDateTo(), FORMAT_MMddyyyyHHmm));
        }
        if (source.getSettledDate() != null) {
            v.setSettledDate(DateUtil.strToDate(source.getSettledDate(), FORMAT_MMddyyyyHHmm));
        }
        v.setSettledAmount(Long.valueOf(AmountUtil.convertDollar2Cent(source.getSettledAmount())));
        v.setRemark(Strings.nullToEmpty(source.getRemark()));
        return v;
    }
}
