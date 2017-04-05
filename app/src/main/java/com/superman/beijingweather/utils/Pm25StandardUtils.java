package com.superman.beijingweather.utils;

/**
 * Created by wenlin on 2017/2/21.
 */

public class Pm25StandardUtils {
    public static String pmValueToChn(String apiString) {
        String tem = "";
        int api = Integer.valueOf(apiString);
        if (api > 0 && api <= 35) {
            tem = "优";
        } else if (api > 35 && api <= 75) {
            tem = "良";
        } else if (api > 75 && api <= 115) {
            tem = "轻度污染";
        } else if (api > 115 && api < 150) {
            tem = "中度污染";
        } else if (api > 150 && api <= 250) {
            tem = "重度污染";
        } else if (api > 250 && api < 500) {
            tem = "严重污染";
        }
        return tem;
    }
}
