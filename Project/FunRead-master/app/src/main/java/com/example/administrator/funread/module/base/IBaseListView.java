package com.example.administrator.funread.module.base;

import java.util.List;

/**
 * 作者：created by weidiezeng on 2019/8/7 14:40
 * 邮箱：1067875902@qq.com
 * 描述：
 * @param <T>
 */
public interface IBaseListView<T>extends IBaseView<T>{
    /**
     * 显示加载动画
     */
    void onShowLoading();

    /**
     * 隐藏加载
     */
    void onHideLoading();

    /**
     * 显示网络错误
     */
    void onShowNetError();

    /**
     * 设置presenter
     * @param prenter
     */
    void setPresenter(T prenter);

    /**
     *设置适配器
     * @param list
     */
    void onSetAdapter(List<?>list);

    /**
     * 加载完毕
     */
    void onShowNoMore();
}
