package com.superman.beijingweather.ui.weather;

import com.superman.beijingweather.httpserver.api.WeatherController;
import com.superman.beijingweather.model.weather.County;
import com.superman.beijingweather.model.weather.Weather;
import com.superman.beijingweather.ui.BaseSubscriber;
import com.superman.beijingweather.utils.HttpUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wenlin on 2017/2/21.
 */

public class WeatherPresenterImpl implements WeatherPresenter {

    private WeatherView weatherView;

    public WeatherPresenterImpl(WeatherView weatherView) {
        this.weatherView = weatherView;
    }

    @Override
    public void getWeatherInfo(String cityid) {
        WeatherController.getWeatherInfo(cityid).subscribe(new WeatherSubscriber());
    }

    @Override
    public void getBingPic() {
//        WeatherController.getBingPic().subscribe(new BingPicSubscriber());
        loadBingPic();
    }

    @Override
    public void getBeijingCounties() {
        WeatherController.getBeijingCities().subscribe(new CountiesSubscriber());
    }

    @Override
    public void getShanghaiCounties() {
        WeatherController.getShanghaiCities().subscribe(new CountiesSubscriber());
    }



    /**
     * 加载必应每日一图
     */

    private void loadBingPic() {
        final String requestBingPic = "http://guolin.tech/api/bing_pic";
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String bingPic = response.body().string();
                        subscriber.onNext(bingPic);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new BingPicSubscriber());
    }
    private class WeatherSubscriber extends BaseSubscriber{
        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            weatherView.getWeatherError();
        }
        @Override
        public void onNext(Object o) {
            super.onNext(o);
            weatherView.refreshWeatherInfo(((List<Weather>) o).get(0));
        }
    }

    private class BingPicSubscriber extends BaseSubscriber{
        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Object o) {
            super.onNext(o);
            weatherView.bingPic((String) o);
        }
    }

    private class CountiesSubscriber extends BaseSubscriber<List<County>>{
        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(List<County> o) {
            super.onNext(o);
            weatherView.setCounties(o);
        }
    }
}
