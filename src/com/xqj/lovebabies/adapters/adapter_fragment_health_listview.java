package com.xqj.lovebabies.adapters;

import java.util.Vector;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_health_information;
import com.xqj.lovebabies.structures.fragment_health_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class adapter_fragment_health_listview extends BaseAdapter {
	private fragment_health_item item = null;
	private table_health_information health_info = null;
	private LayoutInflater inflater = null;
	private Vector<table_health_information> list_health = null;
	private Context context = null;
	
	public adapter_fragment_health_listview(Context context){
		this.list_health = new Vector<table_health_information>();
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_health.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_health.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void addItemTail(table_health_information item){
		list_health.add(item);
		this.notifyDataSetChanged();
	}
	
	/**
	 * 清空所有元素
	 */
	public void removeAll(){
		list_health.removeAllElements();
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		health_info = (table_health_information)getItem(position);
		if(convertView==null){
			convertView = inflater.inflate(R.layout.fragment_health_item, parent, false);
			item = new fragment_health_item();
			TextView textview_health_title = (TextView)convertView.findViewById(R.id.health_title_textview);;
			ImageView imageview_health_pic = (ImageView)convertView.findViewById(R.id.health_pic_imageview);;
			
			item.setTextview_health_title(textview_health_title);
			item.setImageview_health_pic(imageview_health_pic);
			convertView.setTag(item);
		}else{
			item = (fragment_health_item)convertView.getTag();
		}
		
		item.getTextview_health_title().setText(health_info.getTitle());;
		
		if(health_info.getPic_name()!=null && health_info.getPic_name().length()>0){// 显示图片
			String imgurl = network_interface_paths.get_project_root+ health_info.getPic_name();
//			System.out.println("HealthIcon-->"+imgurl);
//			DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//			builder.showImageOnLoading(R.drawable.ic_launcher);
//			builder.showImageForEmptyUri(R.drawable.ic_launcher);
//			builder.cacheInMemory(false);
//			builder.cacheOnDisk(true);
//			builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//			DisplayImageOptions options = builder.build();
//			ImageLoader.getInstance().displayImage(imgurl, item.getImageview_health_pic(), options);
			utils_common_tools.f_display_Image(context, 
					item.getImageview_health_pic(), imgurl,R.drawable.ic_launcher,
					R.drawable.ic_launcher, ImageScaleType.IN_SAMPLE_INT);
		}else{
			item.getImageview_health_pic().setImageResource(R.drawable.ic_launcher);
		}
		return convertView;
	}
	
	
}
