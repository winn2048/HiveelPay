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
public class InvoiceToInvoiceVoConverter implements Converter<Invoice, InvoiceVo> {

    @Override
    public InvoiceVo convert(Invoice source) {
        InvoiceVo iv = new InvoiceVo();
        iv.setInvoiceId(source.getInvoiceId());
        iv.setMchId(source.getMchId());
        iv.setDateFrom(DateUtil.date2Str(source.getDateFrom(), FORMAT_MMddyyyyHHmm));
        iv.setDateTo(DateUtil.date2Str(source.getDateTo(), FORMAT_MMddyyyyHHmm));
        if (source.getSettledDate() != null) {
            iv.setSettledDate(DateUtil.date2Str(source.getSettledDate(), FORMAT_MMddyyyyHHmm));
        }
        iv.setCreateAt(DateUtil.date2Str(source.getCreateAt(), FORMAT_MMddyyyyHHmm));
        iv.setLastUpdateAt(DateUtil.date2Str(source.getLastUpdateAt(), FORMAT_MMddyyyyHHmm));
        iv.setInvoiceStatus(source.getInvoiceStatus().name());
        iv.setInvoiceAmount(AmountUtil.convertCent2Dollar(String.valueOf(source.getInvoiceAmount())));
        iv.setSettledAmount(AmountUtil.convertCent2Dollar(String.valueOf(source.getSettledAmount())));
        iv.setRemark(Strings.nullToEmpty(source.getRemark()));
        iv.setTotalRecords(source.getTotalRecords());
        iv.setCommissionAmount(AmountUtil.convertCent2Dollar(String.valueOf(source.getCommissionAmount())));
        iv.setRemark(source.getRemark());
        return iv;
    }
}
