package com.example.administrator.funread.module.news.article;

import com.example.administrator.funread.bean.news.NewsArticleBean;
import com.example.administrator.funread.module.base.IBaseListView;
import com.example.administrator.funread.module.base.IBasePresenter;

import java.util.List;

/**
 * 作者：created by weidiezeng on 2019/8/11 16:48
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public interface INewsArticle {
    interface View extends IBaseListView<Presenter>{
        /**
         * 请求数据
         */
        void onLoadData();

        /**
         * 刷新
         */
        void onRefresh();
    }
    interface Presenter extends IBasePresenter{

        /**
         * 请求数据
         */
        void doLoadData(String... category);

        /**
         * 再起请求数据
         */
        void doLoadMoreData();

        /**
         * 设置适配器
         */
        void doSetAdapter(List<NewsArticleBean.ResultBean.Databean> dataBean);

        /**
         * 加载完毕
         */
        void doShowNoMore();
    }
}
