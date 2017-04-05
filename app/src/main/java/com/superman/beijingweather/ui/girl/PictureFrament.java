package com.superman.beijingweather.ui.girl;

import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.superman.beijingweather.R;
import com.superman.beijingweather.ui.base.BaseFragment;
import com.superman.beijingweather.utils.IEventListener;
import com.superman.beijingweather.utils.IEventType;

import uk.co.senab.photoview.PhotoViewAttacher;


public class PictureFrament extends BaseFragment {

    private uk.co.senab.photoview.PhotoView mImageView;

    private String url;

    private IEventListener eventListener;

    public void setEventListener(IEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_picture;
    }

    @Override
    protected void initViews() {
        url = getArguments().getString("url");
        mImageView = findView(R.id.picture);
    }

    @Override
    protected void lazyFetchData() {
        Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).priority(Priority.IMMEDIATE).crossFade(0)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(mImageView);

        mImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                try {
                    eventListener.eventProcess(IEventType.GIRL_PHOTO_CLICK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });
    }
}
