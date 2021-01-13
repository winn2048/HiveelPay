package com.hiveelpay.common.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class CarServicesProperty implements Serializable {
    @Expose
    @SerializedName(value = "startDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMddyyyyHHmmss", timezone ="America/Los_Angeles")
    protected Date startDate = null;

    @Expose
    @SerializedName(value = "endDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMddyyyyHHmmss", timezone ="America/Los_Angeles")
    protected Date endDate = null;

    public CarServicesProperty() {
//        Calendar c = Calendar.getInstance();
//        c.set(1900, 1, 1);
//        Date d = c.getTime();
//        this.startDate = d;
//        this.endDate = d;
    }

    public CarServicesProperty(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "CarServicesProperty{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
