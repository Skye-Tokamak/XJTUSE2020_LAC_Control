package com.example.administrator.funread.module.base;

/**
 * 作者：created by weidiezeng on 2019/8/7 11:30
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public interface IBasePresenter {
    /**
     * 刷新数据
     */
    void doRefresh();

    /**
     * 显示网络错误
     */
    void doShowNetError();
}
