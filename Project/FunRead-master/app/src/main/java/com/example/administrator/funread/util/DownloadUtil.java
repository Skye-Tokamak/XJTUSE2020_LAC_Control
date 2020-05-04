package com.example.administrator.funread.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.administrator.funread.ErrorAction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static android.R.attr.path;

/**
 * 作者：created by weidiezeng on 2019/8/19 15:05
 * 邮箱：1067875902@qq.com
 * 描述：下载工具类
 */
public class DownloadUtil {

    public static Boolean saveImage(String url, Context context) {
        boolean flag = false;
        try {
            // 获取 bitmap
            Bitmap bitmap = Glide.with(context).asBitmap().load(url)
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            if (bitmap != null) {
                // 首先保存图片
                File appDir = new File(Environment.getExternalStorageDirectory(), "Toutiao");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                File file = new File(appDir, fileName);
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();

                // 其次把文件插入到系统图库
//                MediaStore.Images.Media.insertImage(InitApp.AppContext.getContentResolver(), file.getAbsolutePath(), fileName, null);
                // 最后通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

                flag = true;
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            ErrorAction.print(e);
            return false;
        }
        return flag;
    }
}
