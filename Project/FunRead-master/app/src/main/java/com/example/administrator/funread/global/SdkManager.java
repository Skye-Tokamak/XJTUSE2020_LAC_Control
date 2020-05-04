package com.example.administrator.funread.global;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 作者：created by weidiezeng on 2019/8/12 10:04
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class SdkManager {
    public static void initStetho(Context context) {
        Stetho.initializeWithDefaults(context);//Stetho调试工具
    }

    public static OkHttpClient.Builder initInterceptor(OkHttpClient.Builder builder) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return builder;
    }

    public static void initLeakCanary(Application app) {
        if (LeakCanary.isInAnalyzerProcess(app)) {//内存泄露检测
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(app);
    }
}
