package com.hiveelpay.boot.service;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

public class SystemHasExceptionEvent extends ApplicationEvent implements Serializable {
    private String subject;
    private Throwable throwable;
    private String requestString;


    public SystemHasExceptionEvent(String subject, Throwable throwable, String requestString) {
        super(throwable);
        this.subject = subject;
        this.throwable = throwable;
        this.requestString = requestString;
    }

    public String getSubject() {
        return subject;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getRequestString() {
        return requestString;
    }


    @Override
    public String toString() {
        return "SystemHasExceptionEvent{" +
                "subject='" + subject + '\'' +
                ", throwable=" + throwable +
                ", requestString=" + requestString +
                '}';
    }
}
