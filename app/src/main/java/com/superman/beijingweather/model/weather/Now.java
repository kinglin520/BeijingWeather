package com.superman.beijingweather.model.weather;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Now implements MultiItemEntity , Serializable {
    private int itemType = 0 ;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    @Override
    public int getItemType() {
        return Weather.TYPE_NOW;
    }

    public class More implements Serializable {

        @SerializedName("txt")
        public String info;

    }

}
