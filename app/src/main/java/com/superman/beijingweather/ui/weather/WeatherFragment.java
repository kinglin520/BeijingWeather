package com.superman.beijingweather.ui.weather;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.superman.beijingweather.R;
import com.superman.beijingweather.event.WeatherEvent;
import com.superman.beijingweather.model.weather.Suggestion;
import com.superman.beijingweather.model.weather.Weather;
import com.superman.beijingweather.ui.base.BaseContentFragment;
import com.superman.beijingweather.ui.weather.adapter.WeatherAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wenlin on 2017/2/20.
 */

public class WeatherFragment extends BaseContentFragment implements WeatherView {


    @BindView(R.id.bing_pic_img)
    ImageView bingPicImg;
    @BindView(R.id.rv_weather)
    RecyclerView rvWeather;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    /**
     * 北京
     */
    private String cityid = "CN101010100";

    public static String CITYID = "cityid";

    private WeatherPresenter presenter;

    private WeatherAdapter adapter;

    public static WeatherFragment getInstance(Bundle bundle) {
        WeatherFragment weatherFragment = new WeatherFragment();
        weatherFragment.setArguments(bundle);
        return weatherFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.forecast_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        cityid = getArguments().getString(CITYID);

        presenter = new WeatherPresenterImpl(this);

        initRecyler();
    }

    private void initRecyler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new WeatherAdapter(null, getActivity());
        rvWeather.setLayoutManager(layoutManager);
        adapter.openLoadAnimation();
        rvWeather.setAdapter(adapter);
    }

    @Override
    protected void lazyFetchData() {
        getWeather();
    }

    private void getWeather() {
        showRefreshing(true);
        presenter.getWeatherInfo(cityid);
        presenter.getBingPic();
    }

    @Override
    public void refreshWeatherInfo(Weather weather) {
        showRefreshing(false);
        if(weather == null){
            return;
        }
        List<MultiItemEntity> weathers = new ArrayList<>();

//        Weather hourForecast = (Weather)weather.clone();
//        hourForecast.setItemType(Weather.TYPE_NOW);
//        weathers.add(hourForecast);

//        AQI aqi = weather.aqi;
//        aqi.setItemType(Weather.TYPE_AQI);
//        weathers.add(aqi);

        Weather forecast = (Weather) weather.clone();
        forecast.setItemType(Weather.TYPE_DETAILFORECAST);
        weathers.add(forecast);

        Suggestion suggestion = weather.suggestion;
        suggestion.setItemType(Weather.TYPE_SUGGESTION);
        weathers.add(suggestion);

        Weather nowDetail = (Weather) weather.clone();
        nowDetail.setItemType(Weather.TYPE_NOWDETAIL);
        weathers.add(nowDetail);

        adapter.setNewData(weathers);

        if(weather.basic.weatherId.equals(WeatherEvent.BEIJING)){
            EventBus.getDefault().post(new WeatherEvent( WeatherEvent.BEIJING, weather));
        }else if(weather.basic.weatherId.equals(WeatherEvent.SHANGHAI)){
            EventBus.getDefault().post(new WeatherEvent( WeatherEvent.SHANGHAI, weather));
        }
    }

    @Override
    public void getWeatherError() {
        showRefreshing(false);
        Snackbar.make(getView(), "获取天气失败!", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getWeatherInfo(cityid);
            }
        }).setActionTextColor(ContextCompat.getColor(getContext(),R.color.actionColor)).show();
    }

    @Override
    public void bingPic(final String url) {
        Glide.with(getActivity()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).dontTransform().into(bingPicImg);
    }

}
