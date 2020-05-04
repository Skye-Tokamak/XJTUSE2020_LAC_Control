package com.example.administrator.funread.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 作者：created by weidiezeng on 2019/8/13 16:50
 * 邮箱：1067875902@qq.com
 * 描述：网络操作类
 */
public class NetWorkUtil {
    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context){
        if(context!=null){
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            // 获取NetworkInfo对象
            NetworkInfo networkInfo=manager.getActiveNetworkInfo();
            return null!=networkInfo&&networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断wifi网络是否可用
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context){
        if(context!=null){
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            // 获取NetworkInfo对象
            NetworkInfo networkInfo=manager.getActiveNetworkInfo();
            if(null!=networkInfo&&networkInfo.getType()==ConnectivityManager.TYPE_WIFI){
                return networkInfo.isConnected();
            }
        }
        return  false;
    }
    /**
     * 判断MOBILE网络是否可用
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空 并且类型是否为MOBILE
            if (null != networkInfo && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return networkInfo.isAvailable();
        }
        return false;
    }
}
