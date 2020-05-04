package com.example.administrator.funread.module.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.funread.Constant;
import com.example.administrator.funread.R;
import com.example.administrator.funread.Test.Test_Fragment;
import com.example.administrator.funread.adapter.base.BasePagerAdapter;
import com.example.administrator.funread.bean.news.NewsChannelBean;
import com.example.administrator.funread.database.dao.NewsChannelDao;
import com.example.administrator.funread.module.base.BaseListFragMent;
import com.example.administrator.funread.module.news.article.NewsArticlePresenter;
import com.example.administrator.funread.module.news.article.NewsArticleView;
import com.example.administrator.funread.module.news.channel.NewsChannelActivity;
import com.example.administrator.funread.util.Rxbus;
import com.example.administrator.funread.util.SettingUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

/**
 * 作者：created by weidiezeng on 2019/8/6 16:01
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class NewsTabLayout extends Fragment {


    public static final String TAG = "NewsTabLayout";
    private static NewsTabLayout instance = null;
    /*@BindView(R.id.tab_layout_news)
    TabLayout mTabLayoutNews;*/
    @BindView(R.id.add_channel_iv)
    ImageView mAddChannelIv;
    @BindView(R.id.header_layout)
    LinearLayout mHeaderLayout;
   // @BindView(R.id.view_pager_news)
    ViewPager mViewPagerNews;
    Unbinder unbinder;
    private BasePagerAdapter mAdapter;
    private NewsChannelDao dao = new NewsChannelDao();
    private List<Fragment> mFragmentList;
    private List<String> titleList;
    private Observable<Boolean> mObservable;
    private Map<String, Fragment> mMap = new HashMap<>();


    public static NewsTabLayout getInstance() {
        if (instance == null) {
            instance = new NewsTabLayout();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_tab, container, false);
        initView(view);
        initData();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mHeaderLayout.setBackgroundColor(SettingUtil.getInstance().getColor());
    }

    /**
     * 初始化视图
     *
     * @param view
     */
    private void initView(View view) {
        //tablayout与viewpager绑定
        TabLayout mTabLayoutNews = view.findViewById(R.id.tab_layout_news);
        mViewPagerNews = view.findViewById(R.id.view_pager_news);
        try {
            mTabLayoutNews.setupWithViewPager(mViewPagerNews);
        }catch (Exception e){
            e.printStackTrace();
        }
        mTabLayoutNews.setTabMode(TabLayout.MODE_SCROLLABLE);
        //打开NewsChannelActivity
        ImageView mAddChannelIv = view.findViewById(R.id.add_channel_iv);
        mAddChannelIv.setOnClickListener(v -> startActivity(new Intent(getActivity(), NewsChannelActivity.class)));
        mHeaderLayout = view.findViewById(R.id.header_layout);
        mHeaderLayout.setBackgroundColor(SettingUtil.getInstance().getColor());
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initTabs();
        //打印获取的标题
        /*for(int i=0;i<titleList.size();i++){
            Log.d(NewsTabLayout.TAG,titleList.get(i));
        }*/
        //设置viewpager的adapter
        mAdapter = new BasePagerAdapter(getChildFragmentManager(), mFragmentList, titleList);
        mViewPagerNews.setAdapter(mAdapter);
        //设置视图层次结构中处于空闲状态时，应该保留在当前页面两侧的页面数量
        mViewPagerNews.setOffscreenPageLimit(15);

        mObservable = Rxbus.getInstance().register(NewsTabLayout.TAG);
        mObservable.subscribe(isRefresh -> {
            if (isRefresh) {
                initTabs();
                mAdapter.recreateItems(mFragmentList, titleList);
            }
        });

    }

    /**
     * 出事栏目列表
     */
    private void initTabs() {
        //测试代码
        /*mFragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        mFragmentList.add(new Test_Fragment());
        titleList.add("推荐");
        mFragmentList.add(new Test_Fragment());
        titleList.add("热点");
        mFragmentList.add(new Test_Fragment());
        titleList.add("视频");
        mFragmentList.add(new Test_Fragment());
        titleList.add("社会");
        mFragmentList.add(new Test_Fragment());
        titleList.add("娱乐");*/

        /*测试api
        NewsArticlePresenter presenter=new NewsArticlePresenter();
        presenter.doLoadData("top");*/
        //dao=new NewsChannelDao();
        List<NewsChannelBean> channelBeanList = dao.query(1);
        mFragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        if (channelBeanList.size() == 0) {
            dao.addInitData();
            channelBeanList = dao.query(Constant.NEWS_CHANNEL_ENABLE);
        }
        for (NewsChannelBean bean : channelBeanList) {
            Fragment fragment = null;
            String channelId = bean.getChannelId();
            if (mMap.containsKey(channelId)) {
                mFragmentList.add(mMap.get(channelId));
            } else {
                //初始化newsArticleView
                fragment=NewsArticleView.newInstance(channelId);
                mFragmentList.add(fragment);
            }
            titleList.add(bean.getChannelName());
            if (fragment != null) {
                mMap.put(channelId, fragment);
            }
        }

    }

    /**
     * 双击刷新
     */
    public void onDoubleClick() {
        if (titleList != null && titleList.size() > 0 && mFragmentList != null && mFragmentList.size() > 0) {
            int item = mViewPagerNews.getCurrentItem();
            ((BaseListFragMent) mFragmentList.get(item)).onRefresh();
        }
    }

    @Override
    public void onDestroy() {
        Rxbus.getInstance().unregister(NewsTabLayout.TAG, mObservable);
        if (instance != null) {
            instance = null;
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
