package com.hiveelpay.common.model.response;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class HGeo implements Serializable {

    @Expose
    private Double lon = 0d;//
    @Expose
    private Double lat = 0d;//纬度

    public HGeo() {

    }

    public HGeo(Double lon, Double lat) {
        if (lon == null || lat == null) {
            throw new IllegalArgumentException("lon and lat can not be null.");
        }
        this.lon = lon;
        this.lat = lat;
    }

    public HGeo(String lon, String lat) {
        this.lon = Double.parseDouble(lon);
        this.lat = Double.parseDouble(lat);
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public boolean isCorrect() {
        if (lon == null || lat == null) {
            return false;
        }
        return (lon >= -180 || lon <= 180) && (lat >= -90 && lat <= 90);
    }

    @Override
    public String toString() {
        return "HGeo{" +
                "lon=" + lon +
                ", lat=" + lat +
                '}';
    }
}
