package com.example.administrator.funread.module.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.funread.R;
import com.example.administrator.funread.module.news.NewsTabLayout;
import com.example.administrator.funread.util.Rxbus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

/**
 * 作者：created by weidiezeng on 2019/8/6 16:03
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class WeatherTabLayout extends Fragment {
    public static final String TAG = "WeatherTabLayout";
    private Observable<Boolean> mObservable;
    Unbinder unbinder;
    private static WeatherTabLayout instance = null;

    public WeatherTabLayout() {
    }

    public static WeatherTabLayout getInstance() {
        if (instance == null) {
            instance = new WeatherTabLayout();
        }
        return instance;
    }
//
//    LinearLayout soildetext; //点击的LinearLayout
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_weather_tab, container, false);
//
//        unbinder = ButterKnife.bind(this, view);
//        return view;
//        //绑定该LinearLayout的ID
//        soildetext=(LinearLayout)view.findViewById(R.id.soilclick);
//        //设置监听
//        soildetext.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                //SoilsenerActivity.class为想要跳转的Activity
//                intent.setClass(getActivity(), MainActivity.class);
//                startActivity(intent);
//            }
//        }
//
//
//    }



    @Override
    public void onDestroy() {
        Rxbus.getInstance().unregister(NewsTabLayout.TAG, mObservable);
        if (instance != null) {
            instance = null;
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
