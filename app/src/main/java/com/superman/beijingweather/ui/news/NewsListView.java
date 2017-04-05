package com.superman.beijingweather.ui.news;

import com.superman.beijingweather.model.news.NewsSummary;

import java.util.List;

/**
 * Created by wenlin on 2017/3/9.
 */

public interface NewsListView {
    void setNewsContent(List<NewsSummary> newsSummaryList);
    void getNewsError();
}
