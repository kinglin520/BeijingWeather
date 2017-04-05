package com.superman.beijingweather.ui.news.photodetail;

import com.superman.beijingweather.httpserver.api.NewsController;
import com.superman.beijingweather.model.news.NewsDetail;
import com.superman.beijingweather.ui.BaseSubscriber;

/**
 * Created by wenlin on 2017/3/10.
 */

public class NewsDetailPresenterImpl implements NewsDetailPresenter {

    private NewsDetailView view;

    public NewsDetailPresenterImpl(NewsDetailView view) {
        this.view = view;
    }

    @Override
    public void getNewsDetail(String postId) {
        NewsController.getNewsDetails("", postId).subscribe(new NewsSubscriber());
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
            view.setNewsDetail((NewsDetail) o);
        }
    }
}
