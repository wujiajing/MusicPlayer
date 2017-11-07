package com.demo.android.musicplayer.Utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.demo.android.musicplayer.Base.music;

import java.util.ArrayList;

/**
 * Created by kk on 2017/10/27.
 */


/**
 * 音乐工具类,
 */
public class MusicUtil {
    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */
    public static ArrayList<music> getMusicData(Context context) {
        ArrayList<music> list = new ArrayList<music>();
        // 媒体库查询语句（写一个工具类MusicUtil）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                music m_song = new music();
                //歌曲编号
                m_song.id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                //歌曲名
                m_song.song = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                //歌手
                m_song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                //歌曲全路径
                m_song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                //歌曲时长
                m_song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                list.add(m_song);
            }
            // 关闭游标，释放资源
            cursor.close();
        }
        return list;
    }





}



