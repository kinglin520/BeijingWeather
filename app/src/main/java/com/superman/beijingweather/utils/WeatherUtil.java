package com.superman.beijingweather.utils;


import com.google.gson.reflect.TypeToken;
import com.superman.beijingweather.App;
import com.superman.beijingweather.httpserver.RetrofitManager;
import com.superman.beijingweather.model.weather.DetailForecast;
import com.superman.beijingweather.model.weather.Weather;
import com.superman.beijingweather.model.weather.WeatherBean;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class WeatherUtil {

    private static WeatherUtil instance;

    private Map<String, WeatherBean> weatherBeanMap;

    private WeatherUtil() {
        weatherBeanMap = new HashMap<>();
        List<WeatherBean> list = RetrofitManager.gson().fromJson(readFromAssets(),
                new TypeToken<List<WeatherBean>>() {
                }.getType());
        for (WeatherBean bean : list) {
            weatherBeanMap.put(bean.getCode(), bean);
        }
    }

    private static WeatherUtil getInstance() {
        if (instance == null) {
            synchronized (WeatherUtil.class) {
                instance = new WeatherUtil();
            }
        }
        return instance;
    }

    public static Observable<WeatherBean> getWeatherDict(final String code) {
        return Observable.create(new Observable.OnSubscribe<WeatherBean>() {
            @Override
            public void call(Subscriber<? super WeatherBean> subscriber) {
                subscriber.onNext(WeatherUtil.getInstance().weatherBeanMap.get(code));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    private String readFromAssets() {
        try {
            InputStream is = App.getContext().getAssets().open("weather.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveDailyHistory(Weather weather) {
        Observable.just(weather).filter(new Func1<Weather, Boolean>() {
            @Override
            public Boolean call(Weather weather5) {
                return weather5 != null;
            }
        }).map(new Func1<Weather, Boolean>() {
            @Override
            public Boolean call(Weather weather5) {
                ACache mCache = ACache.get(App.getContext());
                for (DetailForecast bean : weather5.forecastList) {
                    mCache.put(bean.getDate(), bean, 7 * 24 * 60 * 60);//每天的情况缓存7天，供后面查询
                }
                return true;
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public static DetailForecast getYesterday() {
        return (DetailForecast) ACache.get(App.getContext())
                .getAsObject(TimeUtils.getPreviousDay(TimeUtils.getCurTimeString(TimeUtils.DATE_SDF), 1));
    }

    public static String getShareMessage(Weather weather) {
        StringBuffer message = new StringBuffer();
        message.append(weather.basic);
        message.append("天气：");
        message.append("\r\n");
        message.append(weather.basic.update.updateTime);
        message.append(" 发布：");
        message.append("\r\n");
        message.append(weather.now.more.info);
        message.append("，");
        message.append(weather.now.temperature + "℃");
        message.append("。");
        message.append("\r\n");
        message.append("PM2.5：" + weather.aqi.city.pm25);
        message.append("，");
        message.append(weather.aqi.city.qlty);
        message.append("。");
        message.append("\r\n");
        message.append("今天：");
        message.append(weather.forecastList.get(0).getTmp().getMin() + "℃-");
        message.append(weather.forecastList.get(0).getTmp().getMax() + "℃");
        message.append("，");
        message.append(weather.forecastList.get(0).getCond().getTxt_d());
        message.append("，");
        message.append(weather.forecastList.get(0).getWind().getDir());
        message.append(weather.forecastList.get(0).getWind().getSc());
        message.append("级。");
        message.append("\r\n");
        message.append("明天：");
        message.append(weather.forecastList.get(1).getTmp().getMin() + "℃-");
        message.append(weather.forecastList.get(1).getTmp().getMax() + "℃");
        message.append("，");
        message.append(weather.forecastList.get(1).getCond().getTxt_d());
        message.append("，");
        message.append(weather.forecastList.get(1).getWind().getDir());
        message.append(weather.forecastList.get(1).getWind().getSc());
        message.append("级。");

        return message.toString();
    }
}
