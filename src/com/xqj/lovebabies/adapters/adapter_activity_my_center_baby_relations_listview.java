package com.xqj.lovebabies.adapters;

import java.util.*;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_my_center_baby_relations;
import com.xqj.lovebabies.structures.activity_my_center_baby_relations_item;

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

public class adapter_activity_my_center_baby_relations_listview extends BaseAdapter {
	private Vector<table_my_center_baby_relations> list;
	private LayoutInflater inflater;
	private int right_width;
	private Context context;
	
	public adapter_activity_my_center_baby_relations_listview(Context context, int right_width){
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.right_width = right_width;
		list = new Vector<table_my_center_baby_relations>();
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

	public void addItem(table_my_center_baby_relations item){
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
		activity_my_center_baby_relations_item viewItem = null;
		table_my_center_baby_relations careItem = (table_my_center_baby_relations)getItem(position);
		
		if(convertView==null){
			convertView = inflater.inflate(R.layout.activity_my_center_relations_item, parent, false);
			viewItem = new activity_my_center_baby_relations_item();
			
			TextView relation_name_textview = (TextView)convertView.findViewById(R.id.my_center_relations_name_textview);
			TextView baby_relations_textview = (TextView)convertView.findViewById(R.id.my_center_relations_textview);
			ImageView relation_icon_imageview = (ImageView)convertView.findViewById(R.id.my_center_relations_icon_imageview);
			RelativeLayout relation_right_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_relations_right_layout);
			RelativeLayout relation_left_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_relations_item_left_layout);
			
			viewItem.setRelation_name_textview(relation_name_textview);
			viewItem.setRelation_textview(baby_relations_textview);
			viewItem.setIcon_imageview(relation_icon_imageview);
			viewItem.setRight_layout(relation_right_layout);
			viewItem.setLeft_layout(relation_left_layout);
			
			convertView.setTag(viewItem);
		}else{
			viewItem = (activity_my_center_baby_relations_item)convertView.getTag();
		}
		
		LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
		viewItem.getLeft_layout().setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(right_width, LayoutParams.MATCH_PARENT);
        viewItem.getRight_layout().setLayoutParams(lp2);
		
//        viewItem.getIcon_imageview().setImageResource(careItem.getResource_id());
        // ×°ÔØÍøÂçÍ¼Æ¬
    	if(careItem.getUser_pic()!=null && careItem.getUser_pic().length()>0){
    			String imgurl = network_interface_paths.get_project_root + careItem.getUser_pic();
    			System.out.println("Relation--getUser_pic-->"+imgurl);
//    			DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//    			builder.showImageOnLoading(R.drawable.default_head_icon);
//    			builder.showImageForEmptyUri(R.drawable.default_head_icon);
//    			builder.cacheInMemory(false);
//    			builder.cacheOnDisk(true);
//    			builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//    			DisplayImageOptions options = builder.build();
//    			ImageLoader.getInstance().displayImage(imgurl, viewItem.getIcon_imageview(), options);
    			utils_common_tools.f_display_Image(context, 
    					viewItem.getIcon_imageview(), imgurl,R.drawable.default_head_icon,
						R.drawable.default_head_icon, ImageScaleType.IN_SAMPLE_INT);
    	}else{
    			viewItem.getIcon_imageview().setImageResource(R.drawable.default_head_icon);
    		}
        viewItem.getRelation_name_textview().setText(careItem.getUser_nick_name());
        viewItem.getRelation_textview().setText(careItem.getRelation());
        
        viewItem.getRight_layout().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener!=null){
					mListener.onDelItemClick(v, position);
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
    }
}
