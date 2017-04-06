package com.superman.beijingweather.ui.girl.nestimageset.personalpictureset;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.superman.beijingweather.R;
import com.superman.beijingweather.event.GirlsComingEvent;
import com.superman.beijingweather.model.Girl;
import com.superman.beijingweather.service.GirlService;
import com.superman.beijingweather.ui.base.BaseActivity;
import com.superman.beijingweather.ui.girl.adapter.GirlsAdapter;
import com.superman.beijingweather.utils.ThemeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;


public class PersonalPictureSetActivity extends BaseActivity implements PersonalPictureSetView {

    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String EXTRA_IMAGE_TITLE = "image_title";
    @BindView(R.id.rv_gank)
    RecyclerView recyclerView;

    SwipeRefreshLayout refreshLayout;

    private GirlsAdapter adapter;
    private String baseUrl = "";
    private PersonalPictureSetPresenter presenter;

    public static Intent newIntent(Context context, String url, String desc) {
        Intent intent = new Intent(context, PersonalPictureSetActivity.class);
        intent.putExtra(PersonalPictureSetActivity.EXTRA_IMAGE_URL, url);
        intent.putExtra(PersonalPictureSetActivity.EXTRA_IMAGE_TITLE, desc);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personalsets_picture;
    }

    @Override
    protected int getMenuId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        showSystemUI();
        toolbar.setTitle("图集");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        presenter = new PersonalPictureSetPresenterImpl(this);

        setDisplayHomeAsUpEnabled(true);
        baseUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (adapter.getData() == null || adapter.getData().size() == 0)
                    loadData();
                else {
                    adapter.notifyDataSetChanged();
                    showRefreshing(false);
                }
            }
        });
        refreshLayout.setColorSchemeResources(ThemeUtil.getCurrentColorPrimary(this));
        recyclerView = (RecyclerView) findViewById(R.id.rv_gank);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GirlsAdapter(this, null);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        adapter.setNewData(null);
        presenter.getPersonalGirls(baseUrl);
    }

    protected void showRefreshing(final boolean refresh) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refresh);
            }
        });
    }

    @Override
    public void setPersonalGirls(List<Girl> girls) {
        GirlService.start(PersonalPictureSetActivity.this, GirlsComingEvent.GIRLS_FROM_MZITU, girls);
    }

    @Override
    public void getGirlsError() {
        showRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void girlIsComing(GirlsComingEvent event) {
        if (event.getFrom() != GirlsComingEvent.GIRLS_FROM_MZITU)
            return;
        showRefreshing(false);
        if (adapter.getData() == null || adapter.getData().size() == 0) {
            adapter.setNewData(event.getGirls());
        } else {
            adapter.addData(adapter.getData().size(), event.getGirls());
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
        presenter.releaseSubscriber();
        super.onDestroy();
    }


}
