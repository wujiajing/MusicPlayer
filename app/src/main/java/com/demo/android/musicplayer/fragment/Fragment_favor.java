package com.demo.android.musicplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.demo.android.musicplayer.Base.BaseFragment;
import com.demo.android.musicplayer.R;

public class Fragment_favor extends BaseFragment {

    @Override
    public View initView(LayoutInflater inflater) {
        //添加页面布局
        View view = inflater.inflate(R.layout.fragment_2, null);

        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {


    }

}
