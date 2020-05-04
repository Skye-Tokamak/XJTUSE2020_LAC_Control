package com.example.administrator.funread.widget;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

import com.afollestad.materialdialogs.color.CircleView;
import com.example.administrator.funread.R;
import com.example.administrator.funread.util.SettingUtil;

/**
 * 作者：created by weidiezeng on 2019/8/21 11:13
 * 邮箱：1067875902@qq.com
 * 描述：更改主题颜色控件
 */
public class IconPreference extends Preference {
    private CircleView mCircleView;
    public IconPreference(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        setWidgetLayoutResource(R.layout.item_icon_preference_preview);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        int color=SettingUtil.getInstance().getColor();
        mCircleView=view.findViewById(R.id.iv_preview);
        mCircleView.setBackgroundColor(color);
    }
    public void setView(){
        int color=SettingUtil.getInstance().getColor();
        mCircleView.setBackgroundColor(color);
    }
}
