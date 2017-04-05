package com.superman.beijingweather.httpserver.api;


import com.superman.beijingweather.httpserver.BaseGankResponse;
import com.superman.beijingweather.httpserver.BaseJiandanResponse;
import com.superman.beijingweather.model.Gank;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 干货查询的接口
 * Created by wenlin on 2017/2/16.
 */

public interface GirlsService {
    @GET("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/20/{page}")
    Observable<BaseGankResponse<List<Gank>>> getGank(@Path("page") String page);

    @GET("http://i.jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments")
    Observable<BaseJiandanResponse> getXXOO(@Query("page") int page);

}
