package com.superman.beijingweather;

import android.app.Application;
import android.content.Context;

/**
 * Created by wenlin on 2017/2/16.
 */

public class App extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
//        LitePal.initialize(this);
//        if (!BuildConfig.DEBUG) {
//            AppExceptionHandler.getInstance().setCrashHanler(this);
//        }

    }

    public static Context getContext() {
        return mContext;
    }
}
