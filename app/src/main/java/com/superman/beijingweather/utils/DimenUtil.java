package com.superman.beijingweather.utils;

import com.superman.beijingweather.App;

/**
 * @version 1.0
 * Created by Yoosir on 2016/11/15 0015.
 */
public class DimenUtil {

    public static float dp2px(float dp) {
        final float scale = App.getContext().getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(float sp) {
        final float scale = App.getContext().getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int getScreenSize() {
        return App.getContext().getResources().getDisplayMetrics().widthPixels;
    }

}
