package com.hiveelpay.boot.service.impl;

import com.google.common.base.Strings;
import com.hiveelpay.boot.service.InvoiceService;
import com.hiveelpay.common.enumm.InvoiceStatusEnum;
import com.hiveelpay.common.model.HiveelPage;
import com.hiveelpay.common.model.requests.InvoiceSearchRequest;
import com.hiveelpay.common.util.MyLog;
import com.hiveelpay.dal.dao.mapper.AppointmentDocMapper;
import com.hiveelpay.dal.dao.mapper.InvoiceMapper;
import com.hiveelpay.dal.dao.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private static final MyLog _log = MyLog.getLog(InvoiceServiceImpl.class);

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private AppointmentDocMapper appointmentDocMapper;

    @Override
    public List<Invoice> searchInvoice(InvoiceSearchRequest searchRequest, HiveelPage page) {
        if (page == null) {
            page = new HiveelPage();
        }
        if (page.getCurrentPage() == 1) {
            page.setTotal(invoiceMapper.searchCount(searchRequest));
        }
        if (page.getTotal() <= 0) {
            return Collections.emptyList();
        }
        List<Invoice> list = invoiceMapper.searchInvoice(searchRequest, page);
        list.forEach(i -> i.setTotalRecords(appointmentDocMapper.countWithInvoiceId(i.getInvoiceId())));
        return list;
    }

    @Override
    public Invoice findByInvoiceId(String invoiceId) {
        if (Strings.isNullOrEmpty(invoiceId)) {
            return null;
        }
        return invoiceMapper.queryByInvoiceId(invoiceId);
    }

    @Override
    public boolean settleInvoice(Invoice invoice) {
        if (invoice == null) {
            return false;
        }
        invoice.setInvoiceStatus(InvoiceStatusEnum.SETTLED);
        if (invoice.getSettledDate() == null) {
            invoice.setSettledDate(new Date());
        }
        int i = invoiceMapper.updateInvoice(invoice, InvoiceStatusEnum.NEW_SAVED);
        return i == 1;
    }
}
