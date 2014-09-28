package com.xqj.lovebabies.adapters;

import java.util.*;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityMain;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_album_gridview_photo_path;
import com.xqj.lovebabies.structures.fragment_album_gridview_item;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.*;
import android.widget.*;

public class adapter_activity_add_growth_gridview extends BaseAdapter {
	private fragment_album_gridview_item item = null;
	private Vector<table_album_gridview_photo_path> pictures = null;
	private LayoutInflater inflater = null;
	private Context context = null;
	
	public adapter_activity_add_growth_gridview(Vector<table_album_gridview_photo_path> pictures, Context context){
		this.pictures = pictures;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pictures.size();
	}

	@Override
	public table_album_gridview_photo_path getItem(int position) {
		// TODO Auto-generated method stub
		return pictures.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void removeAll(){
		pictures.removeAllElements();
		this.notifyDataSetChanged();
	}
	
	public void addItem(table_album_gridview_photo_path photo_path){
		if(pictures==null){
			pictures = new Vector<table_album_gridview_photo_path>();
		}
		pictures.add(photo_path);
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = inflater.inflate(R.layout.fragment_add_growth_gridview_item, parent, false);
			item = new fragment_album_gridview_item();
			ImageView imageView = (ImageView)convertView.findViewById(R.id.fragment_album_gridview_item_pictrue);
			item.setAlbum_pictrue(imageView);
			
			convertView.setTag(item);
		}else{
			item = (fragment_album_gridview_item)convertView.getTag();
		}
		
		table_album_gridview_photo_path photo_path = getItem(position);
		if(photo_path.getImage_path()!=null && photo_path.getImage_path().length() > 0){
			// ×°ÔØ±¾µØÍ¼Æ¬
			Bitmap bm = utils_common_tools.getBitmapFromSDCard(photo_path.getImage_path());
			item.getAlbum_pictrue().setImageBitmap(bm);
		}else{
			item.getAlbum_pictrue().setImageResource(photo_path.getResource_id());
		}
		
		return convertView;
	}

}
