package com.example.administrator.funread.module.base;

import android.app.ActivityManager;
import android.arch.lifecycle.Lifecycle;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.afollestad.materialdialogs.color.CircleView;
import com.example.administrator.funread.Constant;
import com.example.administrator.funread.R;
import com.example.administrator.funread.util.SettingUtil;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG="BaseActivity";
    protected SlidrInterface slidrInterface;
    protected Context context;
    private int iconType=-1;

    /**
     * 初始化Toolbar
     * @param toolbar
     * @param homeAsUpEnabled
     * @param title
     */
    protected void  initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title){
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        //将图标转换为可点击的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);//设置home是否应该显示为“up”启示
    }

    /**
     * 初始化滑动返回
     */
    protected void initSlidable(){
        int isSlidable= SettingUtil.getInstance().getSlidable();
        if(isSlidable!=Constant.SLIDABLE_DISABLE){
            SlidrConfig config=new SlidrConfig.Builder()
                    .edge(isSlidable==Constant.SlIDABLE_EDGE)
                    .build();
            slidrInterface=Slidr.attach(this,config);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.iconType=SettingUtil.getInstance().getCustomIconValue();
        this.context=this;
        initSlidable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int color=SettingUtil.getInstance().getColor();
        int drawable=Constant.ICONS_DRAWABLES[SettingUtil.getInstance().getCustomIconValue()];
        if(getSupportActionBar()!=null){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(CircleView.shiftColorDown(color));
            //最近任务栏上色
            ActivityManager.TaskDescription taskDescription=new ActivityManager.TaskDescription(
                    getString(R.string.app_name),
                    BitmapFactory.decodeResource(getResources(),drawable),
                    color
            );
            setTaskDescription(taskDescription);
            if(SettingUtil.getInstance().getNavBar()){
                getWindow().setNavigationBarColor(CircleView.shiftColorDown(color));//设置导航栏颜色
            }else{
                getWindow().setNavigationBarColor(Color.BLACK);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int count=getSupportFragmentManager().getBackStackEntryCount();
        if(count==0){
            super.onBackPressed();
        }else{
            getSupportFragmentManager().popBackStack();
        }

    }

    /**
     * 动态更换图标回调
     */
    @Override
    protected void onStop() {
        if (iconType != SettingUtil.getInstance().getCustomIconValue()) {
            new Thread(() -> {

                String act = ".SplashActivity_";

                //禁用
                for (String s : Constant.ICONS_TYPE) {
                    getPackageManager().setComponentEnabledSetting(new ComponentName(BaseActivity.this, getPackageName() + act + s),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                }

                act += Constant.ICONS_TYPE[SettingUtil.getInstance().getCustomIconValue()];

                //启用
                getPackageManager().setComponentEnabledSetting(new ComponentName(BaseActivity.this, getPackageName() + act),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,//此组件或应用程序已明确启用，而不管其清单中指定了什么。
                        PackageManager.DONT_KILL_APP);//表示不希望终止包含该组件的应用程序
            }).start();
        }
        super.onStop();
    }

    /**解决RxJava的内存泄漏问题
     * @param <X>
     * @return
     */
    public <X>AutoDisposeConverter<X> bindAutoDispose(){
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
        .from(this,Lifecycle.Event.ON_DESTROY));
    }
}
