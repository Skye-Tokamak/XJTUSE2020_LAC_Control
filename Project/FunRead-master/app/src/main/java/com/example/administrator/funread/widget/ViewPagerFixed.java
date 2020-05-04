package com.example.administrator.funread.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者：created by weidiezeng on 2019/8/19 08:22
 * 邮箱：1067875902@qq.com
 * 描述：viewPager处理
 */
public class ViewPagerFixed extends ViewPager {
    public ViewPagerFixed(@NonNull Context context) {
        super(context);
    }
    public ViewPagerFixed(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        }catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try{
            return super.onInterceptTouchEvent(ev);
        }catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }
        return false;
    }
}
