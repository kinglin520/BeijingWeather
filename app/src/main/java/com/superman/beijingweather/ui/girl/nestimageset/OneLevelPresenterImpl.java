package com.superman.beijingweather.ui.girl.nestimageset;

import com.superman.beijingweather.model.Girl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wenlin on 2017/2/28.
 */

public class OneLevelPresenterImpl implements OneLevelPresenter {

    private OneLevelView view;

    private Subscription subscription;

    public OneLevelPresenterImpl(OneLevelView view) {
        this.view = view;
    }

    @Override
    public void getGirls(String url) {
        subscription = Observable.just(url).subscribeOn(Schedulers.io()).map(new Func1<String, List<Girl>>() {
            @Override
            public List<Girl> call(String url) {
                List<Girl> girls = new ArrayList<>();
                try {
                    Document doc = (Document) Jsoup.connect(url).timeout(10000).get();
                    Element total = doc.select("div.postlist").first();
                    Elements items = total.select("li");
                    for (Element element : items) {
                        Girl girl = new Girl(element.select("img").first().attr("data-original"));
                        girl.setLink(element.select("a[href]").attr("href"));
                        girls.add(girl);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return girls;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Girl>>(){
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {
                view.getGirlsFalse();
            }
            @Override
            public void onNext(List<Girl> girls) {
                view.setGirls(girls);
            }
        });
    }

    @Override
    public void releaseSubscriber() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
