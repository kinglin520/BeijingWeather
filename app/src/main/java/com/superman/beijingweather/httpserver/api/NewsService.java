package com.superman.beijingweather.httpserver.api;

import com.superman.beijingweather.model.news.NewsDetail;
import com.superman.beijingweather.model.news.NewsSummary;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

/**
 *  新闻模块 服务接口
 * @version 1.0
 */
public interface NewsService {

    @GET("http://c.m.163.com/nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String ,List<NewsSummary>>> getNewsList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type,
            @Path("id") String id,
            @Path("startPage") int startPage);

    @GET("http://c.m.163.com/nc/article/{postId}/full.html")
    Observable<Map<String,NewsDetail>> getNewsDetail(
            @Header("Cache-Control") String cacheControl,
            @Path("postId") String postId
    );

}
