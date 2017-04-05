package com.superman.beijingweather.ui.girl.nestimageset.personalpictureset;

import com.superman.beijingweather.model.Girl;

import java.util.List;

/**
 * Created by wenlin on 2017/2/28.
 */

public interface PersonalPictureSetView {
    void setPersonalGirls(List<Girl> girls);
    void getGirlsError();
}
