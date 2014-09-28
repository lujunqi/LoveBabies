package com.xqj.lovebabies.adapters;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class adapter_fragment_interaction_viewpager extends FragmentStatePagerAdapter {
	private List<Fragment> list_fragment = null;

	public adapter_fragment_interaction_viewpager(FragmentManager fm, List<Fragment> list_fragment) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.list_fragment = list_fragment;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list_fragment.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_fragment.size();
	}

}
