package com.superman.beijingweather.ui.girl.nestimageset.personalpictureset;

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

public class PersonalPictureSetPresenterImpl implements PersonalPictureSetPresenter {

    private PersonalPictureSetView view;

    private Subscription getPageSubscription, getPicSubscription;
    private int totalPages = 1;
    private String baseUrl = "";

    public PersonalPictureSetPresenterImpl(PersonalPictureSetView view) {
        this.view = view;
    }

    @Override
    public void getPersonalGirls(String url) {
        baseUrl = url;
        getPageSubscription = Observable.just(baseUrl).subscribeOn(Schedulers.io()).map(new Func1<String, List<Girl>>() {
            @Override
            public List<Girl> call(String url) {
                List<Girl> girls = new ArrayList<>();
                try {
                    Document doc = Jsoup.connect(url).timeout(10000).get();
                    Element total = doc.select("div.pagenavi").first();
                    Elements spans = total.select("span");
                    for (Element s : spans) {
                        int page;
                        try {
                            page = Integer.parseInt(s.text());
                            if (page >= totalPages)
                                totalPages = page;
                        } catch (Exception e) {

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return girls;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Girl>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                view.getGirlsError();
            }

            @Override
            public void onNext(List<Girl> girls) {
                for (int i = 1; i <= totalPages; i++) {
                    getGrilsFromServer(i);
                }
            }
        });
    }

    private void getGrilsFromServer(int page) {

        String url = baseUrl + "/" + page;

        final String fakeRefer = baseUrl + "/"; //伪造 refer 破解防盗链
        final String realUrl = "http://api.caoliyu.cn/meizitu.php?url=%s&refer=%s";// 然后用自己的服务器进行转发

        getPicSubscription = Observable.just(url).subscribeOn(Schedulers.io()).map(new Func1<String, List<Girl>>() {
            @Override
            public List<Girl> call(String url) {
                List<Girl> girls = new ArrayList<>();
                try {
                    Document doc = Jsoup.connect(url).timeout(10000).get();
                    Element total = doc.select("div.main-image").first();
                    String s = total.select("img").first().attr("src");
//                    girls.add(new Girl(s));
                    girls.add(new Girl(String.format(realUrl, s, fakeRefer)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return girls;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Girl>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                view.getGirlsError();
            }

            @Override
            public void onNext(List<Girl> girls) {
                view.setPersonalGirls(girls);
            }
        });
    }

    @Override
    public void releaseSubscriber() {
        if (getPageSubscription != null && !getPageSubscription.isUnsubscribed())
            getPageSubscription.unsubscribe();
        if (getPicSubscription != null && !getPicSubscription.isUnsubscribed())
            getPicSubscription.unsubscribe();
    }
}
