package com.hiveelpay.common.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class CarServicesCarOfDayProperty extends CarServicesProperty implements Serializable {

    @Expose
    @SerializedName(value = "zipcode")
    private String zipcode;

    @Expose
    @SerializedName(value = "geo")
    private HGeo geo;

    public CarServicesCarOfDayProperty() {
//        this.zipcode = "";
//        this.geo = new HGeo();
//        Calendar c = Calendar.getInstance();
//        c.set(1900, 1, 1);
//        Date d = c.getTime();
//        this.startDate = d;
//        this.endDate = d;
    }

    public CarServicesCarOfDayProperty(Date startDate, Date endDate) {
        super(startDate, endDate);
    }

    public CarServicesCarOfDayProperty(String zipcode, Date startDate, Date endDate) {
        super(startDate, endDate);
        this.zipcode = zipcode;
    }

    public CarServicesCarOfDayProperty(HGeo hGeo, String zipcode, Date startDate, Date endDate) {
        super(startDate, endDate);
        this.zipcode = zipcode;
        this.geo = hGeo;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public HGeo getGeo() {
        return geo;
    }

    public void setGeo(HGeo geo) {
        this.geo = geo;
    }

    @Override
    public String toString() {
        return "CarServicesCarOfDayProperty{" +
                "zipcode='" + zipcode + '\'' +
                ", geo=" + geo +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
