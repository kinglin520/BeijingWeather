package com.superman.beijingweather.model.weather;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Weather implements MultiItemEntity ,Cloneable , Serializable{

    public  static final int TYPE_BASIC = 4;

    public  static final int TYPE_NOW = 1;

    public  static final int TYPE_AQI = 2;

    public  static final int TYPE_SUGGESTION = 3;

    public static final int TYPE_DETAILFORECAST = 5;

    public static final int TYPE_HOURFORECAST = 6;

    public static final int TYPE_NOWDETAIL = 7;

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    private int itemType = 0 ;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @SerializedName("daily_forecast")
    public List<DetailForecast> forecastList;

    @SerializedName("hourly_forecast")
    public List<HourForecast> hourForecastList;

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public Object clone() {
        Weather o = null;
        try {
            o = (Weather) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}
