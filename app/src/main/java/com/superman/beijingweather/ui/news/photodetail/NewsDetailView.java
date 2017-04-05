package com.superman.beijingweather.ui.news.photodetail;

import com.superman.beijingweather.model.news.NewsDetail;

/**
 * Created by wenlin on 2017/3/10.
 */

public interface NewsDetailView  {
    void setNewsDetail(NewsDetail newsDetail);
    void getNewsError();
}
