package com.superman.beijingweather.ui.weather;

import com.superman.beijingweather.model.weather.Weather;

/**
 * Created by wenlin on 2017/2/21.
 */

public interface WeatherView  {
    public void refreshWeatherInfo(Weather weather);
    public void getWeatherError();
    public void bingPic(String url);
}
