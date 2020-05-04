package com.example.administrator.funread;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.administrator.funread.setting.SettingActivity;
import com.example.administrator.funread.module.base.BaseActivity;
import com.example.administrator.funread.module.csdn.CSDNTabLayout;
import com.example.administrator.funread.module.news.NewsTabLayout;
import com.example.administrator.funread.module.photo.PhotoTabLayout;
import com.example.administrator.funread.module.search.SearchActivity;
import com.example.administrator.funread.module.weather.WeatherTabLayout;
import com.example.administrator.funread.util.SettingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigation;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private static final String TAG="MainActivity";
    private static final String POSITION="position";
    private static final String SELECT_ITEM="bottomnavigationSelectItem";
    private static final int FRAGMENT_NEWS=0;
    private static final int FRAGMENT_CSDN=1;
    private static final int FRAGMENT_WEATHER=3;
    private static final int FRAGMENT_PHOTO=2;
    private NewsTabLayout newsTabLayout;
    private CSDNTabLayout csdnTabLayout;
    private PhotoTabLayout photoTabLayout;
    private WeatherTabLayout weatherTabLayout;
    private long exitTime = 0;
    private long firstClickTime = 0;
    private int position;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        补充登录信息

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        if(savedInstanceState!=null){
            newsTabLayout=(NewsTabLayout)getSupportFragmentManager().findFragmentByTag(NewsTabLayout.class.getName());
            photoTabLayout = (PhotoTabLayout) getSupportFragmentManager().findFragmentByTag(PhotoTabLayout.class.getName());
            csdnTabLayout = (CSDNTabLayout) getSupportFragmentManager().findFragmentByTag(CSDNTabLayout.class.getName());
            weatherTabLayout = (WeatherTabLayout) getSupportFragmentManager().findFragmentByTag(WeatherTabLayout.class.getName());

            //恢复recreate之前的状态
            showFragement(savedInstanceState.getInt(POSITION));
            mBottomNavigation.setSelectedItemId(savedInstanceState.getInt(SELECT_ITEM));
        }else{
            showFragement(FRAGMENT_NEWS);
        }
    }

    @Override
    protected void initSlidable() {
        //禁止滑动返回
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION,position);
        outState.putInt(SELECT_ITEM,mBottomNavigation.getSelectedItemId());
    }

    private void initView(){
        mToolbar.inflateMenu(R.menu.menu_activty_main);
        //解决item>3出现位移的情况
        mBottomNavigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        //BottomNavigationViewHelper.disableShiftMode(mBottomNavigation);
        setSupportActionBar(mToolbar);

        mBottomNavigation.setOnNavigationItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.action_news:
                    showFragement(FRAGMENT_NEWS);
                    doubleClick(FRAGMENT_NEWS);//双击刷新
                    break;
                case R.id.action_csdn:
                    showFragement(FRAGMENT_CSDN);
                    doubleClick(FRAGMENT_CSDN);
                    break;
                case R.id.action_photo:
                    showFragement(FRAGMENT_PHOTO);
                    doubleClick(FRAGMENT_PHOTO);
                    break;
                case R.id.action_weather:
                    showFragement(FRAGMENT_WEATHER);;
                    doubleClick(FRAGMENT_WEATHER);
                    break;
            }
            return true;
        } );

        //这个类提供了一种方便的方法来将DrawerLayout的功能和框架ActionBar结合起来，从而实现导航抽屉的推荐设计
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,mDrawerLayout,mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();//将折叠项指示器或者启示项的状态与链接的DrawerLayout同步
        mNavView.setNavigationItemSelectedListener(this);
    }

    /**
     * @param index
     */
    public void doubleClick(int index){
        long secondClickTime=System.currentTimeMillis();
        if(secondClickTime-firstClickTime>500){
            switch (index){
                case FRAGMENT_NEWS:
                    newsTabLayout.onDoubleClick();
                    break;
                case FRAGMENT_CSDN:
                    break;
                case FRAGMENT_PHOTO:
                    break;
                case FRAGMENT_WEATHER:
                    Intent intent = new Intent();
                //SoilsenerActivity.class为想要跳转的Activity
                    break;
            }
        }else{
            firstClickTime=secondClickTime;
        }

    }

    /**
     * @param index
     */
    private void showFragement(int index){

        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        hideFragMent(ft);
        position=index;
        switch (index){
            case FRAGMENT_NEWS:
                mToolbar.setTitle(R.string.title_news);
                /**
                 * 如果Fragment为空，就新建一个实例
                 * 如果不为空，就将它从栈中显示出来
                 */
                if (newsTabLayout == null) {
                    newsTabLayout = NewsTabLayout.getInstance();
                    ft.add(R.id.container, newsTabLayout, NewsTabLayout.class.getName());
                } else {
                    ft.show(newsTabLayout);
                }
                break;
            case FRAGMENT_CSDN:
                mToolbar.setTitle(R.string.title_csdn);
                /**
                 * 如果Fragment为空，就新建一个实例
                 * 如果不为空，就将它从栈中显示出来
                 */
                if (csdnTabLayout == null) {
                    csdnTabLayout = CSDNTabLayout.getInstance();
                    ft.add(R.id.container, csdnTabLayout, CSDNTabLayout.class.getName());
                } else {
                    ft.show(csdnTabLayout);
                }
                break;
            case FRAGMENT_PHOTO:
                mToolbar.setTitle(R.string.title_photo);
                /**
                 * 如果Fragment为空，就新建一个实例
                 * 如果不为空，就将它从栈中显示出来
                 */
                if (photoTabLayout == null) {
                    photoTabLayout = photoTabLayout.getInstance();
                    ft.add(R.id.container, photoTabLayout, PhotoTabLayout.class.getName());
                } else {
                    ft.show(photoTabLayout);
                }
                break;
            case FRAGMENT_WEATHER:
                mToolbar.setTitle(R.string.title_weather);
                /**
                 * 如果Fragment为空，就新建一个实例
                 * 如果不为空，就将它从栈中显示出来
                 */
                if (weatherTabLayout == null) {
                    weatherTabLayout = weatherTabLayout.getInstance();
                    ft.add(R.id.container, weatherTabLayout,WeatherTabLayout.class.getName());
                } else {
                    ft.show(weatherTabLayout);
                }

        }
        ft.commit();
    }
    private void hideFragMent(FragmentTransaction ft){
        if(newsTabLayout!=null){
            ft.hide(newsTabLayout);
        }
        if(csdnTabLayout!=null){
            ft.hide(csdnTabLayout);
        }
        if(photoTabLayout!=null){
            ft.hide(photoTabLayout);
        }
        if(weatherTabLayout!=null){
            ft.hide(weatherTabLayout);
        }
    }

    /**
     * 初始化菜单栏布局
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activty_main,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - exitTime) < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, R.string.double_click_exit, Toast.LENGTH_SHORT).show();
            exitTime = currentTime;
        }
    }

    /***
     * 菜单栏事件处理
     *  @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_search){
            //搜索功能暂时未做
            //startActivity(new Intent(MainActivity.this,SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.nav_switch_night_mode:
                int mode=getResources().getConfiguration().uiMode&Configuration.UI_MODE_NIGHT_MASK;
                if(mode==Configuration.UI_MODE_NIGHT_YES){
                    SettingUtil.getInstance().setIsNightMode(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }else{
                    SettingUtil.getInstance().setIsNightMode(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                recreate();
                return  false;
            case R.id.nav_setting:
                startActivity(new Intent(this,SettingActivity.class));
                mDrawerLayout.closeDrawers();
                return false;
            case R.id.nav_share:
                Intent shareIntent=new Intent()
                        .setAction(Intent.ACTION_SEND)
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT,getString(R.string.share_app_text)+getString(R.string.source_code_url));
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_to)));//活动选择器
                mDrawerLayout.closeDrawers();
                return false;

        }
        return false;
    }
}
