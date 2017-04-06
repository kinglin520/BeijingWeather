package com.superman.beijingweather.ui.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.superman.beijingweather.R;
import com.superman.beijingweather.model.weather.DetailForecast;
import com.superman.beijingweather.model.weather.HourForecast;
import com.superman.beijingweather.model.weather.Suggestion;
import com.superman.beijingweather.model.weather.Weather;
import com.superman.beijingweather.model.weather.WeatherBean;
import com.superman.beijingweather.ui.weather.SimpleSubscriber;
import com.superman.beijingweather.utils.DateUtils;
import com.superman.beijingweather.utils.SizeUtils;
import com.superman.beijingweather.utils.WeatherUtil;
import com.superman.beijingweather.widgets.WeatherChartView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;



/**
 * Created by wenlin on 2017/2/21.
 */

public class WeatherAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private Context context;
    private boolean showWeatherChart = false;
    private boolean isToday = true;

    public WeatherAdapter(List<MultiItemEntity> data, Context context) {
        super(data);
        this.context = context;
//        addItemType(Weather.TYPE_NOW, item_weather_container);
        addItemType(Weather.TYPE_DETAILFORECAST, R.layout.forecast);
//        addItemType(Weather.TYPE_AQI ,R.layout.aqi);
        addItemType(Weather.TYPE_SUGGESTION, R.layout.item_suggestion_weather);
        addItemType(Weather.TYPE_NOWDETAIL, R.layout.forecast_now_detail);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, MultiItemEntity multiItemEntity) {
        switch (baseViewHolder.getItemViewType()) {
//            case Weather.TYPE_NOW:
//                Weather hourForecast = (Weather) multiItemEntity;
//                LinearLayout hourforecastLayout = baseViewHolder.getView(R.id.contentLayout);
//                hourforecastLayout.removeAllViews();
//                for (HourForecast hourForecast1 : hourForecast.hourForecastList) {
//                    View view = View.inflate(context, R.layout.item_now_weather, null);
//                    TextView tvTime = (TextView) view.findViewById(R.id.tv_now_time);
//                    TextView tvTemp = (TextView) view.findViewById(R.id.tv_now_temp);
//                    TextView tvPop = (TextView) view.findViewById(R.id.tv_now_pop);
//                    TextView tvWind = (TextView) view.findViewById(R.id.tv_now_wind);
//                    tvTime.setText(hourForecast1.getDate().split(" ")[1]);
//                    tvTemp.setText(hourForecast1.getTmp() + "℃");
//                    tvPop.setText(hourForecast1.getCond().getTxt());
//                    tvWind.setText(hourForecast1.getWind().getSc());
//                    hourforecastLayout.addView(view);
//                }
//                break;
            case Weather.TYPE_DETAILFORECAST:
                Weather weather = (Weather) multiItemEntity;
                if (weather == null){
                    return;
                }
                LinearLayout forecastLayout = baseViewHolder.getView(R.id.forecast_layout);
                TextView countyText = (TextView)baseViewHolder.getView(R.id.proudcast_county_tv);
                countyText.setText("预报 " + weather.basic.cityName);

                if (showWeatherChart) {
                    forecastLayout.setPadding(0, SizeUtils.dp2px(mContext, 16), 0, SizeUtils.dp2px(mContext, 16));
                    forecastLayout.removeAllViews();
                    forecastLayout.addView(getChartView(weather));
                } else {
                    forecastLayout.removeAllViews();
                    for (DetailForecast forecast : weather.forecastList) {
                        View view = LayoutInflater.from(context).inflate(R.layout.forecast_item, forecastLayout, false);

                        TextView dateText = (TextView) view.findViewById(R.id.date_text);
                        TextView maxText = (TextView) view.findViewById(R.id.max_text);
                        TextView minText = (TextView) view.findViewById(R.id.min_text);
                        final ImageView info_iv = (ImageView) view.findViewById(R.id.info_iv);

                        maxText.setText(String.format("%s℃", forecast.getTmp().getMax()));
                        minText.setText(String.format("%s℃", forecast.getTmp().getMin()));
                        WeatherUtil.getWeatherDict(forecast.getCond().getCode_d()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SimpleSubscriber<WeatherBean>() {
                            @Override
                            public void onNext(WeatherBean weatherBean) {
                                Glide.with(context).load(weatherBean.getIcon()).diskCacheStrategy(DiskCacheStrategy.ALL).dontTransform().into(info_iv);
                            }
                        });
                        forecastLayout.addView(view);
                        if (isToday &&
                                weather.hourForecastList != null && weather.hourForecastList.size() > 0) {
                            dateText.setText(String.format("%s 今天",DateUtils.getWeek(forecast.getDate())));
                            for (HourForecast hourForecast1 : weather.hourForecastList) {
                                View hourView = View.inflate(context, R.layout.item_now_weather, null);
                                TextView tvTime = (TextView) hourView.findViewById(R.id.tv_now_time);
                                TextView tvTemp = (TextView) hourView.findViewById(R.id.tv_now_temp);
                                TextView tvWind = (TextView) hourView.findViewById(R.id.tv_now_wind);
                                final ImageView todayInfo_iv = (ImageView) hourView.findViewById(R.id.info_iv);
                                WeatherUtil.getWeatherDict(hourForecast1.getCond().getCode()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SimpleSubscriber<WeatherBean>() {
                                    @Override
                                    public void onNext(WeatherBean weatherBean) {
                                        Glide.with(context).load(weatherBean.getIcon()).diskCacheStrategy(DiskCacheStrategy.ALL).dontTransform().into(todayInfo_iv);
                                    }
                                });
                                tvTime.setText(hourForecast1.getDate().split(" ")[1]);
                                tvTemp.setText(hourForecast1.getTmp() + "℃");
                                tvWind.setText(hourForecast1.getWind().getSc());
                                forecastLayout.addView(hourView);
                            }
                            isToday = false;
                        }else {
                            dateText.setText(DateUtils.getWeek(forecast.getDate()));
                        }
                    }
                }
                forecastLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showWeatherChart = !showWeatherChart;
                        notifyItemChanged(baseViewHolder.getAdapterPosition());
                    }
                });
                isToday = true;
                break;
//            case Weather.TYPE_AQI:
//                AQI aqi = (AQI) multiItemEntity;
//                TextView aqi_tv = baseViewHolder.getView(R.id.aqi_text);
//                TextView pm25_tv = baseViewHolder.getView(R.id.pm25_text);
//                aqi_tv.setText(aqi.city.aqi);
//                pm25_tv.setText(aqi.city.pm25);
//                break;
            case Weather.TYPE_SUGGESTION:
                Suggestion suggestion = (Suggestion) multiItemEntity;
                baseViewHolder.setText(R.id.tv_suggestion_air, String.format("舒适指数 -- %s", suggestion.comfort.brf));
                baseViewHolder.setText(R.id.tv_suggestion_air_info, suggestion.comfort.info);
                baseViewHolder.setText(R.id.tv_suggestion_out, String.format("运动指数 -- %s", suggestion.sport.brf));
                baseViewHolder.setText(R.id.tv_suggestion_out_info, suggestion.sport.info);
                baseViewHolder.setText(R.id.tv_suggestion_car, String.format("洗车指数 -- %s", suggestion.carWash.brf));
                baseViewHolder.setText(R.id.tv_suggestion_car_info, suggestion.carWash.info);
                break;
            case Weather.TYPE_NOWDETAIL:
                Weather now = (Weather) multiItemEntity;
                if(now == null){
                    return;
                }
                TextView sunStart_tv = baseViewHolder.getView(R.id.item_sunstart_tv);
                TextView sunEnd_tv = baseViewHolder.getView(R.id.item_sunend_tv);
                TextView pres_tv = baseViewHolder.getView(R.id.item_pres_tv);
                TextView uv_tv = baseViewHolder.getView(R.id.item_uv_tv);
                TextView dir_tv = baseViewHolder.getView(R.id.item_dir_tv);
                TextView sc_tv = baseViewHolder.getView(R.id.item_sc_tv);
                TextView air_tv = baseViewHolder.getView(R.id.item_air_tv);

                sunStart_tv.setText(now.forecastList.get(0).getAstro().getSr());
                sunEnd_tv.setText(now.forecastList.get(0).getAstro().getSs());
                pres_tv.setText(String.format("%s 百帕", now.forecastList.get(0).getPres()));
                uv_tv.setText(now.forecastList.get(0).getUv());
                dir_tv.setText(now.forecastList.get(0).getWind().getDir());
                sc_tv.setText(String.format("%s 级", now.forecastList.get(0).getWind().getSc()));
                air_tv.setText(now.aqi.city.aqi);
                break;
        }
    }

    private WeatherChartView getChartView(Weather weather) {
        WeatherChartView chartView = new WeatherChartView(mContext);
        chartView.setWeather(weather);
        return chartView;
    }
}
