package com.example.administrator.funread.module.news.content;

import android.content.Intent;

import android.os.Bundle;

import com.example.administrator.funread.InitApp;
import com.example.administrator.funread.R;
import com.example.administrator.funread.bean.news.NewsContentBean;
import com.example.administrator.funread.module.base.BaseActivity;

public class NewsContentActivity extends BaseActivity {

    private static final String TAG="NewsContenActivity";

    public static void lauch(NewsContentBean bean){
        InitApp.AppContext.startActivity(new Intent(InitApp.AppContext,NewsContentActivity.class)
            .putExtra(TAG,bean)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        Intent intent=getIntent();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,
                        NewsContentFragment.newInstance(intent.getParcelableExtra(TAG)))
                .commit();
    }
}
