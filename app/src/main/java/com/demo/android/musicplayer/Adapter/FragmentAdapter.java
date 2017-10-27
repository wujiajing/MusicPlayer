package com.demo.android.musicplayer.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.demo.android.musicplayer.Base.BaseFragment;

import java.util.List;


public class FragmentAdapter extends FragmentPagerAdapter {
	private List<BaseFragment> list;

	public FragmentAdapter(FragmentManager fm, List<BaseFragment> list) {
		super(fm);
		this.list = list;
	}

	// 得到数据
	@Override
	public Fragment getItem(int position) {

		return list.get(position);
	}

	@Override
	public int getCount() {
		
		return list.size();
	}

}
