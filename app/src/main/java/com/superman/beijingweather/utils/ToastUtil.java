package com.superman.beijingweather.utils;

import android.widget.Toast;

import com.superman.beijingweather.App;

/**
 * Created by wenlin on 2017/2/16.
 */

public class ToastUtil {
    // 单例模式 避免频繁多条弹出提示
    private static Toast toast;
    public static void show( String info) {
        if(toast == null){
            toast = Toast.makeText(App.mContext, info, Toast.LENGTH_SHORT);
        }else {
            toast.setText(info);
        }
        toast.show();
    }

    public static void show( int info) {
        if(toast == null){
            toast = Toast.makeText(App.mContext, info, Toast.LENGTH_SHORT);
        }else {
            toast.setText(info);
        }
        toast.show();
    }

    public static void showLong(String message) {
        if(toast == null){
            toast = Toast.makeText(App.mContext, message, Toast.LENGTH_LONG);
        }else {
            toast.setText(message);
        }
        toast.show();
    }
}
