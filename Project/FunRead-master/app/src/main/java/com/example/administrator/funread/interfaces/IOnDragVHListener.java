package com.example.administrator.funread.interfaces;

/**
 * 作者：created by weidiezeng on 2019/8/7 11:30
 * 邮箱：1067875902@qq.com
 * 描述：
 */

public interface IOnDragVHListener {

    /**
     * Item被选中时触发
     */
    void onItemSelected();


    /**
     * Item在拖拽结束/滑动结束后触发
     */
    void onItemFinish();
}
