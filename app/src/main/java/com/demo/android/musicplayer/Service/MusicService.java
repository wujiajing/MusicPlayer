package com.demo.android.musicplayer.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.demo.android.musicplayer.Base.music;

import java.io.IOException;
import java.util.ArrayList;


public class MusicService extends Service {

    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public static ArrayList<music>  PlayList = null;
    private int Cursor;


    //构建iBinder对象，以获取service实例
    private final IBinder mBinder = new myBinder();

    public class myBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void setCurrentCursor(int cursor) {
        this.Cursor = cursor;
    }

    //播放与暂停
    public void play() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
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
              setCurrentCursor(Cursor + 1);
          }
            play_music(Cursor);
    }


    public void play_music(int cursor) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(PlayList.get(cursor).getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
            unregisterReceiver(mReceiver);
            super.onDestroy();
    }

    @Override
    public void onCreate() {
        try{
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(mOnCompletionListener);
        registerReceiver(mReceiver,new IntentFilter(Intent.ACTION_HEADSET_PLUG));
        super.onCreate();
    }

    public MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
                play_next();
        }
    };

    //设置广播监听，当拔出耳机时音乐暂停
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
                Log.w("Broadcast","Received");
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        }
    };


}