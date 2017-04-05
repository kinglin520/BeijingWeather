package com.superman.beijingweather.httpserver;

import com.superman.beijingweather.model.JiandanXXOO;

import java.util.List;

/**
 * Created by wenlin on 2017/2/16.
 */

public class BaseJiandanResponse {
    public String status;
    public int current_page;
    public int total_comments;
    public int page_count;
    public int count;
    public List<JiandanXXOO> comments;
}
