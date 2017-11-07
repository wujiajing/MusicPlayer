package com.demo.android.musicplayer.Base;

/**
 * Created by kk on 2017/10/27.
 */

public class music {
    // 歌曲的编号
    public int id;
    //歌曲名
    public String song;
    //歌手
    public String singer;
    //歌曲的地址
    public String path;
    //歌曲长度
    public int duration;


    public int getId(){return id;}
    public String getSong(){return song;}
    public String getSinger(){return singer;}
    public String getPath(){return path;}
    public int getDuration(){return duration;}



}
