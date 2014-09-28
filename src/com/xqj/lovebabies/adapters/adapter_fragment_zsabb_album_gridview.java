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
import android.view.*;
import android.widget.*;

public class adapter_fragment_zsabb_album_gridview extends BaseAdapter {
	private fragment_album_gridview_item item = null;
	private Vector<table_album_gridview_photo_path> pictures = new Vector<table_album_gridview_photo_path>();
	private LayoutInflater inflater = null;
	private Context context = null;
	
	public adapter_fragment_zsabb_album_gridview(Context context){
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
	
	public void addItem(table_album_gridview_photo_path photo_path){
		pictures.add(photo_path);
		this.notifyDataSetChanged();
	}

	public void removeAll(){
		pictures.removeAllElements();
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = inflater.inflate(R.layout.fragment_zsabb_album_item, parent, false);
			item = new fragment_album_gridview_item();
			ImageView imageView = (ImageView)convertView.findViewById(R.id.fragment_album_gridview_item_pictrue);
			item.setAlbum_pictrue(imageView);
			
			convertView.setTag(item);
		}else{
			item = (fragment_album_gridview_item)convertView.getTag();
		}
		
		table_album_gridview_photo_path photo_path = getItem(position);
		if(photo_path.getResource_id() > 0){
			item.getAlbum_pictrue().setImageResource(photo_path.getResource_id());
		}
		else{
			// ×°ÔØÍøÂçÍ¼Æ¬
			if(photo_path.getImage_path()!=null && photo_path.getImage_path().length()>0){
				String imgurl = network_interface_paths.get_project_root+ "img/1/" + photo_path.getImage_path();
//				System.out.println("GridViewIcons-->"+imgurl);
//				DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//				builder.showImageOnLoading(R.drawable.ic_launcher);
//				builder.showImageForEmptyUri(R.drawable.ic_launcher);
//				builder.cacheInMemory(false);
//				builder.cacheOnDisk(true);
//				builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
////				builder.displayer(new RoundedBitmapDisplayer(20));
//				DisplayImageOptions options = builder.build();
//				ImageLoader.getInstance().displayImage(imgurl, item.getAlbum_pictrue(), options);
				utils_common_tools.f_display_Image(context, 
						item.getAlbum_pictrue(), imgurl,R.drawable.ic_launcher,
						R.drawable.ic_launcher, ImageScaleType.IN_SAMPLE_INT);
			}else{
				item.getAlbum_pictrue().setImageResource(R.drawable.ic_launcher);
			}
		}
		
		return convertView;
	}

}
