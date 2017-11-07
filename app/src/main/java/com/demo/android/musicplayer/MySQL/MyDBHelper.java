package com.demo.android.musicplayer.MySQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by kk on 2017/11/3.
 */

public class MyDBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DBNAME = "favor.db";



    //构造函数
    public MyDBHelper(Context context) {
        super(context,DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE favor_music(_id integer primary key autoincrement," +
                "song varchar,singer varchar,path varchar,duration integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }


}