package com.superman.beijingweather.ui.news.photodetail;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.superman.beijingweather.R;
import com.superman.beijingweather.model.news.NewsPhotoDetail;
import com.superman.beijingweather.ui.base.BaseActivity;
import com.superman.beijingweather.widgets.HackyViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by wenlin on 2017/3/10.
 */

public class NewsPhotoDetailActivity extends BaseActivity {

    private static final String PHOTO_DETAIL = "PHOTO_DETAIL";
    @BindView(R.id.photo_viewpager)
    HackyViewPager photoViewpager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.photo_count_tv)
    TextView photoCountTv;
    @BindView(R.id.photo_news_title_tv)
    TextView photoNewsTitleTv;
    @BindView(R.id.photo_news_desc_tv)
    TextView photoNewsDescTv;
    @BindView(R.id.photo_text_layout)
    FrameLayout photoTextLayout;
    @BindView(R.id.activity_news_photo_detail)
    FrameLayout activityNewsPhotoDetail;


    private NewsPhotoDetail mNewsPhotoDetail;
    private List<NewsPhotoDetail.PictureItem> mPictureList;

    private boolean isHidden = false;

    public static Intent getNewsDetailIntent(Context context, NewsPhotoDetail newsPhotoDetail) {
        Intent intent = new Intent(context, NewsPhotoDetailActivity.class);
        intent.putExtra(PHOTO_DETAIL, newsPhotoDetail);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_photo_detail;
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
    }

    @Override
    protected void loadData() {
        mNewsPhotoDetail = getIntent().getParcelableExtra(PHOTO_DETAIL);
        mPictureList = mNewsPhotoDetail.getPictureItemList();
        toolbar.setTitle("图集");

        photoNewsTitleTv.setText(mNewsPhotoDetail.getTitle());
        photoNewsDescTv.setText(mPictureList.get(0).getDescription());
        photoCountTv.setText("1/" + mPictureList.size());
        photoViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                photoNewsDescTv.setText(mPictureList.get(position).getDescription());
                photoCountTv.setText((position + 1) + "/" + mPictureList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        photoViewpager.setAdapter(new PhotoPagerAdapter());
    }


    public void hideToolBarAndTextView() {
        isHidden = !isHidden;
        if (isHidden) {
            startAnimation(true, 1.0f, 0.0f);
        } else {
            startAnimation(false, 0.1f, 1.0f);
        }
    }

    private void startAnimation(final boolean endState, float startValue, float endValue) {
        ValueAnimator animator = ValueAnimator.ofFloat(startValue, endValue).setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float y1, y2;
                if (endState) {
                    y1 = (0 - animation.getAnimatedFraction()) * toolbar.getHeight();
                    y2 = animation.getAnimatedFraction() * photoTextLayout.getHeight();
                } else {
                    y1 = (animation.getAnimatedFraction() - 1) * toolbar.getHeight();
                    y2 = (1 - animation.getAnimatedFraction()) * photoTextLayout.getHeight();
                }
                toolbar.setTranslationY(y1);
                photoTextLayout.setTranslationY(y2);
            }
        });
        animator.start();
    }

    private void hideOrShowStatusBar() {
        if (getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
            hideSystemUI();
        } else {
            showSystemUI();
            getSupportActionBar().show();
        }
    }

    class PhotoPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPictureList == null ? 0 : mPictureList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            PhotoView photoView = new PhotoView(NewsPhotoDetailActivity.this);
            Glide.with(NewsPhotoDetailActivity.this).load(mPictureList.get(position).getImgPath())
                    .placeholder(R.mipmap.ic_loading)
                    .error(R.mipmap.ic_load_fail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photoView);
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float v, float v1) {
                    hideToolBarAndTextView();
                    hideOrShowStatusBar();
                }

                @Override
                public void onOutsidePhotoTap() {

                }
            });
            container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
            return photoView;
        }
    }


}
