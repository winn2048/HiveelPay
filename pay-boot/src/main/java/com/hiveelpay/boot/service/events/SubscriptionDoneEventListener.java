package com.hiveelpay.boot.service.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.hiveelpay.common.util.MyLog;

@Component
public class SubscriptionDoneEventListener {
    private static final MyLog _log = MyLog.getLog(SubscriptionDoneEventListener.class);

    @EventListener
    public void listen(SubscriptionDoneEvent subscriptionDoneEvent) {
        _log.info("Finished subscription :{}", subscriptionDoneEvent);
        if (!subscriptionDoneEvent.isSubscriptionReq()) {
            _log.warn("{}", subscriptionDoneEvent);
            return;
        }

        // H_SUBSCRIPTION success
    }
}
