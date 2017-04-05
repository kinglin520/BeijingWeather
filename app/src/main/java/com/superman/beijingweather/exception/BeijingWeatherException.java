package com.superman.beijingweather.exception;

/**
 * Created by wenlin on 2017/2/16.
 */

public class BeijingWeatherException extends RuntimeException {
    public String code;
    public String msg;

    public BeijingWeatherException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
