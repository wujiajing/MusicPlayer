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

import com.demo.android.musicplayer.MainActivity;
import com.demo.android.musicplayer.R;
import com.demo.android.musicplayer.Service.MusicService;

import static com.demo.android.musicplayer.MainActivity.musicList;


/**
 * Created by kk on 2017/10/27.
 */

public class Fragment_local extends BaseFragment {

    private ListView mListView;
    private MusicAdapter madapter;
    private ImageButton playBtn;
    View v;
    private MainActivity mainActivity;
    private MusicService musicService;

    @Override
    public void onAttach(Context context) {
        mainActivity = (MainActivity) context;
        musicService = mainActivity.getMusicService();
        super.onAttach(context);
        Log.w("local ", "OnATTACH");
    }

    @Override
    public View initView(LayoutInflater inflater) {
        v = inflater.inflate(R.layout.fragment_1, null);
        playBtn = (ImageButton) mainActivity.findViewById(R.id.btn_play);
        mListView = (ListView) v.findViewById(R.id.listView1);
        return v;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        madapter = new MusicAdapter(mainActivity,musicList);
        mListView.setAdapter(madapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (musicService.PlayList==null || musicService.PlayList!=musicList) {
                    musicService.PlayList = musicList;
                    Log.w("Fragment_local", "PlayList == musicList");
                }
                musicService.setCurrentCursor(position);
                musicService.play_music(position);
                playBtn.setImageResource(R.drawable.notification_pause);
            }
        });
    }
}
