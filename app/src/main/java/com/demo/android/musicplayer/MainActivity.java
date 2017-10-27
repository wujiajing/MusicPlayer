package com.demo.android.musicplayer;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.demo.android.musicplayer.Adapter.FragmentAdapter;
import com.demo.android.musicplayer.Adapter.MusicAdapter;
import com.demo.android.musicplayer.Base.BaseFragment;
import com.demo.android.musicplayer.Base.music;
import com.demo.android.musicplayer.Utils.MusicUtil;
import com.demo.android.musicplayer.fragment.Fragment_favor;
import com.demo.android.musicplayer.fragment.Fragment_local;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager viewPager;
    //数据源
    private List<BaseFragment> list;
    private List<music> mlist;

    private ImageView localBtn;
    private ImageView favorBtn;

    private ListView mListView;
    private MusicAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //添加数据
        list = new ArrayList<BaseFragment>();
        list.add(new Fragment_local());
        list.add(new Fragment_favor());
        initView();
    }

    /**
     * 初始化数据
     */
    @SuppressWarnings("deprecation")
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        localBtn = (ImageView) findViewById(R.id.musicbtn);
        favorBtn = (ImageView) findViewById(R.id.favorbtn);
        //绑定监听
        localBtn.setOnClickListener(this);
        favorBtn.setOnClickListener(this);
        //设置不进行预加载
        viewPager.setOffscreenPageLimit(0);
        //绑定适配器
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        //设置viewpager切换监听
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

    private void setTabSelection(int index) {
        //清除状态
        localBtn.setImageResource(R.drawable.actionbar_music);
        favorBtn.setImageResource(R.drawable.desk2_btn_love_prs);
        switch (index) {
            case 0:
                localBtn.setImageResource(R.drawable.actionbar_music_prs);
                showMusicList();
                break;
            case 1:
                favorBtn.setImageResource(R.drawable.btn_love_prs);
                //showFavorList();
                break;
        }

    }

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
        }
    }

    private void showMusicList(){
        mListView = (ListView) findViewById(R.id.listView1);
        mlist = new ArrayList<>();
        //把扫描到的音乐赋值给list
        mlist = MusicUtil.getMusicData(this);
        madapter = new MusicAdapter(this,mlist);
        mListView.setAdapter(madapter);

    }
}
