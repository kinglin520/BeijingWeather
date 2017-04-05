package com.superman.beijingweather.ui.setting;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.superman.beijingweather.App;
import com.superman.beijingweather.R;
import com.superman.beijingweather.ui.weather.SimpleSubscriber;
import com.superman.beijingweather.utils.FileSizeUtil;
import com.superman.beijingweather.utils.FileUtil;
import com.superman.beijingweather.utils.SettingsUtil;
import com.superman.beijingweather.utils.TTSManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;



public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener {

    private ListPreference weatherShareType;
    private ListPreference busRefreshFreq;
    private ListPreference ttsVoiceType;
    private Preference cleanCache;
    private Preference theme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        weatherShareType = (ListPreference) findPreference(SettingsUtil.WEATHER_SHARE_TYPE);
        busRefreshFreq = (ListPreference) findPreference(SettingsUtil.BUS_REFRESH_FREQ);
        ttsVoiceType = (ListPreference) findPreference(SettingsUtil.TTS_VOICE_TYPE);
        cleanCache = findPreference(SettingsUtil.CLEAR_CACHE);
        theme = findPreference(SettingsUtil.THEME);

        weatherShareType.setSummary(weatherShareType.getValue());
        ttsVoiceType.setSummary(ttsVoiceType.getEntry());
        busRefreshFreq.setSummary(String.format("%s 秒，长按『刷新』按钮即可开启自动模式。", busRefreshFreq.getValue()));
        String[] colorNames = getActivity().getResources().getStringArray(R.array.color_name);
        int currentThemeIndex = SettingsUtil.getTheme();
        if (currentThemeIndex >= colorNames.length) {
            theme.setSummary("自定义色");
        } else {
            theme.setSummary(colorNames[currentThemeIndex]);
        }

        weatherShareType.setOnPreferenceChangeListener(this);
        busRefreshFreq.setOnPreferenceChangeListener(this);
        ttsVoiceType.setOnPreferenceChangeListener(this);
        cleanCache.setOnPreferenceClickListener(this);
        theme.setOnPreferenceClickListener(this);

        String[] cachePaths = new String[]{FileUtil.getInternalCacheDir(App.getContext()), FileUtil.getExternalCacheDir(App.getContext())};
        Observable
                .just(cachePaths)
                .map(new Func1<String[], String>() {
                    @Override
                    public String call(String[] strings) {
                        return FileSizeUtil.getAutoFileOrFilesSize(strings);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        cleanCache.setSummary(s);
                    }
                });

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference == weatherShareType) {
            weatherShareType.setSummary((String) o);
            SettingsUtil.setWeatherShareType((String) o);
        } else if (preference == busRefreshFreq) {
            busRefreshFreq.setSummary(String.format("%s 秒，长按『刷新』按钮即可开启自动模式。", (String) o));
            SettingsUtil.setBusRefreshFreq(Integer.parseInt((String) o));
        } else if (preference == ttsVoiceType) {
            ttsVoiceType.setSummary(ttsVoiceType.getEntries()[ttsVoiceType.findIndexOfValue((String) o)]);
            SettingsUtil.setTtsVoiceType((String) o);
            TTSManager.destroy();
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == cleanCache) {
            Observable
                    .just(FileUtil.delete(FileUtil.getInternalCacheDir(App.getContext())))
                    .map(new Func1<Boolean, Boolean>() {
                        @Override
                        public Boolean call(Boolean result) {
                            return result && FileUtil.delete(FileUtil.getExternalCacheDir(App.getContext()));
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SimpleSubscriber<Boolean>() {
                        @Override
                        public void onNext(Boolean aBoolean) {
                            cleanCache.setSummary(FileSizeUtil.getAutoFileOrFilesSize(FileUtil.getInternalCacheDir(App.getContext()), FileUtil.getExternalCacheDir(App.getContext())));
                            Snackbar.make(getView(), "缓存已清除 (*^__^*)", Snackbar.LENGTH_SHORT).show();
                        }
                    });
        } else if (preference == theme) {
            new ColorChooserDialog.Builder((SettingActivity) getActivity(), R.string.theme)
                    .customColors(R.array.colors, null)
                    .doneButton(R.string.done)
                    .cancelButton(R.string.cancel)
                    .allowUserColorInput(false)
                    .allowUserColorInputAlpha(false)
                    .show();
        }
        return true;
    }
}
