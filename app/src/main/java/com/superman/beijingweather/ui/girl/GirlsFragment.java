package com.superman.beijingweather.ui.girl;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.superman.beijingweather.R;
import com.superman.beijingweather.ui.MainActivity;
import com.superman.beijingweather.ui.base.BaseFragment;
import com.superman.beijingweather.ui.girl.nestimageset.OneLevelFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by wenlin on 2017/2/16.
 */

public class GirlsFragment extends BaseFragment {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public static String FRAGMENT_TYPE = "fragmenttype";

    public static String GANK = "gank";
    public static String JIANDAN = "jiandan";

    public static String GIRLSERVERURL = "girlserverurl";

    public static String HOT = "hot";
    public static String SEXGRIL = "sexgirl";
    public static String JAPANGIRLS = "japangirl";
    public static String SINGLEGIRL = "singlegirl";
    public static String PREFACEGIRL = "prefacegirl";


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_viewpager;
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle("福利");
        ((MainActivity) getActivity()).initDrawer(mToolbar);
        initTabLayout();
    }

    @Override
    protected void lazyFetchData() {

    }

    private void initTabLayout() {
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void setupViewPager(ViewPager viewPager) {
        Bundle data;
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        Fragment newfragment = new GankFragment();
        data = new Bundle();
        data.putString(FRAGMENT_TYPE, GANK);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, GANK);

        Fragment jianDanFragment = new GankFragment();
        data = new Bundle();
        data.putString(FRAGMENT_TYPE, JIANDAN);
        jianDanFragment.setArguments(data);
        adapter.addFrag(jianDanFragment, "煎蛋");

        data = new Bundle();
        data.putString(GIRLSERVERURL, "http://www.mzitu.com/hot");
        Fragment hotFragment = OneLevelFragment.getInstance(data);
        adapter.addFrag(hotFragment, "Hot");

        data = new Bundle();
        data.putString(GIRLSERVERURL, "http://www.mzitu.com/xinggan");
        Fragment sexFragment = OneLevelFragment.getInstance(data);
        adapter.addFrag(sexFragment, "性感");

        data = new Bundle();
        data.putString(GIRLSERVERURL, "http://www.mzitu.com/japan");
        Fragment japanFragment = OneLevelFragment.getInstance(data);
        adapter.addFrag(japanFragment, "日本");

        data = new Bundle();
        data.putString(GIRLSERVERURL, "http://www.mzitu.com/mm");
        Fragment mmFragment = OneLevelFragment.getInstance(data);
        adapter.addFrag(mmFragment, "清纯");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
