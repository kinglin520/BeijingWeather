package com.superman.beijingweather.model.weather;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Basic implements Serializable {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update implements Serializable {

        @SerializedName("loc")
        public String updateTime;

    }

}
