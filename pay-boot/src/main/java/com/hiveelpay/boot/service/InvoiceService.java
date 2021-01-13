package com.hiveelpay.boot.service;

import com.hiveelpay.common.model.HiveelPage;
import com.hiveelpay.common.model.requests.InvoiceSearchRequest;
import com.hiveelpay.dal.dao.model.Invoice;

import java.util.List;

public interface InvoiceService {
    List<Invoice> searchInvoice(InvoiceSearchRequest searchRequest, HiveelPage page);

    Invoice findByInvoiceId(String invoiceId);

    boolean settleInvoice(Invoice invoice);
}
