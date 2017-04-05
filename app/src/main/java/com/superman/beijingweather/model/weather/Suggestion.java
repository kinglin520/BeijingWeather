package com.superman.beijingweather.model.weather;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Suggestion implements MultiItemEntity , Serializable {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;
    @SerializedName("air")
    public Air air;

    private int itemType = 0 ;

    public class Air implements Serializable {
        /**
         * 空气状况
         */
        public String brf;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public class Comfort implements Serializable {

        @SerializedName("txt")
        public String info;
        @SerializedName("brf")
        public String brf;

    }

    public class CarWash implements Serializable {

        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brf;
    }

    public class Sport implements Serializable {

        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brf;
    }

}
