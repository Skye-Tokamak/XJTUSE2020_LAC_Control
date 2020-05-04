package com.example.administrator.funread.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.funread.R;
import com.example.administrator.funread.bean.LoadingEndBean;

import me.drakeet.multitype.ItemViewBinder;

/**
 * 作者：created by weidiezeng on 2019/8/16 15:04
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class LoadingEndViewBinder extends ItemViewBinder<LoadingEndBean,LoadingEndViewBinder.ViewHolder> {


    @NonNull
    @Override
    protected LoadingEndViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view=inflater.inflate(R.layout.item_loading_end,parent,false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull LoadingEndViewBinder.ViewHolder holder, @NonNull LoadingEndBean item) {

    }
    class ViewHolder extends RecyclerView.ViewHolder{
        ViewHolder(View itemView){
            super(itemView);
        }
    }
}
