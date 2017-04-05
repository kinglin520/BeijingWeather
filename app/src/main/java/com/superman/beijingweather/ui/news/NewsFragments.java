package com.superman.beijingweather.ui.news;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.superman.beijingweather.R;
import com.superman.beijingweather.model.news.NewsChannelTable;
import com.superman.beijingweather.ui.MainActivity;
import com.superman.beijingweather.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wenlin on 2017/3/6.
 */

public class NewsFragments extends BaseFragment {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public static String FRAGMENT_TYPE = "fragmenttype";

    private static final String TAG = "NewsFragments";



//    private ArrayList<Fragment> mNewsFragmentList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_viewpager;
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle("新闻");
        ((MainActivity) getActivity()).initDrawer(mToolbar);
        initTabLayout();
    }

    @Override
    protected void lazyFetchData() {

    }

    private void initTabLayout() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        setupViewPager(adapter);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    private void setupViewPager(ViewPagerAdapter adapter) {
        NewsChannelTable toutiaoChannelTable = new NewsChannelTable();
        toutiaoChannelTable.setNewsChannelId("T1348647909107");
        toutiaoChannelTable.setNewsChannelType("headline");
        toutiaoChannelTable.setNewsChannelSelect(true);
        toutiaoChannelTable.setNewsChannelIndex(0);
        toutiaoChannelTable.setNewsChannelFixed(true);
        toutiaoChannelTable.setNewsChannelName("头条");

        NewsChannelTable scienceChannelTable = new NewsChannelTable();
        scienceChannelTable.setNewsChannelId("T1348649580692");
        scienceChannelTable.setNewsChannelType("list");
        scienceChannelTable.setNewsChannelSelect(true);
        scienceChannelTable.setNewsChannelIndex(0);
        scienceChannelTable.setNewsChannelFixed(false);
        scienceChannelTable.setNewsChannelName("科技");

        NewsChannelTable monkeyChannelTable = new NewsChannelTable();
        monkeyChannelTable.setNewsChannelId("T1348648756099");
        monkeyChannelTable.setNewsChannelType("list");
        monkeyChannelTable.setNewsChannelSelect(true);
        monkeyChannelTable.setNewsChannelIndex(0);
        monkeyChannelTable.setNewsChannelFixed(false);
        monkeyChannelTable.setNewsChannelName("财经");

        NewsChannelTable warChannelTable = new NewsChannelTable();
        warChannelTable.setNewsChannelId("T1348648141035");
        warChannelTable.setNewsChannelType("list");
        warChannelTable.setNewsChannelSelect(true);
        warChannelTable.setNewsChannelIndex(0);
        warChannelTable.setNewsChannelFixed(false);
        warChannelTable.setNewsChannelName("军事");

        NewsChannelTable sportChannelTable = new NewsChannelTable();
        sportChannelTable.setNewsChannelId("T1348649079062");
        sportChannelTable.setNewsChannelType("list");
        sportChannelTable.setNewsChannelSelect(true);
        sportChannelTable.setNewsChannelIndex(0);
        sportChannelTable.setNewsChannelFixed(false);
        sportChannelTable.setNewsChannelName("体育");

        adapter.addFrag(createListFragment("headline",toutiaoChannelTable),toutiaoChannelTable.getNewsChannelName());
        adapter.addFrag(createListFragment("list",scienceChannelTable),scienceChannelTable.getNewsChannelName());
        adapter.addFrag(createListFragment("list",monkeyChannelTable),monkeyChannelTable.getNewsChannelName());
        adapter.addFrag(createListFragment("list",warChannelTable),warChannelTable.getNewsChannelName());
        adapter.addFrag(createListFragment("list",sportChannelTable),sportChannelTable.getNewsChannelName());

    }

    private NewsListFragment createListFragment( String fragmentType, NewsChannelTable newsChannelTable) {
        NewsListFragment fragment = NewsListFragment.newInstance(fragmentType,newsChannelTable.getNewsChannelType(), newsChannelTable.getNewsChannelId(), newsChannelTable.getNewsChannelIndex());
        return fragment;
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

