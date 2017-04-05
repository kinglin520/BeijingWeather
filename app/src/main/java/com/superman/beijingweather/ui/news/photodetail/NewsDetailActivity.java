package com.superman.beijingweather.ui.news.photodetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.superman.beijingweather.R;
import com.superman.beijingweather.model.news.NewsDetail;
import com.superman.beijingweather.ui.base.BaseActivity;
import com.superman.beijingweather.widgets.phototext.PhotoTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wenlin on 2017/3/10.
 */

public class NewsDetailActivity extends BaseActivity implements NewsDetailView{

    private static final String NEWS_POST_ID = "NEWS_POST_ID";
    private static final String NEWS_IMG_RES = "NEWS_IMG_RES";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String mPostId;
    private String mPostImgPath;
    private String mShareLink;
    private String mNewsTitle;

    @BindView(R.id.news_detail_picture_iv)
    ImageView newsDetailPictureIv;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.news_content_from)
    TextView newsContentFrom;
    @BindView(R.id.news_content_text)
    PhotoTextView newsContentText;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private NewsDetailPresenter presenter;

    public static Intent getNewsDetailIntent(Context context, String postId, String postImgPath) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(NEWS_POST_ID, postId);
        intent.putExtra(NEWS_IMG_RES, postImgPath);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected int getMenuId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        showSystemUI();
        setDisplayHomeAsUpEnabled(true);

        presenter = new NewsDetailPresenterImpl(this);

        toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
        toolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));

    }

    @Override
    protected void loadData() {
        mPostId = getIntent().getStringExtra(NEWS_POST_ID);
        mPostImgPath = getIntent().getStringExtra(NEWS_IMG_RES);

        presenter.getNewsDetail(mPostId);

        Glide.with(this).load(mPostImgPath).asBitmap()
                .placeholder(R.mipmap.ic_loading)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .error(R.mipmap.ic_load_fail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(newsDetailPictureIv);
    }

    @OnClick(R.id.fab)
    public void onClick(View v){
        if(v.getId() == R.id.fab){
            share();
        }
    }
    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share));
        intent.putExtra(Intent.EXTRA_TEXT, getShareContents());
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    @NonNull
    private String getShareContents() {
        if (mShareLink == null) {
            mShareLink = "";
        }
        return getString(R.string.share_contents, mNewsTitle, mShareLink);
    }
    @Override
    protected void onDestroy() {
        if (newsContentText != null) {
            newsContentText.cancelImageGetterSubscription();
        }
        super.onDestroy();
    }

    @Override
    public void setNewsDetail(NewsDetail newsDetail) {
        mShareLink = newsDetail.getShareLink();
        mNewsTitle = newsDetail.getTitle();
        toolbarLayout.setTitle(mNewsTitle);
        newsContentText.setText(getString(R.string.news_content_from_value,newsDetail.getSource(),newsDetail.getPtime()));
        newsContentText.setContainText(newsDetail.getBody(),false);
    }

    @Override
    public void getNewsError() {

    }
}
