package com.hiveelpay.mgr.controller;

import com.hiveelpay.mgr.service.jobs.InvoiceJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceJob invoiceJob;

    @GetMapping("/generate")
    public String generate() {

        invoiceJob.generateInvoice();
        invoiceJob.generateInvoice2();

        return "success";
    }


}
