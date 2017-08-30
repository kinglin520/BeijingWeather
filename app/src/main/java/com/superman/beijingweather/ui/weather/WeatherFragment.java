package com.superman.beijingweather.ui.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.superman.beijingweather.R;
import com.superman.beijingweather.event.WeatherEvent;
import com.superman.beijingweather.model.weather.County;
import com.superman.beijingweather.model.weather.Suggestion;
import com.superman.beijingweather.model.weather.Weather;
import com.superman.beijingweather.ui.base.BaseContentFragment;
import com.superman.beijingweather.ui.weather.adapter.WeatherAdapter;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.blankj.utilcode.util.LocationUtils.getAddress;
import static com.blankj.utilcode.util.LocationUtils.isLocationEnabled;

/**
 * Created by wenlin on 2017/2/20.
 */

public class WeatherFragment extends BaseContentFragment implements WeatherView {

    private String TAG = "WeatherFragment";
    @BindView(R.id.bing_pic_img)
    ImageView bingPicImg;
    @BindView(R.id.rv_weather)
    RecyclerView rvWeather;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    /**
     * 北京
     */
    private String cityId = "CN101010100";

    public static String CITYID = "cityid";

    private static final int REQUECT_CODE_LOCATION = 2;
    private static final int REQUECT_CODE_EXTERNAL_STORAGE = 1;

    /**
     * 辖区列表
     */
    private List<County> countyList;

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (REQUECT_CODE_LOCATION == requestCode) {
            if (grantResults.length == 0) {
                return;
            }
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            initLocation();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void initViews() {
        super.initViews();
        cityId = getArguments().getString(CITYID);

        presenter = new WeatherPresenterImpl(this);

        initRecyler();

        initCounties();

        requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUECT_CODE_LOCATION);

    }

    private void initCounties() {
        countyList = DataSupport.where("weatherId like ?",cityId.substring(0,7)+"%").find(County.class);
        if (countyList != null && countyList.size() > 0) {

        } else {
            if(cityId.equalsIgnoreCase(WeatherEvent.BEIJING)){
                presenter.getBeijingCounties();
            }else {
                presenter.getShanghaiCounties();
            }
        }
    }

    private static LocationManager mLocationManager;
    private LocationListener mListener;

    private void initLocation() {
        register(5000, 50, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                refreshLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    /**
     * 只对定位到上海和北京地区进行定位精确到区县
     * @param location
     */
    private void refreshLocationInfo(Location location){
        LogUtils.i(TAG, new Gson().toJson(location));
        String street = getStreet(location.getLatitude(), location.getLongitude());
        if(street.toString().indexOf("北京") != -1) {
            street = street.replace("北京", "");
        }else if(street.toString().indexOf("上海") != -1){
            street = street.replace("上海","");
        }else {
            return;
        }
        for(County county : countyList){
            if(street.contains(county.getCountyName())){
                cityId = county.getWeatherId();
                getWeather();
                return;
            }
        }
    }


    public boolean register(long minTime, long minDistance, LocationListener listener) {
        if (listener == null)
            return false;
        mLocationManager = (LocationManager) Utils.getContext().getSystemService(Context.LOCATION_SERVICE);
        mListener = listener;
        if (!isLocationEnabled()) {
            ToastUtils.showShortToastSafe("无法定位，请打开定位服务");
            return false;
        }
        String provider;
        if(PhoneUtils.isSimCardReady()){
            provider = mLocationManager.getBestProvider(getCriteria(), true);
        }else {
            provider = LocationManager.NETWORK_PROVIDER;
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        mLocationManager.requestLocationUpdates(provider, minTime, minDistance, mListener);
        return true;
    }

    /**
     * 设置定位参数
     *
     * @return {@link Criteria}
     */
    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    /**
     * 根据经纬度获取所在街道
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在街道
     */
    public static String getStreet(double latitude, double longitude) {
        Address address = getAddress(latitude, longitude);
        return address == null ? "unknown" : address.getAddressLine(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mListener != null && mLocationManager != null)
            mLocationManager.removeUpdates(mListener);
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
        presenter.getBingPic();
    }

    private void getWeather() {
        showRefreshing(true);
        presenter.getWeatherInfo(cityId);
    }

    @Override
    public void refreshWeatherInfo(Weather weather) {
            showRefreshing(false);
        if (weather == null) {
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

        if (weather.basic.weatherId.contains(WeatherEvent.BEIJING)) {
            EventBus.getDefault().post(new WeatherEvent(WeatherEvent.BEIJING, weather));
        } else if (weather.basic.weatherId.contains(WeatherEvent.SHANGHAI)) {
            EventBus.getDefault().post(new WeatherEvent(WeatherEvent.SHANGHAI, weather));
        }
    }

    @Override
    public void getWeatherError() {
        showRefreshing(false);
        Snackbar.make(getView(), "获取天气失败!", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeather();
            }
        }).setActionTextColor(ContextCompat.getColor(getContext(), R.color.actionColor)).show();
    }

    @Override
    public void bingPic(final String url) {
        Glide.with(getActivity()).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).dontTransform().into(bingPicImg);
    }

    @Override
    public void setCounties(List<County> counties) {
        for (County county : counties) {
            County mCounty = new County();
            mCounty.setCountyName(county.getCountyName());
            mCounty.setWeatherId(county.getWeatherId());
            mCounty.setCityId(county.getCityId());
            mCounty.save();
        }
    }

}
