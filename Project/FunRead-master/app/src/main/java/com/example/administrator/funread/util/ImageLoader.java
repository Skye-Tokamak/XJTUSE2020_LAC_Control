package com.example.administrator.funread.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * 作者：created by weidiezeng on 2019/8/16 11:02
 * 邮箱：1067875902@qq.com
 * 描述：
 */
@GlideModule
public class ImageLoader extends AppGlideModule {

    public static void loadCenterCrop(Context context, String url, ImageView view,int defaultResId){
        if(SettingUtil.getInstance().getIsNoPhotoMode()&&NetWorkUtil.isMobileConnected(context)){
            view.setImageResource(defaultResId);
        }else {

            GlideApp.with(context)
                    .load(url)
                    .transition(withCrossFade())
                    .apply(new RequestOptions().centerCrop())
                    .into(view);
        }

    }

    /**
     * 处理加载异常图片
     * @param context
     * @param url
     * @param view
     * @param defaultResId
     * @param errorResId
     */
    public static void loadCenterCrop(Context context,String url,ImageView view,int defaultResId,int errorResId){
        GlideApp.with(context)
                .load(url)
                .transition(withCrossFade())
                .apply(new RequestOptions().centerCrop().error(errorResId))
                .into(view);

    }

    public static void loadCenterCrop(Context context, String url, ImageView view, RequestListener listener){
        GlideApp.with(context)
                .load(url)
                .transition(withCrossFade())
                .apply(new RequestOptions().centerCrop())
                .listener(listener)
                .into(view);
    }
    public static void loadNormal(Context context,String url,ImageView view){
        GlideApp.with(context).load(url).into(view);
    }
}
