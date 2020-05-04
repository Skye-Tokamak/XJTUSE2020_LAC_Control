package com.example.administrator.funread.interfaces;

import android.view.View;
/**
 * 作者：created by weidiezeng on 2019/8/7 11:30
 * 邮箱：1067875902@qq.com
 * 描述：
 */


public interface IOnItemLongClickListener {

    /**
     * RecyclerView Item长按事件
     */
    void onLongClick(View view, int position);
}
