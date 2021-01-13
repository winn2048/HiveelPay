package com.hiveelpay.boot.service.events;

import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * service end check event
 */
public class CheckServiceEndEvent extends ApplicationEvent implements Serializable {
    private String eventId;

    public CheckServiceEndEvent(String id) {
        super(id);
        this.eventId = id;

    }

    public String getEventId() {
        return eventId;
    }

    @Override
    public String toString() {
        return "CheckServiceEndEvent{" +
                "eventId='" + eventId + '\'' +
                '}';
    }
}
