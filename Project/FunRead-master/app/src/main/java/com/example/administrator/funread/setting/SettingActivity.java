package com.example.administrator.funread.setting;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.example.administrator.funread.IntentAction;
import com.example.administrator.funread.R;
import com.example.administrator.funread.module.base.BaseActivity;
import com.example.administrator.funread.util.SettingUtil;

public class SettingActivity extends BaseActivity implements ColorChooserDialog.ColorCallback {
    public static final String EXTRA_SHOW_FRAGMENT = "show_fragment";
    public static final String EXTRA_SHOW_FRAGMENT_ARGUMENTS = "show_fragment_args";
    public static final String EXTRA_SHOW_FRAGMENT_TITLE = "show_fragment_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        String initFragment=getIntent().getStringExtra(EXTRA_SHOW_FRAGMENT);
        Bundle initArguments=getIntent().getBundleExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS);
        String initTitle=getIntent().getStringExtra(EXTRA_SHOW_FRAGMENT_TITLE);

        if(TextUtils.isEmpty(initFragment)){
            setUpFragment(GeneralPreferenceFragment.class.getName(),initArguments);
        }else{
            setUpFragment(initFragment,initArguments);
        }

        Toolbar toolbar=findViewById(R.id.toolbar);
        initToolBar(toolbar,true,TextUtils.isEmpty(initTitle)? getString(R.string.title_settings) : initTitle);
    }

    private void setUpFragment(String fragmentName,Bundle bundle){
        Fragment fragment=Fragment.instantiate(this,fragmentName,bundle);
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        //填充container
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commitAllowingStateLoss();

    }
    public Intent onBuildStartFragmentIntent(String fragmentName,Bundle args,String title){
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setClass(this,getClass());
        intent.putExtra(EXTRA_SHOW_FRAGMENT,fragmentName);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS,args);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_TITLE,title);
        return intent;

    }

    /**
     * 打开自动切换夜间和字体大小设置
     * @param fragmentName
     * @param args
     * @param resultTo
     * @param resultCode
     * @param title
     */
    public void startWithFragment(String fragmentName,Bundle args,Fragment resultTo,int resultCode,String title){
        Intent intent=onBuildStartFragmentIntent(fragmentName,args,title);
        if(resultTo==null){
            startActivity(intent);
        }else {
            resultTo.startActivityForResult(intent,resultCode);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=item.getItemId();
        if(itemId==R.id.action_share){
            IntentAction.send(SettingActivity.this,getString(R.string.share_app_text)+getString(R.string.source_code_url));
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 更改主题颜色回调接口
     * @param dialog
     * @param selectedColor
     */
    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        //标题栏
        if(getSupportActionBar()!=null){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(selectedColor));
        }
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            //状态栏上色
            getWindow().setStatusBarColor(selectedColor);
            //任务栏上色
            ActivityManager.TaskDescription description=new ActivityManager.TaskDescription(
                    getString(R.string.app_name),
                    BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher1_round),
                    selectedColor
            );
            setTaskDescription(description);
            //导航栏上色
            if(SettingUtil.getInstance().getNavBar()){
                getWindow().setNavigationBarColor(selectedColor);
            }else {
                getWindow().setNavigationBarColor(Color.BLACK);
            }
        }
        if (!dialog.isAccentMode()) {
            SettingUtil.getInstance().setColor(selectedColor);
        }


    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }
}
