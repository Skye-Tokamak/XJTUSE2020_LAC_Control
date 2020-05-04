package com.example.administrator.funread.module.news.article;

import android.util.Log;

import com.example.administrator.funread.ErrorAction;
import com.example.administrator.funread.api.INewsApi;
import com.example.administrator.funread.bean.news.NewsArticleBean;
import com.example.administrator.funread.util.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：created by weidiezeng on 2019/8/14 16:04
 * 邮箱：1067875902@qq.com
 * 描述：新闻数据操作类
 */
public class NewsArticlePresenter implements INewsArticle.Presenter{
    private static final String TAG="NewsArticlePresenter";
    private INewsArticle.View mView;
    private List<NewsArticleBean.ResultBean.Databean> mDatabeanList=new ArrayList<>();
    private String category;
   // private Random mRandom=new Random();

    /*//默认构造函数，测试api用

    public NewsArticlePresenter(){
    }
*/

    public NewsArticlePresenter(INewsArticle.View view){
        this.mView=view;
    }

    @Override
    public void doLoadData(String... category) {

        try{
            if(this.category==null){
                this.category=category[0];
            }
        }catch (Exception e){
            ErrorAction.print(e);
        }
        if(mDatabeanList.size()>150){
            mDatabeanList.clear();
        }

        getRandom()
                .subscribeOn(Schedulers.io())
                .switchMap((Function<NewsArticleBean,Observable<NewsArticleBean.ResultBean.Databean>>)newsArticleBean->{
                    List<NewsArticleBean.ResultBean.Databean>dataList=new ArrayList<>();
                    for(NewsArticleBean.ResultBean.Databean databean:newsArticleBean.getResult().getData()){
                        dataList.add(databean);
                    }
                    return Observable.fromIterable(dataList);
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                //.as(mView.bindAutoDispose())
                .subscribe(list->{
                    if(null!=list&&list.size()>0){
                        /*//测试api是否请求成功
                        测试成功
                        for(int i=0;i<list.size();i++){
                            Log.d(TAG,list.get(i).getTitle());
                        }*/
                        doSetAdapter(list);
                    }else {
                        doShowNoMore();
                    }
                },throwable -> {
                    doShowNetError();
                    ErrorAction.print(throwable);
                });
    }

    @Override
    public void doLoadMoreData() {

        doLoadData();
    }

    @Override
    public void doSetAdapter(List<NewsArticleBean.ResultBean.Databean> list) {

        mDatabeanList.addAll(list);
        mView.onSetAdapter(mDatabeanList);
        mView.onHideLoading();
    }

    @Override
    public void doShowNoMore() {

        mView.onHideLoading();
        mView.onShowNoMore();
    }

    @Override
    public void doRefresh() {

        if(mDatabeanList.size()!=0){
            mDatabeanList.clear();
        }
        mView.onShowLoading();
        doLoadData();
    }

    @Override
    public void doShowNetError() {

        mView.onHideLoading();
        mView.onShowNetError();
    }
    public Observable<NewsArticleBean>getRandom(){
        Observable<NewsArticleBean>ob1=RetrofitFactory.getRetrofit().create(INewsApi.class)
                .getNewArticle(this.category);
        return ob1;
    }
}
