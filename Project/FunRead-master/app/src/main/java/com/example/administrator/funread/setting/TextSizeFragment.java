package com.example.administrator.funread.setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.funread.R;
import com.example.administrator.funread.module.base.BaseListFragMent;
import com.example.administrator.funread.util.Rxbus;
import com.example.administrator.funread.util.SettingUtil;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import java.text.DecimalFormat;

/**
 * 作者：created by weidiezeng on 2019/8/21 15:38
 * 邮箱：1067875902@qq.com
 * 描述：字体大小
 */
public class TextSizeFragment extends PreferenceFragment {

    private RangeSeekBar seekbar;
    private TextView text;
    private DecimalFormat df = new DecimalFormat("0");
    private int currentSize = -1;
    private SettingUtil settingUtil = SettingUtil.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_textsize, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        seekbar = view.findViewById(R.id.seekbar);
        text = view.findViewById(R.id.text);
        //设置初始值
        text.setTextSize(settingUtil.getTextSize());
        seekbar.setProgress(settingUtil.getTextSize() - 14);
        //设置progress颜色
        seekbar.setProgressColor(0, settingUtil.getColor());
        seekbar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, final float min, float max, boolean isFromUser) {
                if (isFromUser) {
                    //转为十进制
                    int size = Integer.parseInt(df.format(min));
                    if (currentSize != size) {
                        setText(size);
                        currentSize = size;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
    }

    private void setText(int size) {
        // 最小 14sp
        size = 14 + size;
        text.setTextSize(size);
        settingUtil.setTextSize(size);
        Rxbus.getInstance().post(BaseListFragMent.TAG, size);
    }
}
