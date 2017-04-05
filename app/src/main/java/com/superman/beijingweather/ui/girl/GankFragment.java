package com.superman.beijingweather.ui.girl;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.superman.beijingweather.R;
import com.superman.beijingweather.event.GirlsComingEvent;
import com.superman.beijingweather.model.Gank;
import com.superman.beijingweather.model.Girl;
import com.superman.beijingweather.model.JiandanXXOO;
import com.superman.beijingweather.service.GirlService;
import com.superman.beijingweather.ui.girl.adapter.GirlsAdapter;
import com.superman.beijingweather.ui.base.BaseContentFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wenlin on 2017/2/16.
 */

public class GankFragment extends BaseContentFragment implements GankFragmentView {


    @BindView(R.id.rv_gank)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    private GirlsAdapter adapter;
    private int currentPage = 1;
    private boolean isLoading = false;
    private GankPresenter presenter;
    private String fragmentType;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void initViews() {
        super.initViews();

        fragmentType = getArguments().getString(GirlsFragment.FRAGMENT_TYPE);

        presenter = new GankPresenterImpl(this);

        adapter = new GirlsAdapter(getActivity(), null);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    isLoading = true;
                    getGirlFromServer();
                }
            }


        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void lazyFetchData() {
        currentPage = 1;
        adapter.setNewData(null);
        getGirlFromServer();
    }

    private void getGirlFromServer() {
        showRefreshing(true);
        if (fragmentType.equalsIgnoreCase(GirlsFragment.GANK)) {
            presenter.getGank(String.valueOf(currentPage));
        } else {
            presenter.getJianDan(String.valueOf(currentPage));
        }
    }

    @Override
    public void setListGank(List<Gank> ganks) {
        isLoading = false;
        currentPage++;
        List<Girl> girls = new ArrayList<>();
        for (Gank gank : ganks) {
            girls.add(new Girl(gank.getUrl()));
        }
        GirlService.start(getActivity(), GirlsComingEvent.GIRLS_FROM_GANK, girls);
    }

    @Override
    public void getListGankFalse() {
        isLoading = false;
        showRefreshing(false);
        Snackbar.make(getView(), "获取Gank妹纸失败!", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGirlFromServer();
            }
        }).setActionTextColor(ContextCompat.getColor(getContext(), R.color.actionColor)).show();
    }

    @Override
    public void setListJianDan(List<JiandanXXOO> jianDan) {
        isLoading = false;
        currentPage++;
        List<Girl> girls = new ArrayList<>();
        for (JiandanXXOO jiandanXXOO : jianDan) {
            for (String url : jiandanXXOO.getPics()) {
                girls.add(new Girl(url));
            }
        }
        GirlService.start(getActivity(), GirlsComingEvent.GIRLS_FROM_GANK, girls);
    }

    @Override
    public void getListJianDanFalse() {
        isLoading = false;
        showRefreshing(false);
        Snackbar.make(getView(), "获取煎蛋妹纸失败!", Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGirlFromServer();
            }
        }).setActionTextColor(ContextCompat.getColor(getContext(), R.color.actionColor)).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GirlsComingEvent event) {
        if (event.getFrom() != GirlsComingEvent.GIRLS_FROM_GANK)
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
        super.onDestroy();
    }
}
