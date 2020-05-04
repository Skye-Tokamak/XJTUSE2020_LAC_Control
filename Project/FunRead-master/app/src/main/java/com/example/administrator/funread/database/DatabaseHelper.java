package com.example.administrator.funread.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.funread.InitApp;
import com.example.administrator.funread.database.table.NewChannelTable;

/**
 * 作者：created by weidiezeng on 2019/8/8 10:32
 * 邮箱：1067875902@qq.com
 * 描述：数据库操作类
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="QuYue";
    private static final int DB_VERSION=1;
    private static final String CLEAR_TABLE_DATA="delete from ";
    private static final String DROP_TABLE="drop table if exists ";
    private static  DatabaseHelper instance=null;
    private static SQLiteDatabase database=null;

    public DatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }
    private static synchronized DatabaseHelper getInstance(){
        if(instance==null){
            instance=new DatabaseHelper(InitApp.AppContext,DB_NAME,null,DB_VERSION);

        }
        return instance;
    }
    public static synchronized SQLiteDatabase getDatabase(){
        if(database==null){
            database=getInstance().getWritableDatabase();
        }
        return database;
    }
    public static  synchronized void closeDatabase(){
        if(database!=null){
            database.close();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        //database.execSQL(SearchHistoryTable.CREATE_TABLE);
        database.execSQL(NewChannelTable.CREATE_TABLE);

    }

    /**
     * 数据库版本升级问题
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       /* switch (oldVersion){
            case 1:
                //database.execSQL();
        }*/
    }
}
