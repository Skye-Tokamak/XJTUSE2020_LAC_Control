package com.example.administrator.funread.module.news.content;


import com.example.administrator.funread.bean.news.NewsContentBean;
import com.example.administrator.funread.module.base.IBasePresenter;
import com.example.administrator.funread.module.base.IBaseView;

/**
 * 作者：created by weidiezeng on 2019/8/17 09:50
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public interface INewsContent {

    interface View extends IBaseView<Presenter>{
        void onSetWebView(String url,boolean flag);
    }
    interface Presenter extends IBasePresenter{
         void doLoadData(NewsContentBean bean);
    }

}
