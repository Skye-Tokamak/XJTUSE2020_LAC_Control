package com.example.administrator.funread.setting;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;

import com.example.administrator.funread.R;
import com.example.administrator.funread.util.SettingUtil;

/**
 * 作者：created by weidiezeng on 2019/8/21 15:37
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class AutoNightModeFragment extends PreferenceFragment {

    SettingUtil mSettingUtil=SettingUtil.getInstance();
    private String nightStartHour;
    private String nightStartMinute;
    private String dayStartHour;
    private String dayStartMinute;
    Preference autoNight;
    Preference autoDay;

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_autonight);
        setHasOptionsMenu(true);

        autoNight=findPreference("auto_night");
        autoDay=findPreference("auto_day");

        setText();
        //autoNight绑定，并打开timePickerDialog
        autoNight.setOnPreferenceClickListener(preference -> {
            TimePickerDialog timePickerDialog=new TimePickerDialog(getActivity(),
                    (timePicker,hour,minute)->{
                        mSettingUtil.setNightStartHour(hour > 9 ? "" + hour : "0" + hour);
                        mSettingUtil.setNightStartMinute(minute > 9 ? "" + minute : "0" + minute);
                        setText();
            },Integer.parseInt(nightStartHour),Integer.parseInt(nightStartMinute),true);
            timePickerDialog.show();
            timePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(R.string.done);
            timePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText(R.string.cancel);
            return false;
        });
        //autoNight绑定，并打开timePickerDialog
        autoDay.setOnPreferenceClickListener(preference -> {
            TimePickerDialog dialog = new TimePickerDialog(getActivity(),
                    (timePicker, hour, minute) -> {
                        mSettingUtil.setDayStartHour(hour > 9 ? "" + hour : "0" + hour);
                        mSettingUtil.setDayStartMinute(minute > 9 ? "" + minute : "0" + minute);
                        setText();
                    }, Integer.parseInt(dayStartHour), Integer.parseInt(dayStartMinute), true);
            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(R.string.done);
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText(R.string.cancel);
            return false;
        });
    }

    /**
     * 设置初始值
     */
    private void setText() {
        nightStartHour = mSettingUtil.getNightStartHour();
        nightStartMinute = mSettingUtil.getNightStartMinute();
        dayStartHour = mSettingUtil.getDayStartHour();
        dayStartMinute =mSettingUtil.getDayStartMinute();

        autoNight.setSummary(nightStartHour + ":" + nightStartMinute);
        autoDay.setSummary(dayStartHour + ":" + dayStartMinute);
    }
}
