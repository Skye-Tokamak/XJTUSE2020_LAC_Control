package com.example.administrator.funread;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.example.administrator.funread.util.SettingUtil;

import java.util.Calendar;

/**
 * 作者：created by weidiezeng on 2019/8/6 16:45
 * 邮箱：1067875902@qq.com
 * 描述：自定义application类管理全局
 */
public class InitApp extends MultiDexApplication {
    public static Context AppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext=getApplicationContext();
        initTheme();
        if(BuildConfig.DEBUG){

        }
    }
    private void initTheme(){
        SettingUtil settings=SettingUtil.getInstance();
        //获取是否开启“自动夜间切换模式”
        if(settings.getIsAutoNightMode()){
            int nightStartHour=Integer.parseInt(settings.getNightStartHour());
            int nightStartMinute=Integer.parseInt(settings.getNightStartMinute());
            int dayStartHout=Integer.parseInt(settings.getDayStartHour());
            int dayStartMinute=Integer.parseInt(settings.getDayStartMinute());

            Calendar calendar=Calendar.getInstance();
            int currentHour=calendar.get(Calendar.HOUR_OF_DAY);
            int curretnMinute=calendar.get(Calendar.MINUTE);
            int nightValue=nightStartHour*60+nightStartMinute;
            int dayValue=dayStartHout*60+dayStartMinute;
            int currentValue=currentHour*60+curretnMinute;

            if(currentValue>=nightValue||currentValue<=dayValue){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                settings.setIsNightMode(true);

            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                settings.setIsNightMode(false);
            }
        }else {
            //获取当前主题，是否开启夜间模式
            if(settings.getIsNightMode()){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }
}
