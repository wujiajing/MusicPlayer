package com.demo.android.musicplayer.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.demo.android.musicplayer.Base.music;
import java.io.IOException;
import java.util.ArrayList;




public class MusicService extends Service {
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public ArrayList<music>  PlayList;
    private int Cursor;

    public static Boolean playing = false;



    //构建iBinder对象，以获取service实例
    private final IBinder mBinder = new myBinder();

    public class myBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

   // public MusicService() {  }//mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Music/我怀念的.mp3"

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public ArrayList<music> getPlayList() {
        return this.PlayList;
    }

    public void setupPlayList(ArrayList<music> playList) {
        this.PlayList = playList;
    }

    private int getPlayListSize() {
        return PlayList.size();
    }

    public void setCurrentCursor(int cursor) {
        this.Cursor = cursor;
    }

    public int getCurrentCursor() {
        return this.Cursor;
    }

    //播放
    public void play() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playing = false;
        } else {
            mediaPlayer.start();
            playing = true;
        }
    }

    //上一首
    public void play_pre() {
        if (Cursor == 0) {
            setCurrentCursor(PlayList.size() - 1);
        } else {
            setCurrentCursor(Cursor-1);
        }
        play_music(Cursor);
    }

    //下一首
    public void play_next() {
        if (Cursor == PlayList.size() - 1) {
            setCurrentCursor(0);
        } else {
            setCurrentCursor(Cursor+1);
        }
        play_music(Cursor);
    }

  //判断歌曲是否已收藏
   /* public boolean if_favor() {
        boolean favor=false;
        if (getCurrentTitle()!=null) {
        favor= databaseManager.findData(favor_DBHelper,getCurrentTitle());
        }
        return favor;
    }*/

    public void play_music(int cursor) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(PlayList.get(cursor).getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            playing = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTime(int cursor){
    }


  /*  public int getCurrentId() {
        int s=0;
        if (PlayList != null)
            s=PlayList.get(Cursor).getId();
        return s;
    }*/
    public String getCurrentTitle() {
        String s=null;
        if (PlayList != null)
            s=PlayList.get(Cursor).getSong();
        return s;
    }
    public String getCurrentSinger() {
        String s=null;
        if (PlayList != null)
            s= PlayList.get(Cursor).getSinger();
        return s;
    }
    public String getCurrentPath(){
        String s=null;
        if (PlayList!=null)
            s= PlayList.get(Cursor).getPath();
        return s;
    }
    public int getCurrentDuration() {
        int s=0;
        if (PlayList != null)
            s=PlayList.get(Cursor).getDuration();
        return s;
    }

    @Override
    public void onDestroy() {
        //回收资源
       // if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
       // }
            super.onDestroy();
    }

    @Override
    public void onCreate() {
        try{
            // mediaPlayer.reset();
            //mediaPlayer.setDataSource(PlayList.get(0).getUrl());
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(mOnCompletionListener);
        super.onCreate();
    }

    public MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (PlayList!=null) {
                play_next();
            }
        }
    };




}