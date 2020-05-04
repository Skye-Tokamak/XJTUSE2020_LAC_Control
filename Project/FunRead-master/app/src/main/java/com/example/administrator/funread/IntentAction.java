package com.example.administrator.funread;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * 作者：created by weidiezeng on 2019/8/16 14:50
 * 邮箱：1067875902@qq.com
 * 描述：分享操作类
 */
public class IntentAction {
    public static void send(@NonNull Context context,@NonNull String shareText){
        Intent shareIntent=new Intent()
                .setAction(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT,shareText);
        context.startActivity(Intent.createChooser(shareIntent,context.getString(R.string.share_to)));
    }

    public static void sendImage(@NonNull Context context, @NonNull Uri uri){
        Intent shareIntent=new Intent()
                .setAction(Intent.ACTION_SEND)
                .setType("image/*")
                .putExtra(Intent.EXTRA_STREAM,uri);
        context.startActivity(Intent.createChooser(shareIntent,context.getString(R.string.share_to)));
    }
}
