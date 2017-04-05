package com.superman.beijingweather.httpserver.api;

import com.superman.beijingweather.httpserver.ApiFactory;
import com.superman.beijingweather.httpserver.BaseGankResponse;
import com.superman.beijingweather.httpserver.BaseJiandanResponse;
import com.superman.beijingweather.model.Gank;
import com.superman.beijingweather.model.JiandanXXOO;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wenlin on 2017/2/16.
 */

public class GirlsController {

    private static final GirlsService girlsService = ApiFactory.getGirlsService();


    public static Observable<List<Gank>> getGankGrils(String page){
        return handThread(girlsService.getGank(page)).map(new Func1<BaseGankResponse<List<Gank>>, List<Gank>>() {
            @Override
            public List<Gank> call(BaseGankResponse<List<Gank>> listBaseGankResponse) {
                return listBaseGankResponse.results;
            }
        });
    }

    public static Observable<List<JiandanXXOO>> getJianDan(String page){
        return handThread(girlsService.getXXOO(Integer.valueOf(page))).map(new Func1<BaseJiandanResponse, List<JiandanXXOO>>() {
            @Override
            public List<JiandanXXOO> call(BaseJiandanResponse baseJiandanResponse) {
                return baseJiandanResponse.comments;
            }
        });
    }

    private static <T> Observable<T> handThread(Observable<T> obs) {
        return obs.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

}

