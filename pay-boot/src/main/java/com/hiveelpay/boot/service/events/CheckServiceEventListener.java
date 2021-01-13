package com.hiveelpay.boot.service.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.hiveelpay.boot.service.BizOrderService;
import com.hiveelpay.common.util.MyLog;

@Component
public class CheckServiceEventListener {
    private static final MyLog _log = MyLog.getLog(CheckServiceEventListener.class);
    @Autowired
    private BizOrderService bizOrderServiceImpl;


    @Async
    @EventListener
    public void listenCheckNewService(CheckNewServiceEvent checkNewServiceEvent) {
//        bizOrderServiceImpl.updateToInservice();
    }

//    @Async
    @EventListener
    public void listenCheckNewService(CheckServiceEndEvent checkServiceEndEvent) {
        bizOrderServiceImpl.checkAndEndService();
    }


}
