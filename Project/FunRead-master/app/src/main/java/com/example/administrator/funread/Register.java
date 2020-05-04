package com.example.administrator.funread;

import android.support.annotation.NonNull;

import com.example.administrator.funread.bean.LoadingBean;
import com.example.administrator.funread.bean.LoadingEndBean;
import com.example.administrator.funread.bean.news.NewsArticleBean;
import com.example.administrator.funread.binder.LoadingEndViewBinder;
import com.example.administrator.funread.binder.LoadingViewBinder;
import com.example.administrator.funread.binder.news.NewsArticleViewBinder;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 作者：created by weidiezeng on 2019/8/16 09:26
 * 邮箱：1067875902@qq.com
 * 描述：MultiTypeAdapter 注册类
 */
public class Register {
    public static void registerNewsArticleItem(@NonNull MultiTypeAdapter adapter){
        adapter.register(NewsArticleBean.ResultBean.Databean.class,new NewsArticleViewBinder());
        adapter.register(LoadingBean.class,new LoadingViewBinder());
        adapter.register(LoadingEndBean.class,new LoadingEndViewBinder());
    }
}
