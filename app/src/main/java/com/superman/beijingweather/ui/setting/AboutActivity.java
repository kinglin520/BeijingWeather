package com.superman.beijingweather.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.superman.beijingweather.R;
import com.superman.beijingweather.ui.base.BaseActivity;
import com.superman.beijingweather.ui.weather.SimpleSubscriber;
import com.superman.beijingweather.utils.FileUtil;
import com.superman.beijingweather.utils.PackageUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.superman.beijingweather.utils.FileUtil.getFileDir;


public class AboutActivity extends BaseActivity {

    private TextView tvVersion;
    private ImageSwitcher imageSwitcher;
    private String[] imageUrls = {
            "http://7xp1a1.com1.z0.glb.clouddn.com/liyu01.png",
            "http://7xp1a1.com1.z0.glb.clouddn.com/liyu02.png",
            "http://7xp1a1.com1.z0.glb.clouddn.com/liyu03.png",
            "http://7xp1a1.com1.z0.glb.clouddn.com/liyu04.png",
            "http://7xp1a1.com1.z0.glb.clouddn.com/liyu05.png",
            "https://avatars2.githubusercontent.com/u/13759787?s=400&u=309598350141229ebfde6c7d229437e8f2f26181&v=4"};

    private Subscription subscription;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected int getMenuId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setDisplayHomeAsUpEnabled(true);
        tvVersion = (TextView) findViewById(R.id.tv_app_version);
//        tvVersion.setText("v" + BuildConfig.VERSION_NAME);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(AboutActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.zoom_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.zoom_out));

        tvVersion.setText(PackageUtils.getVersionName(this)+"("+PackageUtils.getVersionCode(this)+")");

    }

    private void loadImage() {
        Glide.with(this).load(imageUrls[new Random().nextInt(6)]).into(new SimpleTarget<GlideDrawable>(imageSwitcher.getWidth(), imageSwitcher.getHeight()) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                imageSwitcher.setImageDrawable(resource);
            }
        });
    }

    @Override
    protected void loadData() {
        imageSwitcher.post(new Runnable() {
            @Override
            public void run() {
                loadImage();
            }
        });
        subscription = Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        loadImage();
                    }
                });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_web_home:
//                openWeb("https://github.com/li-yu/FakeWeather");
                break;
            case R.id.btn_feedback:
//                feedBack();
                break;
            case R.id.btn_check_update:
//                UpdateUtil.check(AboutActivity.this, false);
                break;
            case R.id.btn_share_app:
//                ShareUtils.shareText(this, "来不及了，赶紧上车！");
                break;
        }
    }

    private void openWeb(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    private void feedBack() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null));
        intent.putExtra(Intent.EXTRA_EMAIL, "");
        intent.putExtra(Intent.EXTRA_SUBJECT, "反馈");
        intent.putExtra(Intent.EXTRA_TEXT, FileUtil.readFile(getFileDir("Log/crash.log")));
        startActivity(Intent.createChooser(intent, "反馈"));
    }

}
