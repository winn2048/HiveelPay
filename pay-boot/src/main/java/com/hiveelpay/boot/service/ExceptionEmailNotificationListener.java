package com.hiveelpay.boot.service;

import com.hiveelpay.boot.service.impl.EmailComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ExceptionEmailNotificationListener {
    private static final Logger log = LoggerFactory.getLogger(ExceptionEmailNotificationListener.class);

    @Autowired
    private EmailComponent emailComponent;


    @Async
    @EventListener
    public void notification(SystemHasExceptionEvent event) {
        try {
            emailComponent.postExceptionEmail(event.getSubject(), event.getThrowable(), event.getRequestString());
        } catch (Exception e) {
            log.error("This exception can be ignored.", e);
        }
    }
}
