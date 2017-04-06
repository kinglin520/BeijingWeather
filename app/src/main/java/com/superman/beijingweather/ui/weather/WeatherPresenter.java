package com.superman.beijingweather.ui.weather;

/**
 * Created by wenlin on 2017/2/21.
 */

public interface WeatherPresenter {
    public void getWeatherInfo(String cityid);

    public void getBingPic();

    public void getBeijingCounties();

    public void getShanghaiCounties();
}
