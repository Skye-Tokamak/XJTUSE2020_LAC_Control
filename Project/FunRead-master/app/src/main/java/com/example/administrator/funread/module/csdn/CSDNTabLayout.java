package com.example.administrator.funread.module.csdn;

import android.support.v4.app.Fragment;

/**
 * 作者：created by weidiezeng on 2019/8/6 16:02
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class CSDNTabLayout extends Fragment {
    public static final String TAG = "CSDNTabLayout";
    private static CSDNTabLayout instance = null;
    public static CSDNTabLayout getInstance() {
        if (instance == null) {
            instance = new CSDNTabLayout();
        }
        return instance;
    }
}
