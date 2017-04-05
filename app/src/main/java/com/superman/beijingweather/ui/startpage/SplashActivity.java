package com.superman.beijingweather.ui.startpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.superman.beijingweather.R;
import com.superman.beijingweather.ui.MainActivity;

import java.lang.ref.SoftReference;

/**
 * Created by wenlin on 2017/2/16.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        if (savedInstanceState == null) {
            new Handler().postDelayed(new HandlerLogin(this), 1000);
        }
    }

    private static class HandlerLogin implements Runnable {
        private final SoftReference<SplashActivity> sfActivity;

        public HandlerLogin(SplashActivity loginActivity) {
            this.sfActivity = new SoftReference<>(loginActivity);
        }

        @Override
        public void run() {
            if (sfActivity.get() != null) {
                SplashActivity activity = sfActivity.get();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }
    }
}
