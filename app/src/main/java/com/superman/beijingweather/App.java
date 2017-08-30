package com.superman.beijingweather;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.facebook.stetho.Stetho;

import org.litepal.LitePal;

/**
 * Created by wenlin on 2017/2/16.
 */

public class App extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
//        if (!BuildConfig.DEBUG) {
//            AppExceptionHandler.getInstance().setCrashHanler(this);
//        }
        Utils.init(mContext);
        LitePal.initialize(this);
        Stetho.initializeWithDefaults(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
