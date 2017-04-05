package com.superman.beijingweather.ui.news;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.superman.beijingweather.Constants;
import com.superman.beijingweather.R;
import com.superman.beijingweather.model.news.NewsPhotoDetail;
import com.superman.beijingweather.model.news.NewsSummary;
import com.superman.beijingweather.ui.base.BaseContentFragment;
import com.superman.beijingweather.ui.listener.MyRecyclerListener;
import com.superman.beijingweather.ui.news.adapter.NewsListAdapter;
import com.superman.beijingweather.ui.news.photodetail.NewsDetailActivity;
import com.superman.beijingweather.ui.news.photodetail.NewsPhotoDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wenlin on 2017/3/6.
 */
public class NewsListFragment extends BaseContentFragment implements MyRecyclerListener, NewsListView {

    private static final String NEWS_CHANNEL_ID = "NEWS_CHANNEL_ID";
    private static final String NEWS_CHANNEL_TYPE = "NEWS_CHANNEL_TYPE";
    private static final String NEWS_CHANNEL_INDEX = "NEWS_CHANNEL_INDEX";
    private static final String NEWS_CHANNEL_FRAGMENT_TYPE = "FRAGMENT_TYPE";

    private String mNewsChannelType;
    private String mNewsChannelId;
    private int mNewsChannelIndex = -1;
    private String mNewsChannelFragmentType;
    private int currentPage = 1;
    @BindView(R.id.news_rv)
    RecyclerView newsRv;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private NewsListPresenter presenter;

    public static NewsListFragment newInstance(String fragmentType, String channelType, String channelId, int channelIndex) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(NEWS_CHANNEL_TYPE, channelType);
        args.putString(NEWS_CHANNEL_ID, channelId);
        args.putInt(NEWS_CHANNEL_INDEX, channelIndex);
        args.putString(NEWS_CHANNEL_FRAGMENT_TYPE, fragmentType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_list;
    }


    @Override
    protected void lazyFetchData() {
        currentPage = 0;
        mNewsChannelIndex = 0;
        loadNewsFromService();
    }

    @Override
    protected void initViews() {
        super.initViews();
        if (getArguments() != null) {
            mNewsChannelType = getArguments().getString(NEWS_CHANNEL_TYPE);
            mNewsChannelId = getArguments().getString(NEWS_CHANNEL_ID);
            mNewsChannelIndex = getArguments().getInt(NEWS_CHANNEL_INDEX);
            mNewsChannelFragmentType = getArguments().getString(NEWS_CHANNEL_FRAGMENT_TYPE);
        }
        mAdapter = new NewsListAdapter(null);

        presenter = new NewsListPresenterImpl(this);

        initRecyclerView();
    }

    private void loadNewsFromService() {
        showRefreshing(true);
        presenter.getNewsList(mNewsChannelFragmentType, mNewsChannelType, mNewsChannelId, mNewsChannelIndex);
    }

    private boolean isLoading = false;
    private NewsListAdapter mAdapter;

    private void initRecyclerView() {

        final int leftRightPadding = getResources().getDimensionPixelSize(R.dimen.padding_size_l);
        final int topBottomPadding = getResources().getDimensionPixelOffset(R.dimen.padding_size_s);

        newsRv.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        newsRv.setLayoutManager(mLayoutManager);
        newsRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(leftRightPadding, topBottomPadding, leftRightPadding, topBottomPadding);
            }
        });

        newsRv.setItemAnimator(new DefaultItemAnimator());
        newsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

//                int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
//                        .findLastVisibleItemPosition();
//                int visibleItemCount = layoutManager.getChildCount();
//                int totalItemCount = layoutManager.getItemCount();

                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    isLoading = true;
                    mNewsChannelIndex = currentPage * 20;
                    if (mAdapter != null) {
//                        mAdapter.showFooter();
                    }
                    loadNewsFromService();
                    newsRv.scrollToPosition(mAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mAdapter.setOnItemClickListener(this);
        newsRv.setAdapter(mAdapter);
    }

    public void scrollToTop() {
        newsRv.getLayoutManager().scrollToPosition(0);
    }


    @Override
    public void OnItemClickListener(View view, int position) {
        List<NewsSummary> mNewsSummaryList = mAdapter.getList();
        NewsSummary newsSummary = mNewsSummaryList.get(position);
        if(NewsListAdapter.TYPE_PHOTO_SET == mAdapter.getItemViewType(position)){
            NewsPhotoDetail mNewsPhotoDetail = setNewsPhotoDetail(newsSummary);
            startActivity(NewsPhotoDetailActivity.getNewsDetailIntent(getActivity(),mNewsPhotoDetail));
//            KLog.d(TAG,"postId = " + newsSummary.getPostid() +"--- postSetId= "+ newsSummary.getPhotosetID());
        }else{
            Intent intent = NewsDetailActivity.getNewsDetailIntent(getActivity(),newsSummary.getPostid(),newsSummary.getImgsrc());
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ImageView animationIv = (ImageView) view.findViewById(R.id.news_picture_iv);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                        getActivity(),
                        animationIv,
                        Constants.TRANSITION_ANIMATION_NEWS_PHOTOS);
                startActivity(intent,options.toBundle());
            }else{
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                        view,
                        view.getWidth() / 2,
                        view.getHeight() / 2,
                        0,
                        0);
                ActivityCompat.startActivity(getActivity(),intent,optionsCompat.toBundle());
            }
        }
    }
    private NewsPhotoDetail setNewsPhotoDetail(NewsSummary newsSummary){
        List<NewsSummary.AdsBean> adsBeanList =  newsSummary.getAds();
        List<NewsSummary.ImgextraBean> imgextraBeanList = newsSummary.getImgextra();
        List<NewsPhotoDetail.PictureItem> pictureItemList = new ArrayList<>();
        NewsPhotoDetail mNewsPhotoDetail = new NewsPhotoDetail();
        mNewsPhotoDetail.setTitle(newsSummary.getTitle());

        setValuesAndAddToList(pictureItemList,newsSummary.getTitle(),newsSummary.getImgsrc());
        if(adsBeanList != null) {
            for (NewsSummary.AdsBean adsBean : adsBeanList) {
                setValuesAndAddToList(pictureItemList,adsBean.getTitle(),adsBean.getImgsrc());
            }
        }
        if(imgextraBeanList != null) {
            for (NewsSummary.ImgextraBean imgtra : imgextraBeanList) {
                setValuesAndAddToList(pictureItemList,null,imgtra.getImgsrc());
            }
        }
        mNewsPhotoDetail.setPictureItemList(pictureItemList);
        return mNewsPhotoDetail;
    }

    private void setValuesAndAddToList(List<NewsPhotoDetail.PictureItem> pictureItemList, String description, String imgPath) {
        NewsPhotoDetail.PictureItem picture = new NewsPhotoDetail.PictureItem();
        if (description == null) {
            description = "这是一个描述";
        }
        picture.setDescription(description);
        picture.setImgPath(imgPath);

        pictureItemList.add(picture);
    }

    @Override
    public void setNewsContent(List<NewsSummary> newsSummaryList) {
        showRefreshing(false);
        isLoading = false;
        currentPage++;
        mAdapter.addMore(newsSummaryList);
//        mAdapter.hideFooter();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getNewsError() {
        showRefreshing(false);
        isLoading = false;
//        mAdapter.hideFooter();
    }
}
