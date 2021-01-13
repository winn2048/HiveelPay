package com.hiveelpay.boot.service.events;

import com.hiveelpay.dal.dao.model.AppointmentDoc;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

public class TradeInSkipPaymentEvent extends ApplicationEvent implements Serializable {

    private AppointmentDoc appointmentDoc;


    public TradeInSkipPaymentEvent(AppointmentDoc appointment) {
        super(appointment);
        this.appointmentDoc = appointment;
    }

    public AppointmentDoc getAppointmentDoc() {
        return appointmentDoc;
    }

}
