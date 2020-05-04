package com.example.administrator.funread.module.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.funread.bean.LoadingEndBean;
import com.example.administrator.funread.R;
import com.example.administrator.funread.util.Rxbus;
import com.example.administrator.funread.util.SettingUtil;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 作者：created by weidiezeng on 2019/8/7 16:09
 * 邮箱：1067875902@qq.com
 * 描述：
 * @param <T>
 */
public abstract class BaseListFragMent<T extends IBasePresenter> extends LazyLoadFragment<T> implements IBaseListView<T>,SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG="BaseListFragment";
    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected Items oldItems=new Items();
    protected MultiTypeAdapter mAdapter;
    protected boolean canLoadMore=false;
   protected io.reactivex.Observable<Integer>mObservable;

    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(View view) {
        recyclerView=view.findViewById(R.id.recyler_view);
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout=view.findViewById(R.id.refresh_layout);
        //设置下拉刷新的按钮颜色
        swipeRefreshLayout.setColorSchemeColors(SettingUtil.getInstance().getColor());
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setColorSchemeColors(SettingUtil.getInstance().getColor());
    }


    /**
     * 如果没有更多了，要先判断一下。
     * 　　如果之前没有数据，直接在尾巴加一个LoadingEndBean
     * 　　如果之前有数据，先移除最后一个数据，将一个LoadingEndBean加到尾巴。
     * 　　最后将canLoadMore为false。
     * 　　其实这里是处理没有更多的情况
     */
    @Override
    public void onShowNoMore() {
        getActivity().runOnUiThread(()->{
            if(oldItems.size()>0){
                Items newItems=new Items(oldItems);
                newItems.remove(newItems.size()-1);
                newItems.add(new LoadingEndBean());
                mAdapter.setItems(newItems);
                mAdapter.notifyDataSetChanged();
            }else if(oldItems.size()==0){
                oldItems.add(new LoadingEndBean());
                mAdapter.setItems(oldItems);
                mAdapter.notifyDataSetChanged();
            }
            canLoadMore=false;
        });
    }

    @Override
    public void fetechData() {
        mObservable=Rxbus.getInstance().register(BaseListFragMent.TAG);
        mObservable.subscribe(integer -> mAdapter.notifyDataSetChanged());
    }

    @Override
    public void onShowLoading() {
        swipeRefreshLayout.post(()->swipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void onHideLoading() {
        swipeRefreshLayout.post(()->swipeRefreshLayout.setRefreshing(false));
    }

    @Override
    public void onShowNetError() {
        Toast.makeText(getActivity(),R.string.network_error,Toast.LENGTH_SHORT).show();
        getActivity().runOnUiThread(()->{
            mAdapter.setItems(new Items());
            mAdapter.notifyDataSetChanged();
            canLoadMore=false;

        });
    }

    @Override
    public void onRefresh() {
        int firstVisibleItemPosition=((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if(firstVisibleItemPosition==0){
            presenter.doRefresh();
            return;
        }
        recyclerView.scrollToPosition(5);
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroy() {
        Rxbus.getInstance().unregister(BaseListFragMent.TAG,mObservable);
        super.onDestroy();
    }
}
