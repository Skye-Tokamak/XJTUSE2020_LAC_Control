package com.example.administrator.funread.binder.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.PopupMenu;
import android.widget.TextView;

import com.example.administrator.funread.InitApp;
import com.example.administrator.funread.IntentAction;
import com.example.administrator.funread.R;
import com.example.administrator.funread.bean.news.NewsArticleBean;
import com.example.administrator.funread.bean.news.NewsContentBean;
import com.example.administrator.funread.module.news.content.NewsContentActivity;
import com.example.administrator.funread.util.ImageLoader;
import com.example.administrator.funread.util.SettingUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import me.drakeet.multitype.ItemViewBinder;

/**
 * 作者：created by weidiezeng on 2019/8/16 09:58
 * 邮箱：1067875902@qq.com
 * 描述：新闻界面绑定bean
 */
public class NewsArticleViewBinder extends ItemViewBinder<NewsArticleBean.ResultBean.Databean,NewsArticleViewBinder.ViewHolder> {


    @NonNull
    @Override
    protected NewsArticleViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view=inflater.inflate(R.layout.item_news_article,parent,false);

        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull NewsArticleViewBinder.ViewHolder holder, @NonNull NewsArticleBean.ResultBean.Databean item) {
        final Context context = holder.itemView.getContext();

        String tv_authorname=item.getAuthor_name();
        String tv_date=item.getDate();
        String title=item.getTitle();
        String url_img=item.getThumbnail_pic_s();
        String url_img2=item.getThumbnail_pic_s02();
        String url_img3=item.getThumbnail_pic_s03();
        String url_content=item.getUrl();

        /*//设置cardview背景颜色
        if(SettingUtil.getInstance().getIsNightMode()){
            holder.mCardView.setCardBackgroundColor(InitApp.AppContext.getResources().getColor(R.color.Black));
        }*/

        holder.tv_extra.setText(tv_authorname+"-"+tv_date);
        holder.tv_title.setText(title);
        holder.tv_title.setTextSize(SettingUtil.getInstance().getTextSize());

        if(!TextUtils.isEmpty(url_img)){
            ImageLoader.loadCenterCrop(context,url_img,holder.iv_image,R.color.viewBackground);
        }
        if(!TextUtils.isEmpty(url_img2)){
            ImageLoader.loadCenterCrop(context,url_img2,holder.iv_image2,R.color.viewBackground);
        }
        if(!TextUtils.isEmpty(url_img3)){
            ImageLoader.loadCenterCrop(context,url_img3,holder.iv_image3,R.color.viewBackground);
        }
        holder.iv_dots.setOnClickListener(view->{
            PopupMenu popupMenu=new PopupMenu(context,holder.iv_dots,
                    Gravity.END,0,R.style.MyPopupMenu);
            popupMenu.inflate(R.menu.menu_share);
            popupMenu.setOnMenuItemClickListener(menu->{
                int itemId=menu.getItemId();
                if(itemId==R.id.action_share){
                    IntentAction.send(context,item.getTitle()+"\n"+item.getUrl());
                }
                return false;
            });
            popupMenu.show();
        });

        //加载新闻内容
        NewsContentBean bean=new NewsContentBean();
        bean.setUrl(item.getUrl());
        bean.setTitle(item.getTitle());
        bean.setAuthorName(item.getAuthor_name());
        bean.setImg(item.getThumbnail_pic_s());
        bean.setImg02(item.getThumbnail_pic_s02());
        bean.setImg03(item.getThumbnail_pic_s03());
        //使用rxbind处理点击事件，打开NewContenActivity
        RxView.clicks(holder.itemView)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> NewsContentActivity.lauch(bean));
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        private CardView mCardView;
        private TextView tv_extra;
        private ImageView iv_dots;
        private TextView tv_title;
        private ImageView iv_image;
        private ImageView iv_image2;
        private ImageView iv_image3;
        ViewHolder(View itemView){
            super(itemView);
            this.tv_extra=itemView.findViewById(R.id.tv_extra);
            this.iv_dots=itemView.findViewById(R.id.iv_dots);
            this.tv_title=itemView.findViewById(R.id.tv_title);
            this.iv_image=itemView.findViewById(R.id.iv_image);
            this.iv_image2=itemView.findViewById(R.id.iv_image2);
            this.iv_image3=itemView.findViewById(R.id.iv_image3);

            this.mCardView=itemView.findViewById(R.id.card_view);
        }
    }
}
