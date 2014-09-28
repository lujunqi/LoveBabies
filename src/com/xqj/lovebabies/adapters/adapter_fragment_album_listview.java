package com.xqj.lovebabies.adapters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityViewGrowthPhoto;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_density_transform;
import com.xqj.lovebabies.databases.table_album_baby_growth;
import com.xqj.lovebabies.databases.table_album_gridview_photo_path;
import com.xqj.lovebabies.structures.fragment_album_item;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class adapter_fragment_album_listview extends BaseAdapter {
	private static final int SINGLE_PIC_HEIGHT = 90;
	private static final int PIC_MARGIN = 2;
	private fragment_album_item item = null;
	private table_album_baby_growth album = null;
	private LayoutInflater inflater = null;
	private Vector<table_album_baby_growth> list_album = null;
	private Context context = null;
	
	public adapter_fragment_album_listview(Vector<table_album_baby_growth> list, Context context){
		this.list_album = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_album.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_album.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void addItemTail(table_album_baby_growth item){
		list_album.add(item);
		this.notifyDataSetChanged();
	}
	
	/**
	 * 清空所有元素
	 */
	public void removeAll(){
		list_album.removeAllElements();
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		album = (table_album_baby_growth)getItem(position);
		if(convertView==null){
			convertView = inflater.inflate(R.layout.fragment_album_item, parent, false);
			item = new fragment_album_item();
			TextView textview_album_title = (TextView)convertView.findViewById(R.id.fragment_album_textview_title);;
			GridView gridview_album_photos = (GridView)convertView.findViewById(R.id.fragment_album_gridview_photos);;
			TextView textview_album_date = (TextView)convertView.findViewById(R.id.fragment_album_textview_date);;
			TextView textview_album_age = (TextView)convertView.findViewById(R.id.fragment_album_textview_age);;
			TextView textview_growth_height = (TextView)convertView.findViewById(R.id.album_growth_height_textview);
			TextView textview_growth_weight = (TextView)convertView.findViewById(R.id.album_growth_weight_textview);
			TextView textview_growth_location = (TextView)convertView.findViewById(R.id.album_growth_location_textview);
			LinearLayout linearlayout_growth_location = (LinearLayout)convertView.findViewById(R.id.album_growth_location_layout);
			LinearLayout linearlayout_growth_weight_height = (LinearLayout)convertView.findViewById(R.id.growth_weight_height_layout);
			LinearLayout linearlayout_album_comment = (LinearLayout)convertView.findViewById(R.id.fragment_album_layout_comment);;
			LinearLayout linearlayout_album_zan = (LinearLayout)convertView.findViewById(R.id.fragment_album_layout_zan);;
			TextView textview_album_comment = (TextView)convertView.findViewById(R.id.fragment_album_textview_comment);;
			TextView textview_album_zan = (TextView)convertView.findViewById(R.id.fragment_album_textview_zan);;
			ImageView imageview_album_comment = (ImageView)convertView.findViewById(R.id.fragment_album_imageview_comment);;
			ImageView imageview_album_zan = (ImageView)convertView.findViewById(R.id.fragment_album_imageview_zan);;
			LinearLayout linear_layout_top_space = (LinearLayout)convertView.findViewById(R.id.fragment_album_layout_top_space);
			
			item.setTextview_album_title(textview_album_title);
			item.setGridview_album_photos(gridview_album_photos);
			item.setTextview_album_date(textview_album_date);
			item.setTextview_album_age(textview_album_age);
			item.setLinearlayout_album_comment(linearlayout_album_comment);
			item.setLinearlayout_album_zan(linearlayout_album_zan);
			item.setTextview_album_comment(textview_album_comment);
			item.setTextview_album_zan(textview_album_zan);
			item.setImageview_album_comment(imageview_album_comment);
			item.setImageview_album_zan(imageview_album_zan);
			item.setLinearlayout_album_top_space(linear_layout_top_space);
			item.setTextview_baby_height(textview_growth_height);
			item.setTextview_baby_weight(textview_growth_weight);
			item.setTextview_growth_location(textview_growth_location);
			item.setLinearlayout_growth_location(linearlayout_growth_location);
			item.setLinearlayout_growth_weight_height(linearlayout_growth_weight_height);
			convertView.setTag(item);
		}else{
			item = (fragment_album_item)convertView.getTag();
		}
		
		item.getTextview_album_title().setText(album.getWord_record());
		item.getTextview_album_age().setText(album.getAge_true());
		item.getTextview_album_date().setText(album.getRecord_time());
		item.getTextview_growth_location().setText(album.getLocations());
		item.getTextview_baby_height().setText(album.getHeight());
		item.getTextview_baby_weight().setText(album.getWeight());
		item.getTextview_album_comment().setText("评论");
		item.getTextview_album_zan().setText("点赞");
		
		if(album.getLocations()!=null && album.getLocations().length()>0){
			item.getLinearlayout_growth_location().setVisibility(View.VISIBLE);
		}else{// 如果没有地址信息，则不显示地址控件
			item.getLinearlayout_growth_location().setVisibility(View.GONE);
		}
		if(album.getHeight()==null || album.getWeight()==null
				|| album.getHeight().equals("") || album.getWeight().equals("")
				|| album.getHeight().equals("0") || album.getWeight().equals("0")){
			// 如果没有身高  体重信息，隐藏身高体重栏
			item.getLinearlayout_growth_weight_height().setVisibility(View.GONE);
		}else{
			item.getLinearlayout_growth_weight_height().setVisibility(View.VISIBLE);
		}
		
		
		if(album.getPic_list()!=null){	// 显示图片
			System.out.println("显示图片");
			int pic_num = album.getPic_list().size();
			ViewGroup.LayoutParams params = item.getGridview_album_photos().getLayoutParams();
			int gridHeight = utils_density_transform.dip2px(context, getGridViewHeight(pic_num));
			params.height = gridHeight;
			item.getGridview_album_photos().setLayoutParams(params);
			adapter_fragment_album_gridview gridview_adapter = new adapter_fragment_album_gridview(album.getPic_list(), context);
			item.getGridview_album_photos().setAdapter(gridview_adapter);
			item.getGridview_album_photos().setVisibility(View.VISIBLE);
			item.getGridview_album_photos().setOnItemClickListener(gridviewItemClickListener);
			item.getGridview_album_photos().setTag(String.valueOf(position));
		}else{
			item.getGridview_album_photos().setVisibility(View.GONE);
		}
		
		if(position == 0){
			item.getLinearlayout_album_top_space().setVisibility(View.VISIBLE);
		}else{
			item.getLinearlayout_album_top_space().setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	private OnItemClickListener gridviewItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parentView, View arg1, int photoIndex,
				long arg3) {
			// TODO Auto-generated method stub
			try{
				int parentIndex = Integer.parseInt(parentView.getTag().toString());
				
				table_album_baby_growth growth_item = list_album.get(parentIndex);
				Vector<table_album_gridview_photo_path> pic_list = growth_item.getPic_list();
				List<String> pic_path_list = new ArrayList<String>();
				
				if(pic_list!=null){
					for(int i=0;i<pic_list.size();i++){
						pic_path_list.add(pic_list.get(i).getImage_path());
					}
				}
				
				
				Intent intent = new Intent();
				intent.setClass(context, ActivityViewGrowthPhoto.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("pic_list", (Serializable)pic_path_list);
				intent.putExtras(bundle);
				intent.putExtra("photo_index", photoIndex);
				context.startActivity(intent);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	};

	/**
	 * 获取相册GridView的高度
	 * @param num
	 * @return
	 */
	public static int getGridViewHeight(int num){
		if(num>0 && num<=3){
			return SINGLE_PIC_HEIGHT+(PIC_MARGIN*2);
		}else if(num>3 && num<=6){
			return (SINGLE_PIC_HEIGHT*2)+(PIC_MARGIN*3);
		}else if(num>6){
			return (SINGLE_PIC_HEIGHT*3)+(PIC_MARGIN*4);
		}else{
			return 0;			
		}
	}
	
}
