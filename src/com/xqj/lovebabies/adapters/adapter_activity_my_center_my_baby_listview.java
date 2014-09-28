package com.xqj.lovebabies.adapters;

import java.util.*;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_album_my_baby;
import com.xqj.lovebabies.structures.activity_my_center_my_baby_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class adapter_activity_my_center_my_baby_listview extends BaseAdapter {
	private Vector<table_album_my_baby> list;
	private LayoutInflater inflater;
	private int right_width;
	private Context context;
	
	public adapter_activity_my_center_my_baby_listview(Context context, int right_width){
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.right_width = right_width;
		list = new Vector<table_album_my_baby>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addItem(table_album_my_baby item){
		list.add(item);
		this.notifyDataSetChanged();
	}
	
	public void removeItem(int position){
		list.remove(position);
		this.notifyDataSetChanged();
	}
	
	public void removeAll(){
		list.removeAllElements();
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		activity_my_center_my_baby_item viewItem = null;
		table_album_my_baby careItem = (table_album_my_baby)getItem(position);
		
		if(convertView==null){
			convertView = inflater.inflate(R.layout.activity_my_center_my_baby_item, parent, false);
			viewItem = new activity_my_center_my_baby_item();
			
			TextView baby_name_textview = (TextView)convertView.findViewById(R.id.my_center_my_baby_name_textview);
			TextView baby_add_date_textview = (TextView)convertView.findViewById(R.id.my_center_my_baby_add_date_textview);
			TextView baby_relations_textview = (TextView)convertView.findViewById(R.id.my_center_my_baby_relations_textview);
			ImageView baby_icon_imageview = (ImageView)convertView.findViewById(R.id.my_center_my_baby_icon_imageview);
			RelativeLayout baby_right_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_my_baby_right_layout);
			RelativeLayout baby_left_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_my_baby_item_left_layout);
			RelativeLayout baby_modi_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_my_baby_modi_layout);
			RelativeLayout baby_del_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_my_baby_del_layout);
			
			
			viewItem.setIcon_imageview(baby_icon_imageview);
			viewItem.setDel_layout(baby_del_layout);
			viewItem.setLeft_layout(baby_left_layout);
			viewItem.setRight_layout(baby_right_layout);
			viewItem.setModi_layout(baby_modi_layout);
			viewItem.setAdd_date_textview(baby_add_date_textview);
			viewItem.setBaby_name_textview(baby_name_textview);
			viewItem.setGet_relations_textview(baby_relations_textview);
			
			convertView.setTag(viewItem);
		}else{
			viewItem = (activity_my_center_my_baby_item)convertView.getTag();
		}
		
		LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
		viewItem.getLeft_layout().setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(right_width, LayoutParams.MATCH_PARENT);
        viewItem.getRight_layout().setLayoutParams(lp2);
		
     // ×°ÔØÍøÂçÍ¼Æ¬
	if(careItem.getBaby_pic()!=null && careItem.getBaby_pic().length()>0){
			String imgurl = network_interface_paths.get_project_root + careItem.getBaby_pic();
			System.out.println("getBabyPic-->"+imgurl);
//			DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//			builder.showImageOnLoading(R.drawable.ic_launcher);
//			builder.showImageForEmptyUri(R.drawable.ic_launcher);
//			builder.cacheInMemory(false);
//			builder.cacheOnDisk(true);
//			builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//			DisplayImageOptions options = builder.build();
//			ImageLoader.getInstance().displayImage(imgurl, viewItem.getIcon_imageview(), options);
			utils_common_tools.f_display_Image(context, 
					viewItem.getIcon_imageview(), imgurl,R.drawable.ic_launcher,
					R.drawable.ic_launcher, ImageScaleType.IN_SAMPLE_INT);
		}else{
			viewItem.getIcon_imageview().setImageResource(R.drawable.ic_launcher);
		}
        viewItem.getBaby_name_textview().setText(careItem.getBaby_name());
        viewItem.getAdd_date_textview().setText(careItem.getBirthday());
        viewItem.getGet_relations_textview().setText("¿ìÈ¥ÑûÇëÇ×ÓÑ³ÉÎª"+careItem.getBaby_name()+"µÄ·ÛË¿ÍÅ°É~");
        
        viewItem.getDel_layout().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener!=null){
					mListener.onDelItemClick(v, position);
				}
			}
		});
        viewItem.getModi_layout().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener!=null){
					mListener.onModiItemClick(v, position);
				}
			}
		});
        
		return convertView;
	}

	/**
     * É¾³ý°´Å¥ µ¥»÷ÊÂ¼þ¼àÌýÆ÷
     */
    private onDelItemClickListener mListener = null;
    
    public void setOnDelItemClickListener(onDelItemClickListener listener){
    	mListener = listener;
    }

    public interface onDelItemClickListener {
        void onDelItemClick(View v, int position);
        void onModiItemClick(View v, int position);
    }
}
