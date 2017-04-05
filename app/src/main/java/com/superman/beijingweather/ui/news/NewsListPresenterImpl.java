package com.superman.beijingweather.ui.news;

import com.superman.beijingweather.httpserver.api.NewsController;
import com.superman.beijingweather.model.news.NewsSummary;
import com.superman.beijingweather.ui.BaseSubscriber;

import java.util.List;
import java.util.Map;

/**
 * Created by wenlin on 2017/3/9.
 */

public class NewsListPresenterImpl implements NewsListPresenter {
    private NewsListView view;

    public NewsListPresenterImpl(NewsListView view) {
        this.view = view;
    }

    @Override
    public void getNewsList(String cacheControl, String type, String id, int startPage) {
        NewsController.getNewsList(cacheControl, type, id, startPage).subscribe(new NewsSubscriber());
    }

    private class NewsSubscriber extends BaseSubscriber {
        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            view.getNewsError();
        }

        @Override
        public void onNext(Object o) {
            super.onNext(o);
            view.setNewsContent((List<NewsSummary>) o);
        }
    }
}
