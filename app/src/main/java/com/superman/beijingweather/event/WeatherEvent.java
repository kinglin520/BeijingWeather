package com.superman.beijingweather.event;

import com.superman.beijingweather.model.weather.Weather;

/**
 * Created by wenlin on 2017/2/24.
 */

public class WeatherEvent {
    public static final String BEIJING = "CN101010100";
    public static final String SHANGHAI = "CN101020100";

    private String from;
    private Weather weather;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public WeatherEvent(String from, Weather weather) {
        this.from = from;
        this.weather = weather;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}
