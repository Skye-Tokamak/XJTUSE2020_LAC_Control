package com.example.administrator.funread.module.news.channel;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.funread.Constant;
import com.example.administrator.funread.ErrorAction;
import com.example.administrator.funread.R;
import com.example.administrator.funread.adapter.news.NewsChannelAdapter;
import com.example.administrator.funread.bean.news.NewsChannelBean;
import com.example.administrator.funread.database.dao.NewsChannelDao;
import com.example.administrator.funread.module.base.BaseActivity;
import com.example.administrator.funread.module.news.NewsTabLayout;
import com.example.administrator.funread.util.Rxbus;
import com.example.administrator.funread.widget.ItemDragHelperCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewsChannelActivity extends BaseActivity {


    @BindView(R.id.recyler_view)
    RecyclerView mRecylerView;
    @BindView(R.id.linear_layout)
    LinearLayout mLinearLayout;
    private NewsChannelDao mDao=new NewsChannelDao();
    private NewsChannelAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_channel);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        onSaveData();
    }

    private void initView(){
        Toolbar toolbar=findViewById(R.id.toolbar);
        initToolBar(toolbar,true,getString(R.string.title_item_drag));

    }
    private void initData(){
       final List<NewsChannelBean>enanbleItems= mDao.query(Constant.NEWS_CHANNEL_ENABLE);
       final List<NewsChannelBean>disabelItems=mDao.query(Constant.NEWS_CHANNEL_DIABEL);

       //设置布局，一行四个
        GridLayoutManager manager=new GridLayoutManager(this,4);
        mRecylerView.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();//拖拽处理
        final ItemTouchHelper helper = new ItemTouchHelper(callback);//触摸处理
        helper.attachToRecyclerView(mRecylerView);

        mAdapter=new NewsChannelAdapter(this,helper,enanbleItems,disabelItems);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int i) {
                int viewType=mAdapter.getItemViewType(i);
                return viewType==NewsChannelAdapter.TYPE_MY||viewType==NewsChannelAdapter.TYPE_OTHER ? 1 : 4;

            }
        });
        mRecylerView.setAdapter(mAdapter);
        mAdapter.setOnMyChannelItemClickListener((v,position)->
                Toast.makeText(NewsChannelActivity.this, enanbleItems.get(position).getChannelName() + position, Toast.LENGTH_SHORT).show());
    }

    public void onSaveData() {

        Observable
                .create((ObservableOnSubscribe<Boolean>) e -> {
                    List<NewsChannelBean> oldItems = mDao.query(Constant.NEWS_CHANNEL_ENABLE);
                    e.onNext(!compare(oldItems, mAdapter.getMyChannelItems()));
                })
                .subscribeOn(Schedulers.io())
                .doOnNext(aBoolean -> {
                    if (aBoolean) {
                        List<NewsChannelBean> enableItems = mAdapter.getMyChannelItems();
                        List<NewsChannelBean> disableItems = mAdapter.getOtherChannelItems();
                        //删除所有，在重新添加
                        mDao.removeAll();
                        for (int i = 0; i < enableItems.size(); i++) {
                            NewsChannelBean bean = enableItems.get(i);
                            mDao.add(bean.getChannelId(), bean.getChannelName(), Constant.NEWS_CHANNEL_ENABLE, i);
                        }
                        for (int i = 0; i < disableItems.size(); i++) {
                            NewsChannelBean bean = disableItems.get(i);
                            mDao.add(bean.getChannelId(), bean.getChannelName(), Constant.NEWS_CHANNEL_DIABEL, i);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isRefresh -> Rxbus.getInstance().post(NewsTabLayout.TAG, isRefresh), ErrorAction.error());
    }

    public synchronized <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
//        Collections.sort(a);
//        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }

}
