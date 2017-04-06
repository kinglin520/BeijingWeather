package com.superman.beijingweather.httpserver.api;

import com.superman.beijingweather.httpserver.BaseWeatherResponse;
import com.superman.beijingweather.model.weather.County;
import com.superman.beijingweather.model.weather.Weather;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 天气查询接口
 * Created by wenlin on 2017/2/16.
 */

public interface WeatherService {
    @GET("http://guolin.tech/api/weather?")
    Observable<BaseWeatherResponse<List<Weather>>> getWeatherInfo(@Query("cityid") String page , @Query("key") String key);
    @GET("http://guolin.tech/api/bing_pic?")
    Observable<String> getBingPic();
    @GET("http://guolin.tech/api/china/1/1")
    Observable<List<County>> getBeijingCities();
    @GET("http://guolin.tech/api/china/1/2")
    Observable<List<County>> getShanghaiCities();
}
