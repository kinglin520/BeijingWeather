package com.superman.beijingweather.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.superman.beijingweather.R;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    private boolean isViewPrepared; // 标识fragment视图已经初始化完毕
    private boolean hasFetchData; // 标识已经触发过懒加载数据

    protected View mRootView;
    protected Activity activity;
    protected abstract
    @LayoutRes
    int getLayoutId();

    protected abstract void initViews();

    protected abstract void lazyFetchData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    private void lazyFetchDataIfPrepared() {
        if (getUserVisibleHint() && !hasFetchData && isViewPrepared) {
            hasFetchData = true;
            lazyFetchData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyFetchDataIfPrepared();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        initViews();
        isViewPrepared = true;
        lazyFetchDataIfPrepared();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hasFetchData = false;
        isViewPrepared = false;
    }

    public <V extends View> V findViewById(View view, int id) {
        return (V) view.findViewById(id);
    }
    protected <T extends View> T findView(@IdRes int id) {
        return (T) mRootView.findViewById(id);
    }

    public void initEmpty(ListView listview, String text, int drawableId) {
        try {
            if (listview.getEmptyView() == null) {
                View emptyVIew = View.inflate(activity, R.layout.listview_empty_layout, null);
                TextView emptyText = findViewById(emptyVIew, R.id.empty_text);
                ImageView emptyImage = findViewById(emptyVIew, R.id.empty_image);
                emptyText.setText(text);
                if (drawableId != 0) {
                    emptyImage.setBackgroundResource(drawableId);
                } else {
                    emptyImage.setBackgroundResource(R.drawable.dummy_status);
                }
                ((ViewGroup) listview.getParent()).addView(emptyVIew);
                listview.setEmptyView(emptyVIew);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
