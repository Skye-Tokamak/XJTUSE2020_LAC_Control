package com.example.administrator.funread.module.base;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

/**
 * 作者：created by weidiezeng on 2019/8/7 14:46
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public abstract class BaseFragment<T extends  IBasePresenter> extends Fragment implements IBaseView<T>{
   protected T presenter;
   protected Context context;

    /**绑定布局文件
     * @return
     */
   protected abstract int attachLayoutId();
    /**
     * 初始化数据
     */
    protected abstract void initData() throws NullPointerException;

    /**初始化视图控件
     * @param view
     */
   protected abstract void initView(View view);

   protected void initToolBar(Toolbar toolbar,boolean homeAsUpEnabled,String title){
       ((BaseActivity)getActivity()).initToolBar(toolbar,homeAsUpEnabled,title);
   }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(attachLayoutId(),container,false);
        initView(view);
        initData();
        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }


    @Override
    public <X> AutoDisposeConverter<X> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(
                this,Lifecycle.Event.ON_DESTROY
        ));
    }
}
