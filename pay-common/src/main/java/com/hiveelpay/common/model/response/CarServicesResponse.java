package com.hiveelpay.common.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 */
public class CarServicesResponse implements Serializable {
    @Expose
    @SerializedName(value = "carId")
    private String carId;
    @Expose
    @SerializedName(value = "carOfDay")
    private CarServicesCarOfDayProperty carOfDay;
    @Expose
    @SerializedName(value = "advancing")
    private CarServicesProperty advancing;
    @Expose
    @SerializedName(value = "searchResult")
    private CarServicesProperty searchResult;
    @Expose
    @SerializedName(value = "highlighting")
    private CarServicesProperty highlighting;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public CarServicesCarOfDayProperty getCarOfDay() {
        return carOfDay;
    }

    public void setCarOfDay(CarServicesCarOfDayProperty carOfDay) {
        this.carOfDay = carOfDay;
    }

    public CarServicesProperty getAdvancing() {
        return advancing;
    }

    public void setAdvancing(CarServicesProperty advancing) {
        this.advancing = advancing;
    }

    public CarServicesProperty getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(CarServicesProperty searchResult) {
        this.searchResult = searchResult;
    }

    public CarServicesProperty getHighlighting() {
        return highlighting;
    }

    public void setHighlighting(CarServicesProperty highlighting) {
        this.highlighting = highlighting;
    }

    @Override
    public String toString() {
        return "CarServicesResponse{" +
                "carId='" + carId + '\'' +
                ", carOfDay=" + carOfDay +
                ", advancing=" + advancing +
                ", searchResult=" + searchResult +
                ", highlighting=" + highlighting +
                '}';
    }
}
