package com.example.administrator.funread.module.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 作者：created by weidiezeng on 2019/8/7 15:08
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public abstract class LazyLoadFragment<T extends IBasePresenter> extends BaseFragment <T>{
    //是否初始化过布局
    protected  boolean isViewInitiated;

    //当前界面是否可见
    protected boolean isVisibleToUser;

    //是否加载过数据
    protected boolean isDataInitiated;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.isViewInitiated=true;
        prepareFetechData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser=isVisibleToUser;
        if(isVisibleToUser){
            prepareFetechData();
        }
    }

    public void prepareFetechData(){
        prepareFetechData(false);
    }

    /**
     * 懒加载
     */
    public abstract void fetechData();

    /**
     *
     * @param forceUpdate 强制更新
     */
    public void prepareFetechData(boolean forceUpdate){

        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetechData();
            isDataInitiated=true;

        }
    }
}
