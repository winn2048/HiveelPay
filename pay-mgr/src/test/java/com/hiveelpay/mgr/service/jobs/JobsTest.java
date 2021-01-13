package com.hiveelpay.mgr.service.jobs;

import com.hiveelpay.PayMgrApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayMgrApplication.class)
public class JobsTest {
    @Autowired
    private InvoiceJob invoiceJob;

    @Test
    public void generateInvoiceTest() {
        invoiceJob.generateInvoice();
    }
}
