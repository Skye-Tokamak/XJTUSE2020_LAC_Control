package com.example.administrator.funread.setting;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import com.afollestad.materialdialogs.color.CircleView;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.example.administrator.funread.Constant;
import com.example.administrator.funread.R;
import com.example.administrator.funread.util.CacheDataManager;
import com.example.administrator.funread.util.SettingUtil;
import com.example.administrator.funread.widget.IconPreference;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.BSD3ClauseLicense;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

/**
 * 作者：created by weidiezeng on 2019/8/21 10:30
 * 邮箱：1067875902@qq.com
 * 描述：通用配置类
 */
public class GeneralPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private IconPreference colorPreview;
    private SettingActivity context;

    public static GeneralPreferenceFragment newInstance() {
        return new GeneralPreferenceFragment();
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //从资源文件加载
        addPreferencesFromResource(R.xml.pref_general);
        context=(SettingActivity)getActivity();
        //报告fragment希望通过接收对onCreateOptionsMenu(menu, MenuInflater)和相关方法的调用来填充选项菜单
        setHasOptionsMenu(true);
        setText();

        //打开自动切换夜间模式
        findPreference("auto_nightMode").setOnPreferenceClickListener(preference -> {
            context.startWithFragment(AutoNightModeFragment.class.getName(), null, null, 0, null);
            return true;
        });

        //打开设置字体大小
        findPreference("text_size").setOnPreferenceClickListener(preference -> {
            context.startWithFragment(TextSizeFragment.class.getName(), null, null, 0, null);
            return true;
        });
        //打开更换图标
        findPreference("custom_icon").setOnPreferenceChangeListener((preference, newValue) -> {

            int selectValue = Integer.parseInt((String) newValue);
            int drawable = Constant.ICONS_DRAWABLES[selectValue];

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //更改任务栏图标
                ActivityManager.TaskDescription tDesc = new ActivityManager.TaskDescription(
                        getString(R.string.app_name),
                        BitmapFactory.decodeResource(getResources(), drawable),
                        SettingUtil.getInstance().getColor());
                context.setTaskDescription(tDesc);
            }

            return true;
        });
        //更改主题颜色
        findPreference("color").setOnPreferenceClickListener(preference -> {
            new ColorChooserDialog.Builder(context, R.string.choose_theme_color)
                    .backButton(R.string.back)
                    .cancelButton(R.string.cancel)
                    .doneButton(R.string.done)
                    .customButton(R.string.custom)
                    .presetsButton(R.string.back)
                    .allowUserColorInputAlpha(false)
                    .show(context);
            return false;
        });
        colorPreview = (IconPreference) findPreference("color");

        //导航栏着色
        findPreference("nav_bar").setOnPreferenceClickListener(preference -> {
            int color = SettingUtil.getInstance().getColor();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (SettingUtil.getInstance().getNavBar()) {
                    context.getWindow().setNavigationBarColor(CircleView.shiftColorDown(CircleView.shiftColorDown(color)));
                } else {
                    context.getWindow().setNavigationBarColor(Color.BLACK);
                }
            }
            return false;
        });

        //清理缓存
        findPreference("clearCache").setOnPreferenceClickListener(preference -> {
            CacheDataManager.clearAllCache(context);
            Snackbar.make(getView(), R.string.clear_cache_successfully, Snackbar.LENGTH_SHORT).show();
            setText();
            return false;
        });
        //获取当前版本
        try {
            String version = "当前版本 " + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            findPreference("version").setSummary(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //获取更新日志
        findPreference("changelog").setOnPreferenceClickListener(preference -> {
            //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.changelog_url))));
            return false;
        });

        //获取开源许可
        findPreference("licenses").setOnPreferenceClickListener(preference -> {
            createLicenseDialog();
            return false;
        });

        //源代码
        findPreference("sourceCode").setOnPreferenceClickListener(preference -> {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.source_code_url))));
            return false;
        });
        //版权声明
        findPreference("copyRight").setOnPreferenceClickListener(preference -> {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.copyright)
                    .setMessage(R.string.copyright_content)
                    .setCancelable(true)
                    .show();
            return false;
        });

    }

    private void createLicenseDialog() {
        Notices notices=new Notices();

        notices.addNotice(new Notice("RxJava","https://github.com/ReactiveX/RxJava","Copyright (c) 2016-present",new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("PhotoView", "https://github.com/chrisbanes/PhotoView", "Copyright 2017 Chris Banes", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("OkHttp", "https://github.com/square/okhttp", "Copyright 2016 Square, Inc.", new ApacheSoftwareLicense20()));

        notices.addNotice(new Notice("Glide", "https://github.com/bumptech/glide", "Sam Judd - @sjudd on GitHub, @samajudd on Twitter", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Stetho", "https://github.com/facebook/stetho", "Copyright (c) 2015, Facebook, Inc. All rights reserved.", new BSD3ClauseLicense()));
        notices.addNotice(new Notice("PersistentCookieJar", "https://github.com/franmontiel/PersistentCookieJar", "Copyright 2016 Francisco José Montiel Navarro", new ApacheSoftwareLicense20()));

        new LicensesDialog.Builder(context)
                .setNotices(notices)
                //包含自己的license
                .setIncludeOwnLicense(true)
                .build()
                .show();
    }

    private void setText() {
        try {
            findPreference("clearCache").setSummary(CacheDataManager.getTotalCacheSize(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals("color")) {
            colorPreview.setView();
        }
        if (key.equals("slidable")) {
            context.recreate();
        }
    }
}
