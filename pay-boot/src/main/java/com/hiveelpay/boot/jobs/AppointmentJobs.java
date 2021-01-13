package com.hiveelpay.boot.jobs;

import com.hiveelpay.boot.service.AppointmentDocService;
import com.hiveelpay.common.util.MyLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AppointmentJobs {
    private static final MyLog _log = MyLog.getLog(AppointmentJobs.class);
    @Autowired
    private AppointmentDocService appointmentDocServiceImpl;

    /**
     * 预约时间到达前24小时内，失效未支付的预约单
     */
    @Async
    @Scheduled(cron = "0 0,30 * * * ?")// 在0和30分的时候启动执行
    public void invalidAppointment() {
        appointmentDocServiceImpl.invalidAppointment();
    }

    /**
     * 预约时间到达前24小时内，失效未支付的预约单
     */
    @Async
    @Scheduled(cron = "0 1,31 * * * ?")// 在1和31分的时候启动执行
    public void notifyUser() {
        appointmentDocServiceImpl.notifyUserServiceStart();
    }

    @Async
    @Scheduled(cron = "0 1 0/1 * * ?")// 每小时执行一次
    public void sayGoodByToUser() {
        appointmentDocServiceImpl.sayGoodByToUser();
    }

    /**
     * 预约时间已过1天，设置为服务完成
     */
    @Async
    @Scheduled(cron = "0 0 1,2,3 * * ?")//每天的1,2,3点执行
    public void appointmentServiceDone() {
        appointmentDocServiceImpl.serviceDone(1);
    }
}
