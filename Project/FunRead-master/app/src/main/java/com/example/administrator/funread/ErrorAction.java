package com.example.administrator.funread;

import android.support.annotation.NonNull;

import io.reactivex.functions.Consumer;

/**
 * 作者：created by weidiezeng on 2019/8/11 10:18
 * 邮箱：1067875902@qq.com
 * 描述：
 */
public class ErrorAction {
    @NonNull
    public static Consumer<Throwable> error() {
        return throwable -> {
            if (BuildConfig.DEBUG) {
                throwable.printStackTrace();
            }
        };
    }

    public static void print(@NonNull Throwable throwable) {
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace();
        }
    }
}
