package com.superman.beijingweather.model.weather;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class AQI implements MultiItemEntity , Serializable {

    public AQICity city;

    private int itemType = 0 ;

    public class AQICity implements Serializable {

        public String aqi;

        public String pm25;

        public String qlty;

    }
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
