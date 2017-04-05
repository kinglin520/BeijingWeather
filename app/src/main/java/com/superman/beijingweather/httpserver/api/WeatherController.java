package com.superman.beijingweather.httpserver.api;

import com.superman.beijingweather.httpserver.ApiFactory;
import com.superman.beijingweather.httpserver.BaseWeatherResponse;
import com.superman.beijingweather.model.weather.Weather;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wenlin on 2017/2/16.
 */

public class WeatherController {
    //http://guolin.tech/api/weather?cityid=CN101010100&key=bc0418b57b2d4918819d3974ac1285d9
    public static final String key = "bc0418b57b2d4918819d3974ac1285d9";
    private static final WeatherService weatherService = ApiFactory.getWeatherController();

    public static Observable<List<Weather>> getWeatherInfo(String cityid) {
        return handThread(weatherService.getWeatherInfo(cityid, key)).map(new Func1<BaseWeatherResponse<List<Weather>>, List<Weather>>() {
            @Override
            public List<Weather> call(BaseWeatherResponse<List<Weather>> listBaseWeatherResponse) {
                return listBaseWeatherResponse.HeWeather;
            }
        });
    }

    public static Observable<String> getBingPic(){
        return handThread(weatherService.getBingPic());
    }

    private static <T> Observable<T> handThread(Observable<T> obs) {
        return obs.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
