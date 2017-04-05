package com.superman.beijingweather.httpserver.api;

import com.superman.beijingweather.httpserver.ApiFactory;
import com.superman.beijingweather.model.news.NewsDetail;
import com.superman.beijingweather.model.news.NewsSummary;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wenlin on 2017/3/6.
 */

public class NewsController {

    private final static NewsService newsService = ApiFactory.getNewsService();

    public static Observable<List<NewsSummary>> getNewsList(String cacheControl, String type, final String id, int startPage) {
        return handThread(newsService.getNewsList(cacheControl, type, id, startPage)).map(new Func1<Map<String, List<NewsSummary>>, List<NewsSummary>>() {
            @Override
            public List<NewsSummary> call(Map<String, List<NewsSummary>> stringListMap) {
                return stringListMap.get(id);
            }
        });
    }

    public static Observable<NewsDetail> getNewsDetails(String cacheControl, final String postId) {
        return handThread(newsService.getNewsDetail(cacheControl, postId)).map(new Func1<Map<String, NewsDetail>, NewsDetail>() {
            @Override
            public NewsDetail call(Map<String, NewsDetail> stringNewsDetailMap) {
                return stringNewsDetailMap.get(postId);
            }
        });
    }

    private static <T> Observable<T> handThread(Observable<T> obs) {
        return obs.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
