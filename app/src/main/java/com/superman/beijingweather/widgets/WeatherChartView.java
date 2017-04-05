package com.superman.beijingweather.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.superman.beijingweather.R;
import com.superman.beijingweather.model.weather.DetailForecast;
import com.superman.beijingweather.model.weather.Weather;
import com.superman.beijingweather.model.weather.WeatherBean;
import com.superman.beijingweather.ui.weather.SimpleSubscriber;
import com.superman.beijingweather.utils.DateUtils;
import com.superman.beijingweather.utils.SizeUtils;
import com.superman.beijingweather.utils.WeatherUtil;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;

public class WeatherChartView extends LinearLayout {

    private List<DetailForecast> dailyForecastList = new ArrayList<>();

    LayoutParams cellParams;
    LayoutParams rowParams;
    LayoutParams chartParams;

    public WeatherChartView(Context context) {
        this(context, null);
    }

    public WeatherChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(VERTICAL);
        rowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        cellParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        chartParams = new LayoutParams(LayoutParams.MATCH_PARENT, SizeUtils.dp2px(getContext(), 200));
    }

    private void letItGo() {
        removeAllViews();
        LinearLayout dateTitleView = new LinearLayout(getContext());
        dateTitleView.setLayoutParams(rowParams);
        dateTitleView.setOrientation(HORIZONTAL);
        dateTitleView.removeAllViews();

        LinearLayout iconView = new LinearLayout(getContext());
        iconView.setLayoutParams(rowParams);
        iconView.setOrientation(HORIZONTAL);
        iconView.removeAllViews();

        LinearLayout weatherStrView = new LinearLayout(getContext());
        weatherStrView.setLayoutParams(rowParams);
        weatherStrView.setOrientation(HORIZONTAL);
        weatherStrView.removeAllViews();

        List<Integer> minTemp = new ArrayList<>();
        List<Integer> maxTemp = new ArrayList<>();

//        DetailForecast yesterday = WeatherUtil.getYesterday();
//        if (yesterday != null) {
//            dailyForecastList.add(0, yesterday);
//        }

        for (int i = 0; i < dailyForecastList.size(); i++) {
            TextView tvDate = new TextView(getContext());
            tvDate.setGravity(Gravity.CENTER);
            tvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            tvDate.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

            TextView tvWeather = new TextView((getContext()));
            tvWeather.setGravity(Gravity.CENTER);
            tvWeather.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            tvWeather.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

            final ImageView ivIcon = new ImageView(getContext());
            ivIcon.setAdjustViewBounds(true);
            ivIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int padding = SizeUtils.dp2px(getContext(), 10);
            ivIcon.setPadding(padding, padding, padding, padding);

            tvDate.setText(DateUtils.getWeek(dailyForecastList.get(i).getDate()));
//            if (yesterday != null) {
//                if (i == 0) {
//                    tvDate.setText("昨天");
//                } else if (i == 1) {
//                    tvDate.setText("今天");
//                } else if (i == 2) {
//                    tvDate.setText("明天");
//                } else {
//                    tvDate.setText(TimeUtils.getWeek(dailyForecastList.get(i).getDate(), TimeUtils.DATE_SDF));
//                }

//            } else {
//                if (i == 0) {
//                    tvDate.setText("今天");
//                } else if (i == 1) {
//                    tvDate.setText("明天");
//                } else {
//                    tvDate.setText(TimeUtils.getWeek(dailyForecastList.get(i).getDate(), TimeUtils.DATE_SDF));
//                }
//            }
            tvWeather.setText(dailyForecastList.get(i).getCond().getTxt_d());
            WeatherUtil.getWeatherDict(dailyForecastList.get(i).getCond().getCode_d()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SimpleSubscriber<WeatherBean>() {
                @Override
                public void onNext(WeatherBean weatherBean) {
                    Glide.with(getContext()).load(weatherBean.getIcon()).diskCacheStrategy(DiskCacheStrategy.ALL).dontTransform().into(ivIcon);
                }
            });
            minTemp.add(Integer.valueOf(dailyForecastList.get(i).getTmp().getMin()));
            maxTemp.add(Integer.valueOf(dailyForecastList.get(i).getTmp().getMax()));
            weatherStrView.addView(tvWeather, cellParams);
            dateTitleView.addView(tvDate, cellParams);
            iconView.addView(ivIcon, cellParams);
        }
        addView(dateTitleView);
        addView(iconView);
        addView(weatherStrView);
        ChartView chartView = new ChartView(getContext());
        chartView.setData(minTemp, maxTemp);
        chartView.setPadding(0, SizeUtils.dp2px(getContext(), 16), 0, SizeUtils.dp2px(getContext(), 16));
        addView(chartView, chartParams);
    }

    public void setWeather(Weather weather) {
        dailyForecastList.clear();
        dailyForecastList.addAll(weather.forecastList);
        letItGo();
    }
}
