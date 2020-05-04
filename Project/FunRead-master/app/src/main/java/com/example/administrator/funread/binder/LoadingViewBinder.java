package com.example.administrator.funread.binder;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.administrator.funread.R;
import com.example.administrator.funread.bean.LoadingBean;
import com.example.administrator.funread.util.SettingUtil;

import me.drakeet.multitype.ItemViewBinder;
/**
 * 作者：created by weidiezeng on 2019/8/16 09:31
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class LoadingViewBinder extends ItemViewBinder<LoadingBean, LoadingViewBinder.ViewHolder> {


    @NonNull
    @Override
    protected LoadingViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view=inflater.inflate(R.layout.item_loading,parent,false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull LoadingViewBinder.ViewHolder holder, @NonNull LoadingBean item) {

        int color=SettingUtil.getInstance().getColor();
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            Drawable wrapDrawable=DrawableCompat.wrap(holder.mProgressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable,color);
            holder.mProgressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        }else {
            holder.mProgressBar.getIndeterminateDrawable().setColorFilter(color,PorterDuff.Mode.SRC_IN);
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private ProgressBar mProgressBar;
        ViewHolder(View itemView){
            super(itemView);
            this.mProgressBar=itemView.findViewById(R.id.progress_footer);
        }
    }
}
