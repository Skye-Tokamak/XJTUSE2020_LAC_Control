package com.example.administrator.funread;

import android.graphics.Color;

/**
 * 作者：created by weidiezeng on 2019/8/7 09:23
 * 邮箱：1067875902@qq.com
 * 描述：常量
 */
public class Constant {
    public static final int[] TAG_COLORS=new int[]{
            Color.parseColor("#90C5F0"),
            Color.parseColor("#91CED5"),
            Color.parseColor("#F88F55"),
            Color.parseColor("#C0AFD0"),
            Color.parseColor("#E78F8F"),
            Color.parseColor("#67CCB7"),
            Color.parseColor("#F6BC7E")
    };
    public static final int[] ICONS_DRAWABLES=new int[]{
            R.mipmap.ic_launcher1,
            R.mipmap.ic_launcher1_round

    };
    public static final String[] ICONS_TYPE=new String[]{
            "circle","square"
    };
    public static final int SLIDABLE_DISABLE=0;//禁止滑动
    public  static final int SlIDABLE_EDGE=1;//边缘滑动
    public static final int SLIDABLE_FULL=2;//全屏滑动

    public static final int NEWS_CHANNEL_ENABLE=1;
    public static final int NEWS_CHANNEL_DIABEL=0;

    /**
     * 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
     * {@link com.example.administrator.funread.module.news.content.NewsContentPresenter#openImage(String)}
     */
    public static final String JS_INJECT_IMG = "javascript:(function(){" +
            "var objs = document.getElementsByTagName(\"img\"); " +
            "for(var i=0;i<objs.length;i++)  " +
            "{"
            + "    objs[i].onclick=function()  " +
            "    {  "
            + "        window.imageListener.openImage(this.src);  " +
            "    }  " +
            "}" +
            "})()";
}
