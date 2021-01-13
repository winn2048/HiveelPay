package com.hiveelpay.boot.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.hiveelpay.boot.PayBootAppliaction;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayBootAppliaction.class)
public class CustomerValidServiceServiceTest {

    @Autowired
    private CustomerValidServiceService customerValidServiceService;

    @Test
    public void mergeServiceTest() {
        customerValidServiceService.mergeService("Ca518cb540039412598cb540039112513");
    }

}
