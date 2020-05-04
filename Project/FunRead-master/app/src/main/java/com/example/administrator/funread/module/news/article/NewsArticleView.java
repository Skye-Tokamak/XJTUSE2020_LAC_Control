package com.example.administrator.funread.module.news.article;

import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.funread.Register;
import com.example.administrator.funread.bean.LoadingBean;
import com.example.administrator.funread.module.base.BaseListFragMent;
import com.example.administrator.funread.util.DiffCallback;
import com.example.administrator.funread.util.OnLoadMoreListener;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 作者：created by weidiezeng on 2019/8/14 16:52
 * 邮箱：1067875902@qq.com
 * 描述：新闻显示类
 */
public class NewsArticleView extends BaseListFragMent<INewsArticle.Presenter> implements INewsArticle.View {
   private static final String TAG="NewsArticleView";
   private String categotyId;
   public static NewsArticleView newInstance(String categotyId){
       Bundle bundle=new Bundle();
       bundle.putString(TAG,categotyId);
       NewsArticleView view=new NewsArticleView();
       view.setArguments(bundle);
       return view;
   }

    @Override
    protected void initData() throws NullPointerException {

       categotyId=getArguments().getString(TAG);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAdapter=new MultiTypeAdapter(oldItems);
        Register.registerNewsArticleItem(mAdapter);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(canLoadMore){
                    canLoadMore=true;
                    presenter.doLoadMoreData();
                }
            }
        });
    }

    @Override
    public void fetechData() {
        super.fetechData();
        onLoadData();
    }

    @Override
    public void onLoadData() {

       onShowLoading();
       presenter.doLoadData(categotyId);
    }


    @Override
    public void onSetAdapter(final List<?> list) {
        Items newItems=new Items(list);
        newItems.add(new LoadingBean());
        DiffCallback.create(oldItems,newItems,mAdapter);
        oldItems.clear();
        oldItems.addAll(newItems);
        canLoadMore=true;

        //support libraries v26 增加了 RV 惯性滑动，当 root layout 使用了 AppBarLayout Behavior 就会自动生效
        // 因此需要手动停止滑动
        recyclerView.stopScroll();


    }

    @Override
    public void setPresenter(INewsArticle.Presenter presenter) {

       if(null==presenter){
           this.presenter=new NewsArticlePresenter(this);
       }
    }
}
