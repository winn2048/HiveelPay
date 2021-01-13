package com.hiveelpay.boot.service.events;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * new service check event
 */
public class CheckNewServiceEvent extends ApplicationEvent implements Serializable {
    public CheckNewServiceEvent(String id) {
        super(id);
    }
}
