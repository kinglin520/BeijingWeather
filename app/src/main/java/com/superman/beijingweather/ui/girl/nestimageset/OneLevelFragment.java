package com.superman.beijingweather.ui.girl.nestimageset;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.superman.beijingweather.R;
import com.superman.beijingweather.model.Girl;
import com.superman.beijingweather.ui.base.BaseContentFragment;
import com.superman.beijingweather.ui.girl.GirlsFragment;
import com.superman.beijingweather.ui.girl.adapter.OneLevelAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wenlin on 2017/2/28.
 */

public class OneLevelFragment extends BaseContentFragment implements OneLevelView {
    @BindView(R.id.rv_gank)
    RecyclerView rvGank;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    private int currentPage = 1;
    private String baseUrl = "";
    private boolean isLoading = false;
    private OneLevelAdapter adapter;
    private OneLevelPresenter presenter;

    public static OneLevelFragment getInstance(Bundle bundle) {
        OneLevelFragment oneLevelFragment = new OneLevelFragment();
        oneLevelFragment.setArguments(bundle);
        return oneLevelFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void initViews() {
        super.initViews();

        baseUrl = getArguments().getString(GirlsFragment.GIRLSERVERURL);

        presenter = new OneLevelPresenterImpl(this);

        initRv();
    }

    private void initRv() {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvGank.setLayoutManager(layoutManager);
        adapter = new OneLevelAdapter(getActivity(), null);
        rvGank.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    isLoading = true;
                    getGirlFromServer();
                }
            }
        });
        rvGank.setAdapter(adapter);
    }

    @Override
    protected void lazyFetchData() {
        currentPage = 1;
        adapter.setNewData(null);
        getGirlFromServer();
    }

    private void getGirlFromServer() {
        showRefreshing(true);

        String url = baseUrl + "/page/" + currentPage;

        presenter.getGirls(url);
    }

    @Override
    public void setGirls(List<Girl> girls) {
        currentPage++;
        showRefreshing(false);
        if (adapter.getData() == null || adapter.getData().size() == 0) {
            adapter.setNewData(girls);
        } else {
            adapter.addData(adapter.getData().size(), girls);
        }
    }

    @Override
    public void getGirlsFalse() {
        isLoading = false;
        showRefreshing(false);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.releaseSubscriber();
    }
}
