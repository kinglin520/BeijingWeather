package com.superman.beijingweather.ui.weather;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.superman.beijingweather.R;
import com.superman.beijingweather.event.WeatherEvent;
import com.superman.beijingweather.model.weather.Weather;
import com.superman.beijingweather.ui.MainActivity;
import com.superman.beijingweather.ui.base.BaseFragment;
import com.superman.beijingweather.utils.ACache;
import com.superman.beijingweather.utils.TTSManager;
import com.superman.beijingweather.utils.WeatherUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wenlin on 2017/2/24.
 */

public class WeathersFragment extends BaseFragment  {
    @BindView(R.id.bannner)
    ImageView bannner;
    @BindView(R.id.tv_city_name)
    TextView tvCityName;
    @BindView(R.id.tv_weather_string)
    TextView tvWeatherString;
    @BindView(R.id.tv_weather_aqi)
    TextView tvWeatherAqi;
    @BindView(R.id.tv_temp)
    TextView tvTemp;
    @BindView(R.id.tv_update_time)
    TextView tvUpdateTime;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private List<String> cityids = new ArrayList<>();

    private boolean initDataLoaded = false;

    private Weather currentWeather;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initViews() {
        toolbar.setTitle("天气");
        ((MainActivity) getActivity()).initDrawer(toolbar);
        initTabLayout();
        toolbar.inflateMenu(R.menu.menu_weather);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_tts) {
                    TTSManager.getInstance(getActivity()).speak(WeatherUtil.getShareMessage(currentWeather), null);
                    return true;
                }
                return false;
            }
        });
    }

    private void initTabLayout() {
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
    }

    private void setupViewPager(ViewPager viewPager) {
        cityids.clear();
        Bundle data;
        WeathersFragment.ViewPagerAdapter adapter = new WeathersFragment.ViewPagerAdapter(getChildFragmentManager());

        data = new Bundle();
        data.putString(WeatherFragment.CITYID, "CN101010100");
        WeatherFragment one = WeatherFragment.getInstance(data);
        data = new Bundle();
        data.putString(WeatherFragment.CITYID, "CN101020100");
        WeatherFragment two = WeatherFragment.getInstance(data);

        cityids.add("CN101010100");
        cityids.add("CN101020100");
        adapter.addFragment(one,"北京");
        adapter.addFragment(two,"上海");

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(initDataLoaded){
                    currentWeather = (Weather) ACache.get(getContext()).getAsObject(cityids.get(position));
                    if(currentWeather != null){
                        showTitleInfo(currentWeather);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    protected void lazyFetchData() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WeatherEvent weatherEvent) {
        if (weatherEvent.getFrom().equals(WeatherEvent.BEIJING)) {
            ACache.get(getContext()).put(WeatherEvent.BEIJING, weatherEvent.getWeather());
            showTitleInfo(weatherEvent.getWeather());
        } else if (weatherEvent.getFrom().equals(WeatherEvent.SHANGHAI)) {
            ACache.get(getContext()).put(WeatherEvent.SHANGHAI, weatherEvent.getWeather());
            showTitleInfo(weatherEvent.getWeather());
            initDataLoaded = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void showTitleInfo(Weather weather) {
        currentWeather = weather;
        tvCityName.setText(weather.basic.cityName);
        tvWeatherString.setText(weather.now.more.info);
        tvWeatherAqi.setText(weather.aqi.city.qlty);
        tvTemp.setText(weather.now.temperature + "℃");
        tvUpdateTime.setText(String.format("截止 %s", weather.basic.update.updateTime.split(" ")[1]));
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment , String title) {
            fragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
