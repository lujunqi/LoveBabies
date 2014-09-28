package com.xqj.lovebabies.adapters;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.structures.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class adapter_activity_chat_gridview_image_inside extends BaseAdapter {
	private int[] array_image_inside = null;
	private activity_chat_inside_image_item item = null;
	private Context context = null;

	public adapter_activity_chat_gridview_image_inside(Context context) {
		// --
		array_image_inside = global_contants.faceImgId;
		// --
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array_image_inside.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return array_image_inside[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.activity_interaction_chat_gridview_image_inside_item, null);
			item = new activity_chat_inside_image_item();
			item.setCmd_imageview_inside_image((ImageView) view.findViewById(R.id.activity_interaction_chat_gridview_item_image_inside));
			view.setTag(item);
			// --
			item.getCmd_imageview_inside_image().setBackgroundResource(array_image_inside[position]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return view;
	}

}
