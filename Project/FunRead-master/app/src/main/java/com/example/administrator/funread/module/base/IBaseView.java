package com.example.administrator.funread.module.base;

import com.uber.autodispose.AutoDisposeConverter;

/**
 * 作者：created by weidiezeng on 2019/8/7 11:27
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public interface IBaseView<T> {
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
     */
    void setPresenter(T presenter);

    <X>AutoDisposeConverter<X> bindAutoDispose();
}
