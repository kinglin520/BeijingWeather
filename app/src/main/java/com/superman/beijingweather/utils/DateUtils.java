package com.superman.beijingweather.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by wenlin on 2016/10/24.
 */
public class DateUtils {


    private static String mYear; // 当前年
    private static String mMonth; // 月
    private static String mDay;
    private static String mWay;


    /**
     * 获取当前日期几月几号
     */
    public static String getDateString() {

        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        if (Integer.parseInt(mDay) > MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth)))) {
            mDay = String.valueOf(MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth))));
        }
        return mMonth + "月" + mDay + "日";
    }


    /**
     * 获取当前年月日
     *
     * @return
     */
    public static String StringData() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        if (Integer.parseInt(mDay) > MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth)))) {
            mDay = String.valueOf(MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (Integer.parseInt(mMonth))));
        }
        return mYear + "-" + (mMonth.length() == 1 ? "0" + mMonth : mMonth) + "-" + (mDay.length() == 1 ? "0" + mDay : mDay);
    }


    /**
     * 根据当前日期获得是星期几
     *
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {


            c.setTime(format.parse(time));


        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "星期天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "星期一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "星期二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "星期三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "星期四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "星期五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "星期六";
        }
        return Week;
    }


    /**
     * 获取今天往后一周的日期（几月几号）
     */
    public static List<String> getSevendate() {
        List<String> dates = new ArrayList<>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        for (int i = 0; i < 7; i++) {
            mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
            mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + i);// 获取当前日份的日期号码
            if (Integer.parseInt(mDay) > MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (i + 1))) {
                mDay = String.valueOf(MaxDayFromDay_OF_MONTH(Integer.parseInt(mYear), (i + 1)));
            }
            String date = mMonth + "月" + mDay + "日";
            dates.add(date);
        }
        return dates;
    }


    /**
     * 获取今天往后一周的集合
     */
    public static List<String> get7week() {
        String week = "";
        List<String> weeksList = new ArrayList<>();
        List<String> dateList = get7date();
        for (String s : dateList) {
            if (s.equals(StringData())) {
                week = "今天";
            } else {
                week = getWeek(s);
            }
            weeksList.add(week);
        }
        return weeksList;
    }


    public static List<String> get7date() {
        List<String> dates = new ArrayList<>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat(
                "yyyy-MM-dd");
        String date = sim.format(c.getTime());
        dates.add(date);
        for (int i = 0; i < 6; i++) {
            c.add(java.util.Calendar.DAY_OF_MONTH, 1);
            date = sim.format(c.getTime());
            dates.add(date);
        }
        return dates;
    }

    /**
     * 得到当年当月的最大日期
     **/
    public static int MaxDayFromDay_OF_MONTH(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
        //本月份的天数
        return time.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static StringBuffer DatestringToString(String s){
        String[] temp = s.split("-");
        if(temp == null || temp.length < 2){
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(temp[0]);
        stringBuffer.append("年");
        stringBuffer.append(temp[1]);
        stringBuffer.append("月");
        stringBuffer.append(temp[2]);
        stringBuffer.append("日");
        return stringBuffer;
    }


    /**
     * 获取参数日期所在一周的日期集合
     *
     * @param mdate
     * @return
     */
    public static List<String> dateToWeek(Date mdate) {
        int b = mdate.getDay();
        Date fDate;
        List<String> list = new ArrayList<>();
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy-MM-dd");
        long fTime = mdate.getTime() - b * 24 * 3600000;
        for (int i = 1; i <= 7; i++) {
            fDate = new Date();
            fDate.setTime(fTime + (i * 24 * 3600000));
            list.add(i - 1, format.format(fDate));
        }
        return list;
    }

    /**
     * 日期加减。
     *
     * @param base 基础日期
     * @param days 增加天数(减天数则用负数)
     * @return 计算结果
     */
    public static Date datePlus(Date base, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(base);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static String getYMDHMTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}