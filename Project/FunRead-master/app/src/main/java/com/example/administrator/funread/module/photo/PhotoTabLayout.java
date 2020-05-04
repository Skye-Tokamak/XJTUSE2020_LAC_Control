package com.example.administrator.funread.module.photo;

import android.support.v4.app.Fragment;

/**
 * 作者：created by weidiezeng on 2019/8/6 16:02
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class PhotoTabLayout extends Fragment {
    public static final String TAG = "PhotoTabLayout";
    private static PhotoTabLayout instance = null;
    public static PhotoTabLayout getInstance() {
        if (instance == null) {
            instance = new PhotoTabLayout();
        }
        return instance;
    }
}
