package com.demo.android.musicplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.android.musicplayer.Adapter.FragmentAdapter;
import com.demo.android.musicplayer.Adapter.MusicAdapter;
import com.demo.android.musicplayer.Base.BaseFragment;
import com.demo.android.musicplayer.Base.music;
import com.demo.android.musicplayer.MySQL.DatabaseManager;
import com.demo.android.musicplayer.MySQL.MyDBHelper;
import com.demo.android.musicplayer.Service.MusicService;
import com.demo.android.musicplayer.Utils.MusicUtil;
import com.demo.android.musicplayer.fragment.Fragment_favor;
import com.demo.android.musicplayer.fragment.Fragment_local;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.demo.android.musicplayer.Service.MusicService.mediaPlayer;
import static com.demo.android.musicplayer.fragment.Fragment_favor.fListView;
import static com.demo.android.musicplayer.fragment.Fragment_favor.fadapter;
import static com.demo.android.musicplayer.fragment.Fragment_favor.flist;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private List<BaseFragment> list;

    private ImageView localBtn;
    private ImageView favorBtn;
    private boolean prepared=false;

    private ImageButton playBtn;
    private ImageButton nextBtn;
    private ImageButton preBtn;
    private ImageButton myfavorBtn;
    private SeekBar seekBar;
    private TextView timeView;
    private TextView titleView;
    private SimpleDateFormat time = new SimpleDateFormat("m:ss");

    private MusicService musicService;
    public static ArrayList<music> musicList;
    private MyDBHelper favor_DBHelper;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicService=new MusicService();
        favor_DBHelper=new MyDBHelper(MainActivity.this);
        databaseManager=new DatabaseManager();
        connection();
        musicList= MusicUtil.getMusicData(MainActivity.this);
        musicService.PlayList = musicList;
        Log.w("mainActivity", "PlayList == musicList");
        //添加数据
        list = new ArrayList<BaseFragment>();
        list.add(new Fragment_local());
        list.add(new Fragment_favor());
        initView();
        initContorlBtn();
        initSeekBar();
        //监听媒体播放器
        musicService.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.w("mediaPlayer","had been prepared");
                prepared=true;
            }
        });

    }

    //初始化进度条
    private void initSeekBar(){
    //进度条初始化
        seekBar = (SeekBar)this.findViewById(R.id.seekBar);
        seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
        seekBar.setMax(musicService.mediaPlayer.getDuration());
    }

    // 初始化viewpager数据
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        localBtn = (ImageView) findViewById(R.id.musicbtn);
        favorBtn = (ImageView) findViewById(R.id.favorbtn);
        //监听按钮切换Fragment
        localBtn.setOnClickListener(this);
        favorBtn.setOnClickListener(this);
        //绑定适配器
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        //设置viewpager切换监听,，默认显示播放列表界面
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                setTabSelection(arg0);
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }
            @Override
            public void onPageScrollStateChanged(int arg0) { }
        });
        setTabSelection(0);
    }

    //切换fragment时更改相应图标状态，同时更新收藏列表数据
    private void setTabSelection(int index) {
        localBtn.setImageResource(R.drawable.actionbar_music);
        favorBtn.setImageResource(R.drawable.desk2_btn_love_prs);
        switch (index) {
            case 0:
                localBtn.setImageResource(R.drawable.actionbar_music_prs);
                break;
            case 1:
                favorBtn.setImageResource(R.drawable.btn_love_prs);
                flist = databaseManager.getFavorMusicData(favor_DBHelper);
                fadapter = new MusicAdapter(this,flist);
                fListView.setAdapter(fadapter);
                break;
        }
    }

    //初始化控制栏
    private void initContorlBtn() {
        playBtn = (ImageButton) findViewById(R.id.btn_play);
        nextBtn = (ImageButton) findViewById(R.id.btn_next);
        preBtn = (ImageButton) findViewById(R.id.btn_pre);
        myfavorBtn = (ImageButton) findViewById(R.id.btn_to_favor);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        timeView = (TextView) findViewById(R.id.time);
        titleView = (TextView) findViewById(R.id._title);
        playBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        preBtn.setOnClickListener(this);
        myfavorBtn.setOnClickListener(this);
    }

    //导航栏监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.musicbtn:
                setTabSelection(0);
                //设置view显示的页面
                viewPager.setCurrentItem(0);
                break;
            case R.id.favorbtn:
                setTabSelection(1);
                viewPager.setCurrentItem(1);
                break;
            case R.id.btn_play:
                //调用MusicService中的方法
                Log.w("Click ", "play or pause music");
                if (prepared) {
                    musicService.play();
                } else {
                    Toast.makeText(this, "请选择播放歌曲", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_next:
                Log.w("Click ", "turn to next song");
                musicService.play_next();
                break;
            case R.id.btn_pre:
                Log.w("Click ", "turn to previous song");
                musicService.play_pre();
                break;
            case R.id.btn_to_favor:
                //收藏音乐
                if (musicService.getCurrentTitle() != null) {
                    if (if_favor()) {
                        databaseManager.delData(favor_DBHelper, musicService.getCurrentTitle());
                    } else {
                        Object[] arr;
                        arr = new Object[]{musicService.getCurrentTitle(), musicService.getCurrentSinger(),
                                musicService.getCurrentPath(), musicService.getCurrentDuration()};
                        databaseManager.setFavorData(favor_DBHelper, arr);
                    }
                }
                    break;
                    default:break;
        }
    }

    //绑定服务
    private void connection() {
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        Log.w("SERVICE ","had been binded");
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.myBinder) service).getService();
            Log.w("music", "服务已连接");
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            Log.w("music", "服务未连接");
        }
    };

    public MusicService getMusicService(){
        return musicService;
    }

    @Override
    protected void onResume() {
        //重新初始化
        if(prepared) {titleView.setText(musicService.getCurrentTitle());}
        if(if_favor()){
            myfavorBtn.setImageResource(R.drawable.desk2_btn_loved_prs);
        }else{
            myfavorBtn.setImageResource(R.drawable.desk2_btn_love_prs);
        }
        if (mediaPlayer.isPlaying()) {
            playBtn.setImageResource(R.drawable.notification_pause);
        } else {
            playBtn.setImageResource(R.drawable.notification_play);
        }
        if(prepared) {titleView.setText(musicService.getCurrentTitle());}
        seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
        seekBar.setMax(musicService.mediaPlayer.getDuration());
        handler.post(runnable);
     //   handler1.post(runnable1);
        super.onResume();
    }

    public Handler handler=new Handler();
    //更新进度条
    public Runnable runnable=new Runnable() {
        @Override
        public void run() {
            //更新进度条
             timeView.setText(time.format(musicService.mediaPlayer.getCurrentPosition()) + "/"
                        + time.format(musicService.mediaPlayer.getDuration()));
             seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
             seekBar.setMax(musicService.mediaPlayer.getDuration());
             seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            musicService.mediaPlayer.seekTo(seekBar.getProgress());
                        }
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) { }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) { }
                });
            //当前播放歌曲显示栏
            if(prepared) {titleView.setText(musicService.getCurrentTitle());}
            //更新收藏图标
            if(if_favor()){
                myfavorBtn.setImageResource(R.drawable.desk2_btn_loved_prs);
            }else{
                myfavorBtn.setImageResource(R.drawable.desk2_btn_love_prs);
            }
            //更新播放按钮状态
            if (mediaPlayer.isPlaying()) {
                playBtn.setImageResource(R.drawable.notification_pause);
            } else {
                playBtn.setImageResource(R.drawable.notification_play);
            }
            handler.postDelayed(runnable, 100);
        }
    };

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
    //重写onKeyDown事件，使得按返回键应用不销毁
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 判断歌曲是否已收藏
    public boolean if_favor() {
        boolean favor=false;
        if (musicService.getCurrentTitle()!=null) {
            favor= databaseManager.findData(favor_DBHelper,musicService.getCurrentTitle());
        }
        return favor;
    }


}
