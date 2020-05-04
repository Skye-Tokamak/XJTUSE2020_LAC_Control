package com.example.administrator.funread.api;

import com.example.administrator.funread.bean.news.NewsArticleBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：created by weidiezeng on 2019/8/14 08:39
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public interface INewsApi {
    /**
     *
     */
    String HOST = "http://v.juhe.cn/toutiao/";

    @GET("http://v.juhe.cn/toutiao/index?key=822951fb476d79a04f99ed4e0c3cf643")
    Observable<NewsArticleBean> getNewArticle(
            @Query("type") String type);

}
