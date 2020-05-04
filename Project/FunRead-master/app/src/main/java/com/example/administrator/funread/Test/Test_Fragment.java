package com.example.administrator.funread.Test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.funread.R;

/**
 * 作者：created by weidiezeng on 2019/8/13 08:07
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class Test_Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.test_fragment,container,false);
        return view;
    }
}
