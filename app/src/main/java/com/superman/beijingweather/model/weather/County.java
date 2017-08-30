package com.superman.beijingweather.model.weather;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Id;
import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by wenlin on 2017/4/6.
 */
public class County extends DataSupport implements Serializable {

    @SerializedName("name")
    private String countyName;
    @Column(unique = true, defaultValue = "unknown")
    @SerializedName("weather_id")
    @Id
    private String weatherId;

    private int cityId;

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}

