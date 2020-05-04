package com.example.administrator.funread.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.funread.Constant;
import com.example.administrator.funread.InitApp;
import com.example.administrator.funread.R;
import com.example.administrator.funread.bean.news.NewsChannelBean;
import com.example.administrator.funread.database.DatabaseHelper;
import com.example.administrator.funread.database.table.NewChannelTable;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：created by weidiezeng on 2019/8/8 11:01
 * 邮箱：1067875902@qq.com
 * 描述：Channel表操作类
 */
public class NewsChannelDao {
    private static final String TAG="NewsChannelDao";
    private SQLiteDatabase mDatabase;
    public NewsChannelDao(){
        this.mDatabase=DatabaseHelper.getDatabase();
    }

    /**
     * 从array文件添加栏目标题id和标题名
     */
    public void addInitData(){
        String categoryId[]=InitApp.AppContext.getResources().getStringArray(R.array.mobile_news_id);
        String categoryName[]=InitApp.AppContext.getResources().getStringArray(R.array.mobile_news_name);

        for(int i=0;i<5;i++){
            add(categoryId[i],categoryName[i],Constant.NEWS_CHANNEL_ENABLE,i);
            //Log.d(TAG,categoryName[i]);
            Log.i(TAG,categoryName[i]);
        }
        for(int i=5;i<categoryId.length;i++){
            add(categoryId[i],categoryName[i],Constant.NEWS_CHANNEL_DIABEL,i);
            Log.i(TAG,categoryName[i]);
        }
    }

    /**
     * 把栏目信息添加进数据表中保存
     * @param channelId
     * @param channelName
     * @param isEnable
     * @param position
     * @return
     */
    public boolean add(String channelId,String channelName,int isEnable,int position){
        ContentValues values=new ContentValues();
        values.put(NewChannelTable.ID,channelId);
        values.put(NewChannelTable.NAME,channelName);
        values.put(NewChannelTable.IS_ENABLE,isEnable);
        values.put(NewChannelTable.POSITION,position);
        long result=mDatabase.insert(NewChannelTable.TABLENAME,null,values);
        return result!=-1;
    }

    /**查询可用标题，并返回
     * @param isEnable
     * @return
     */
    public List<NewsChannelBean> query(int isEnable){
        Cursor cursor=mDatabase.query(NewChannelTable.TABLENAME,null,NewChannelTable.IS_ENABLE+"=?",new String[]{isEnable+""},null,null,null);
        List<NewsChannelBean>list=new ArrayList<>();
        while(cursor.moveToNext()){
            NewsChannelBean bean=new NewsChannelBean();
            bean.setChannelId(cursor.getString(NewChannelTable.ID_ID));
            bean.setChannelName(cursor.getString(NewChannelTable.ID_NAME));
            bean.setIsEnable(cursor.getInt(NewChannelTable.ID_ISENABLE));
            bean.setPosition(cursor.getInt(NewChannelTable.ID_POSITION));
            list.add(bean);
        }
        cursor.close();
        return list;
    }

    /**查询所有标题，并返回
     * @return
     */
    public List<NewsChannelBean>queryAll(){
        Cursor cursor=mDatabase.query(NewChannelTable.TABLENAME,null,null,null,null,null,null);
        List<NewsChannelBean>List=new ArrayList<>();
        while(cursor.moveToNext()){
            NewsChannelBean bean=new NewsChannelBean();
            bean.setChannelId(cursor.getString(NewChannelTable.ID_ID));
            bean.setChannelName(cursor.getString(NewChannelTable.ID_NAME));
            bean.setIsEnable(cursor.getInt(NewChannelTable.ID_ISENABLE));
            bean.setPosition(cursor.getInt(NewChannelTable.ID_POSITION));
            List.add(bean);
        }
        cursor.close();
        return List;
    }
    public void updateAll(List<NewsChannelBean>list){

    }

    public boolean removeAll(){
        int result=mDatabase.delete(NewChannelTable.TABLENAME,null,null);
        return result!=-1;
    }
}
