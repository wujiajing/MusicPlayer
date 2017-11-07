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
import com.demo.android.musicplayer.R;
import com.demo.android.musicplayer.Service.MusicService;
import com.demo.android.musicplayer.Utils.MusicUtil;
import java.util.ArrayList;



/**
 * Created by kk on 2017/10/27.
 */

public class Fragment_local extends BaseFragment {

    private ListView mListView;
    private MusicAdapter madapter;
    private ArrayList<music> mlist;
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

    /*List<music> getMusicList() {
        return MusicUtil.getMusicData(getActivity());
    }
*/
    @Override
    public void initData(Bundle savedInstanceState) {

        mlist = new ArrayList<>();
        mlist = MusicUtil.getMusicData(mainActivity);
        //把扫描到的音乐赋值给list
        /*if (musicService.getPlayList() != mlist) {
            musicService.setupPlayList(mlist);
        }
        if (musicService.getPlayList() == mlist) {
            Log.w("musicService", "PlayList == mList");
        }*/
        madapter = new MusicAdapter(mainActivity,mlist);
        mListView.setAdapter(madapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (musicService.getPlayList()!=mlist) {
                    musicService.setupPlayList(mlist);
                    Log.w("musicService", "PlayList == mlist");
                }
                musicService.setCurrentCursor(position);
                musicService.play_music(position);
                playBtn.setImageResource(R.drawable.notification_pause);
            }
        });
    }
}
