package com.superman.beijingweather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.superman.beijingweather.Constants;
import com.superman.beijingweather.R;
import com.superman.beijingweather.event.ThemeChangedEvent;
import com.superman.beijingweather.ui.base.BaseActivity;
import com.superman.beijingweather.ui.girl.GirlsFragment;
import com.superman.beijingweather.ui.news.NewsFragments;
import com.superman.beijingweather.ui.setting.AboutActivity;
import com.superman.beijingweather.ui.setting.SettingActivity;
import com.superman.beijingweather.ui.weather.WeathersFragment;
import com.superman.beijingweather.utils.RxDrawer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wenlin on 2017/2/16.
 */
public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private String currentFragmentTag;

    private static final String FRAGMENT_TAG_GANK = "gank";
    private static final String FRAGMENT_TAG_WEATHER = "weather";
    private static final String FRAGMENT_TAG_NEWS = "news";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getMenuId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initNavigationViewHeader();
        initFragment(savedInstanceState);
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            switchContent(FRAGMENT_TAG_WEATHER);
        } else {
            currentFragmentTag = savedInstanceState.getString(Constants.CURRENT_INDEX);
            switchContent(currentFragmentTag);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.CURRENT_INDEX, currentFragmentTag);
        Log.i("currentFragmentTag", currentFragmentTag);
    }

    @Override
    protected void loadData() {

    }

    public void initDrawer(Toolbar toolbar) {
        if (toolbar != null) {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            mDrawerToggle.syncState();
            mDrawerLayout.addDrawerListener(mDrawerToggle);
        }
    }

    private void initNavigationViewHeader() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.inflateHeaderView(R.layout.drawer_header);
        navigationView.setNavigationItemSelectedListener(new NavigationItemSelected());
    }

    class NavigationItemSelected implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(final MenuItem menuItem) {
            RxDrawer.close(mDrawerLayout).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    new BaseSubscriber() {
                        @Override
                        public void onNext(Object o) {
                            switch (menuItem.getItemId()) {
                                case R.id.nav_weather:
                                    menuItem.setChecked(true);
                                    switchContent(FRAGMENT_TAG_WEATHER);
                                    break;
                                case R.id.nav_girl:
                                    menuItem.setChecked(true);
                                    switchContent(FRAGMENT_TAG_GANK);
                                    break;
                                case R.id.nav_news:
                                    menuItem.setChecked(true);
                                    switchContent(FRAGMENT_TAG_NEWS);
                                    break;
                                case R.id.nav_send:
                                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                                    break;
                                case R.id.nav_share:
                                    startActivity(new Intent(MainActivity.this, SettingActivity.class));
                                    break;

                            }
                        }
                    });
            return false;
        }
    }

    public void switchContent(String name) {
        if (currentFragmentTag != null && currentFragmentTag.equals(name))
            return;

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }

        Fragment foundFragment = fragmentManager.findFragmentByTag(name);

        if (foundFragment == null) {
            switch (name) {
                case FRAGMENT_TAG_GANK:
                    foundFragment = new GirlsFragment();
                    break;
                case FRAGMENT_TAG_WEATHER:
                    foundFragment = new WeathersFragment();
                    break;
                case FRAGMENT_TAG_NEWS:
                    foundFragment = new NewsFragments();
                    break;
            }
        }

        if (foundFragment == null) {

        } else if (foundFragment.isAdded()) {
            ft.show(foundFragment);
        } else {
            ft.add(R.id.content_main, foundFragment, name);
        }
        ft.commit();
        currentFragmentTag = name;
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onThemeChanged(ThemeChangedEvent event) {
        this.recreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
