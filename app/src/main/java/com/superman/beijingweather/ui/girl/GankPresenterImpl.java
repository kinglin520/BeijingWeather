package com.superman.beijingweather.ui.girl;

import com.superman.beijingweather.httpserver.api.GirlsController;
import com.superman.beijingweather.model.Gank;
import com.superman.beijingweather.model.JiandanXXOO;
import com.superman.beijingweather.ui.BaseSubscriber;

import java.util.List;

/**
 * Created by wenlin on 2017/2/17.
 */

public class GankPresenterImpl implements GankPresenter {
    private GankFragmentView view;

    public GankPresenterImpl(GankFragmentView view) {
        this.view = view;
    }

    @Override
    public void getGank(String page) {
        GirlsController.getGankGrils(page).subscribe(new GankGirlsSubscriber());
    }

    @Override
    public void getJianDan(String page) {
        GirlsController.getJianDan(page).subscribe(new JianDanSubscriber());
    }

    private class GankGirlsSubscriber extends BaseSubscriber {
        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            view.getListGankFalse();
        }

        @Override
        public void onNext(Object o) {
            super.onNext(o);
            view.setListGank((List<Gank>) o);
        }
    }

    private class JianDanSubscriber extends BaseSubscriber {
        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            view.getListJianDanFalse();
        }

        @Override
        public void onNext(Object o) {
            super.onNext(o);
            view.setListJianDan((List<JiandanXXOO>) o);
        }
    }
}
