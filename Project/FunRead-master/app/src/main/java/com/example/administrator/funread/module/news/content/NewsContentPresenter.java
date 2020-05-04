package com.example.administrator.funread.module.news.content;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.example.administrator.funread.ImageBrowserActivity;
import com.example.administrator.funread.InitApp;
import com.example.administrator.funread.bean.news.NewsContentBean;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：created by weidiezeng on 2019/8/17 10:36
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class NewsContentPresenter implements INewsContent.Presenter {
    private static final String TAG="NewsContentPresenter";
    private INewsContent.View view;
    private NewsContentBean bean;

    NewsContentPresenter(INewsContent.View view){
        this.view=view;
    }
    @Override
    public void doLoadData(NewsContentBean bean) {

        this.bean=bean;
        Observable
                .create((ObservableOnSubscribe<String>)e->{
                    String url=bean.getUrl();
                    e.onNext(url);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(view.bindAutoDispose())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                        Log.d(TAG,s);
                        view.onSetWebView(s,true);
                    }

                    @Override
                    public void onError(Throwable e) {

                        view.onSetWebView(null,false);
                    }

                    @Override
                    public void onComplete() {

                        doShowNetError();
                    }
                });
    }

    @Override
    public void doRefresh() {

    }

    @Override
    public void doShowNetError() {

        view.onHideLoading();
        view.onShowNetError();
    }

    /**
     * 监听图片点击事件，并打开图片浏览
     * @param url
     */
    @JavascriptInterface
    public void openImage(String url){
        if(!TextUtils.isEmpty(url)){
            ArrayList<String>list=getAllImageUrl();
            if(list.size()>0){
                ImageBrowserActivity.start(InitApp.AppContext,url,list);
                Log.d(TAG,"openImage: "+list.toString());
            }
        }
    }

    /**获取图片列表
     * @return
     */
    private ArrayList<String> getAllImageUrl() {
        ArrayList<String>arrayList=new ArrayList<>();
        String img=bean.getImg();
        String img02=bean.getImg02();
        String img03=bean.getImg03();
        Log.d(TAG,img);
        Log.d(TAG,img02);
        Log.d(TAG,img03);
        arrayList.add(img);
        arrayList.add(img02);
        arrayList.add(img03);
        return arrayList;
    }
}
