package com.superman.beijingweather.httpserver.retrofit;

import com.google.gson.Gson;
import com.superman.beijingweather.exception.BeijingWeatherException;
import com.superman.beijingweather.httpserver.BaseResponse;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;


final class TGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    TGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            //ResultResponse 只解析result字段
            BaseResponse resultResponse = gson.fromJson(response, BaseResponse.class);
            String code = String.valueOf(resultResponse.code);
//            Integer code = Integer.valueOf(resultResponse.code);
            if (code.equalsIgnoreCase("200")) {
                return gson.fromJson(response, type);
            } else {
                throw new BeijingWeatherException(code, resultResponse.message);
            }
        } finally {
        }
    }
}