package com.example.administrator.funread.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * 作者：created by weidiezeng on 2019/8/14 17:14
 * 邮箱：1067875902@qq.com
 * 描述：滚动加载跟多
 */
public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager mLayoutManager;
    private int itemCount,lastPosition,lastItemCount;

    public abstract void onLoadMore();

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if(recyclerView.getLayoutManager()instanceof LinearLayoutManager){
            mLayoutManager=(LinearLayoutManager)recyclerView.getLayoutManager();
            itemCount=mLayoutManager.getItemCount();
            lastPosition=mLayoutManager.findLastCompletelyVisibleItemPosition();

        }else {
            Log.e("OnLoadMoreListener","The OnLaodMoreListener only support LinearLayout");
            return;
        }
        if(lastItemCount!=itemCount&&lastPosition==itemCount-1){
            lastItemCount=itemCount;
            this.onLoadMore();
        }
    }
}
