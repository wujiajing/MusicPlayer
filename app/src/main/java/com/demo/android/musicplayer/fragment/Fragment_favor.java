package com.demo.android.musicplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.demo.android.musicplayer.Adapter.MusicAdapter;
import com.demo.android.musicplayer.Base.BaseFragment;
import com.demo.android.musicplayer.Base.music;
import com.demo.android.musicplayer.MainActivity;
import com.demo.android.musicplayer.MySQL.DatabaseManager;
import com.demo.android.musicplayer.MySQL.MyDBHelper;
import com.demo.android.musicplayer.R;
import com.demo.android.musicplayer.Service.MusicService;

import java.util.ArrayList;



/**
 * Created by kk on 2017/10/27.
 */

public class Fragment_favor extends BaseFragment {

    private MainActivity mainActivity;
    private MusicService musicService;
    private ListView fListView;
    private MusicAdapter fadapter;
    private ArrayList<music> flist;
    private ImageButton playBtn;
    private DatabaseManager databaseManager;
    private MyDBHelper favor_DBHelper;


    @Override
    public void onAttach(Context context) {
        mainActivity = (MainActivity)context;
        musicService = mainActivity.getMusicService();
        favor_DBHelper=new MyDBHelper(mainActivity);
        databaseManager=new DatabaseManager();
        super.onAttach(context);
        Log.w("favor ","OnATTACH");
    }

    @Override
    public View initView(LayoutInflater inflater) {
        //添加页面布局
        View view = inflater.inflate(R.layout.fragment_2, null);
        playBtn = (ImageButton) mainActivity.findViewById(R.id.btn_play);
        fListView = (ListView) view.findViewById(R.id.listView2);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        flist = new ArrayList<>();
        //获取数据库内容到list
        flist = databaseManager.getFavorMusicData(favor_DBHelper);
        fadapter = new MusicAdapter(mainActivity,flist);
        fListView.setAdapter(fadapter);
        fListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (musicService.getPlayList()!=flist) {
                    musicService.setupPlayList(flist);
                    Log.w("musicService","PlayList == fList");
                }
                musicService.setCurrentCursor(position);
                musicService.play_music(position);
                playBtn.setImageResource(R.drawable.notification_pause);
            }
        });
    }
}
