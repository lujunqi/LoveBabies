package com.xqj.lovebabies.adapters;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class adapter_activity_description_viewpager extends PagerAdapter {
	private ArrayList<View> list_view = null;

	public adapter_activity_description_viewpager(ArrayList<View> list_view) {
		// TODO Auto-generated constructor stub
		this.list_view = list_view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_view.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		try {
			ImageView imview = (ImageView) list_view.get(position);
			imview.setScaleType(ScaleType.FIT_XY);
			((ViewPager) container).addView(imview, 0);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return (View) list_view.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView(list_view.get(position));
	}

}
