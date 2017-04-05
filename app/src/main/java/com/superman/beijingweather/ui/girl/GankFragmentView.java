package com.superman.beijingweather.ui.girl;

import com.superman.beijingweather.model.Gank;
import com.superman.beijingweather.model.JiandanXXOO;

import java.util.List;

/**
 * Created by wenlin on 2017/2/17.
 */

public interface GankFragmentView {
    void setListGank(List<Gank> ganks);
    void getListGankFalse();

    void setListJianDan(List<JiandanXXOO>jianDan);
    void getListJianDanFalse();
}
