package com.example.administrator.funread.adapter.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：created by weidiezeng on 2019/8/8 08:48
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class BasePagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList;
    private List<String>titleList;
    public BasePagerAdapter(FragmentManager fm, List<Fragment>fragmentList, String[] title){
        super(fm);
        this.mFragmentList=fragmentList;
        this.titleList=new ArrayList<>(Arrays.asList(title));
    }
    public BasePagerAdapter(FragmentManager fm, List<Fragment>fragmentList, List<String>titleList){
        super(fm);
        this.mFragmentList=fragmentList;
        this.titleList=titleList;
    }
    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }
    public void recreateItems(List<Fragment>fragmentList,List<String>titleList){
        this.mFragmentList=fragmentList;
        this.titleList=titleList;
        notifyDataSetChanged();
    }
}
