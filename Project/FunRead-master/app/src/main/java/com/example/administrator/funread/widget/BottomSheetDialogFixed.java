package com.example.administrator.funread.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

/**
 * 作者：created by weidiezeng on 2019/8/19 10:43
 * 邮箱：1067875902@qq.com
 * 描述：解决使用BottomSheetDialog时状态栏变黑的问题
 */
public class BottomSheetDialogFixed extends BottomSheetDialog {
    public BottomSheetDialogFixed(@NonNull Context context){
        super(context);
    }
    private static int getScreenHeight(Activity activity){
        DisplayMetrics displayMetrics=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
    private static int getStatusBarHeight(Context context){
        int statusBarHeight=0;
        Resources resources=context.getResources();
        int resourceId=resources.getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0){
            statusBarHeight=resources.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int screenHeight=getScreenHeight(getOwnerActivity());
        int statusBarHeight=getStatusBarHeight(getContext());
        int dialogHeight=screenHeight-statusBarHeight;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,dialogHeight==0? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
    }
}
