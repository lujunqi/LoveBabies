package com.xqj.lovebabies.adapters;

import java.util.List;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

public class adapter_activity_interaction_notice_editor_viewpager extends FragmentStatePagerAdapter {
	private List<Fragment> list_fragment = null;
	private List<String> list_title = null;

	public adapter_activity_interaction_notice_editor_viewpager(FragmentManager fm, List<Fragment> list_fragment, List<String> list_title) {
		// TODO Auto-generated constructor stub
		super(fm);
		this.list_fragment = list_fragment;
		this.list_title = list_title;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list_fragment.get(arg0);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		SpannableStringBuilder ssb = new SpannableStringBuilder(list_title.get(position));
		try {
			ForegroundColorSpan fcs = new ForegroundColorSpan(Color.WHITE);
			ssb.setSpan(fcs, 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			return ssb;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ssb;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_fragment.size();
	}

}
