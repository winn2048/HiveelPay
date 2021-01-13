package com.hiveelpay.boot.service;

import com.hiveelpay.boot.PayBootAppliaction;
import com.hiveelpay.boot.jobs.AppointmentJobs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayBootAppliaction.class)
public class JobsTest {
    @Autowired
    private AppointmentJobs appointmentJobs;


    @Test
    public void test() {

    }
}
