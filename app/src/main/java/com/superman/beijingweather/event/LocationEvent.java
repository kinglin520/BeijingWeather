package com.superman.beijingweather.event;

/**
 * Created by wenlin on 2017/4/6.
 */

public class LocationEvent {

    private String county;

    public LocationEvent(String county) {
        this.county = county;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
