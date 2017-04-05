package com.superman.beijingweather.ui.setting;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.superman.beijingweather.R;
import com.superman.beijingweather.event.ThemeChangedEvent;
import com.superman.beijingweather.ui.base.BaseActivity;
import com.superman.beijingweather.utils.SettingsUtil;
import com.superman.beijingweather.utils.ThemeUtil;

import org.greenrobot.eventbus.EventBus;


public class SettingActivity extends BaseActivity implements ColorChooserDialog.ColorCallback {

    Toolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected int getMenuId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setDisplayHomeAsUpEnabled(true);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
    }

    @Override
    protected void loadData() {
        getFragmentManager().beginTransaction().replace(R.id.contentLayout, new SettingFragment()).commit();
        toolbar.setTitle("设置");
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        if (selectedColor == ThemeUtil.getThemeColor(this, R.attr.colorPrimary))
            return;
        toolbar.setBackgroundColor(selectedColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(selectedColor);
        }
        if (selectedColor == getResources().getColor(R.color.lapis_blue)) {
            setTheme(R.style.LapisBlueTheme);
            SettingsUtil.setTheme(0);
        } else if (selectedColor == getResources().getColor(R.color.pale_dogwood)) {
            setTheme(R.style.PaleDogwoodTheme);
            SettingsUtil.setTheme(1);
        } else if (selectedColor == getResources().getColor(R.color.greenery)) {
            setTheme(R.style.GreeneryTheme);
            SettingsUtil.setTheme(2);
        } else if (selectedColor == getResources().getColor(R.color.primrose_yellow)) {
            setTheme(R.style.PrimroseYellowTheme);
            SettingsUtil.setTheme(3);
        } else if (selectedColor == getResources().getColor(R.color.flame)) {
            setTheme(R.style.FlameTheme);
            SettingsUtil.setTheme(4);
        } else if (selectedColor == getResources().getColor(R.color.island_paradise)) {
            setTheme(R.style.IslandParadiseTheme);
            SettingsUtil.setTheme(5);
        } else if (selectedColor == getResources().getColor(R.color.kale)) {
            setTheme(R.style.KaleTheme);
            SettingsUtil.setTheme(6);
        } else if (selectedColor == getResources().getColor(R.color.pink_yarrow)) {
            setTheme(R.style.PinkYarrowTheme);
            SettingsUtil.setTheme(7);
        } else if (selectedColor == getResources().getColor(R.color.niagara)) {
            setTheme(R.style.NiagaraTheme);
            SettingsUtil.setTheme(8);
        }
        getFragmentManager().beginTransaction().replace(R.id.contentLayout, new SettingFragment()).commit();
        EventBus.getDefault().post(new ThemeChangedEvent(selectedColor));
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }

}
