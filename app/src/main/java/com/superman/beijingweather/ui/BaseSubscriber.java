package com.superman.beijingweather.ui;

import com.google.gson.JsonParseException;
import com.superman.beijingweather.exception.BeijingWeatherException;
import com.superman.beijingweather.utils.L;
import com.superman.beijingweather.utils.ToastUtil;

import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;


/**
 * Created by wenlin on 2017/2/16.
 */

public class BaseSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof BeijingWeatherException) {
            L.e("code = " + ((BeijingWeatherException) e).code + "---" + ((BeijingWeatherException) e).msg);
//            ToastUtil.show(((BloodSugarException) e).msg);
            switch (((BeijingWeatherException) e).code) {
                case "1":
                    ToastUtil.show("数据库中没有数据");
                    break;
                case "2":
                    ToastUtil.show("服务器错误");
                    break;
                case "3":
                    ToastUtil.show("用户已存在");
                    break;
                case "4":
                    ToastUtil.show("密码错误");
                    break;
                case "5":
                    ToastUtil.show("用户名不存在");
                    break;
                case "6":
                    ToastUtil.show("参数错误");
                    break;
                case "14":
                    ToastUtil.show("用户失效");
                    break;

            }

//            Code 1 not found数据库中没有数据
//            Code 2 server error服务器错误
//            Code 3 user already exist用户已存在
//            Code 4 passwd error密码错误
//            Code 5 user error用户错误
//            Code 6 parameter error 参数错误
//            Code 14 user invalid 用户失效
        } else {
            if (e instanceof HttpException) {
                ToastUtil.show("服务器错误");
            } else if (e instanceof JsonParseException) {
                ToastUtil.show("数据解析错误");
            } else if (e instanceof SocketTimeoutException) {
                ToastUtil.show("网络超时");
            } else {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNext(T o) {

    }


}
