package com.superman.beijingweather.event;


import com.superman.beijingweather.model.Girl;

import java.util.List;


public class GirlsComingEvent {

    public static final int GIRLS_FROM_GANK = 0;
    public static final int GIRLS_FROM_JIANDAN = 1;
    public static final int GIRLS_FROM_MZITU = 2;
    public static final int GIRLS_FROM_MZITU_ZIPAI = 3;

    private List<Girl> girls;
    private int from;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public GirlsComingEvent(int from, List<Girl> girls) {
        this.girls = girls;
        this.from = from;
    }

    public List<Girl> getGirls() {
        return girls;
    }

    public void setGirls(List<Girl> girls) {
        this.girls = girls;
    }
}
