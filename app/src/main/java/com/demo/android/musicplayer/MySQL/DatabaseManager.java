package com.demo.android.musicplayer.MySQL;

import android.database.Cursor;
import android.util.Log;
import com.demo.android.musicplayer.Base.music;


import java.util.ArrayList;

/**
 * Created by kk on 2017/11/3.
 */

public class DatabaseManager {

    //获取音乐信息
    public ArrayList<music> getFavorMusicData(MyDBHelper myDBHelper) {
        ArrayList<music> list = new ArrayList<music>();
        Cursor cursor = myDBHelper.getWritableDatabase().rawQuery("select * from favor_music", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                music f_song = new music();
                //歌曲编号
                f_song.id = cursor.getInt(0);
                //歌曲名
                f_song.song = cursor.getString(1);
                //歌手
                f_song.singer = cursor.getString(2);
                //歌曲全路径
                f_song.path = cursor.getString(3);
                //歌曲时长
                f_song.duration = cursor.getInt(4);
                list.add(f_song);
            }
            cursor.close();
        }
        Log.w("db","获取收藏的全部音乐数据");
        return list;
    }

    //插入音乐数据
    public void setFavorData(MyDBHelper myDBHelper ,Object[] objects){
        myDBHelper.getWritableDatabase().execSQL("insert into favor_music values(null,?,?,?,?)", objects);
        Log.w("db","插入了新数据");
    }

    //删除数据
    public void delData(MyDBHelper myDBHelper,String s){
        myDBHelper.getWritableDatabase().execSQL("delete from favor_music where song = ?",new Object[]{s});
        Log.w("db","删除了数据");
    }

    //查询数据
    public boolean findData(MyDBHelper myDBHelper,String s){
        boolean check_exist=false;
        Cursor cursor=myDBHelper.getWritableDatabase().rawQuery("select * from favor_music where song = ?",new String[]{s});
        if (cursor!=null){
            while (cursor.moveToNext()){
                check_exist=true;
            }
        }
        cursor.close();
        return check_exist;
    }

    //更新数据

}
