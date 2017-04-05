package com.superman.beijingweather.ui.news;

/**
 * Created by wenlin on 2017/3/9.
 */

public interface NewsListPresenter {
    void getNewsList(String cacheControl, String type, String id, int startPage);
}
