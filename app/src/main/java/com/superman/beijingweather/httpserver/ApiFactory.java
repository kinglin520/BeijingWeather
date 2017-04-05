package com.superman.beijingweather.httpserver;

import com.superman.beijingweather.httpserver.api.GirlsService;
import com.superman.beijingweather.httpserver.api.NewsService;
import com.superman.beijingweather.httpserver.api.WeatherService;

/**
 * Created by wenlin on 2017/2/16.
 */

public class ApiFactory {
    protected static final Object monitor = new Object();

    private static WeatherService weatherService;
    private static GirlsService  girlsService;
    private static NewsService newsService;

    public static WeatherService getWeatherController() {
        if (weatherService == null) {
            synchronized (monitor) {
                weatherService = RetrofitManager.getInstance().create(WeatherService.class);
            }
        }
        return weatherService;
    }

    public static GirlsService getGirlsService(){
        if(girlsService == null){
            synchronized (monitor){
                girlsService = RetrofitManager.getInstance().create(GirlsService.class);
            }
        }
        return girlsService;
    }

    public static NewsService getNewsService(){
        if(newsService == null){
            synchronized (monitor){
                newsService = RetrofitManager.getInstance().create(NewsService.class);
            }
        }
        return newsService;
    }

    public static void reset() {
        weatherService = null;
        girlsService = null;
    }
}
