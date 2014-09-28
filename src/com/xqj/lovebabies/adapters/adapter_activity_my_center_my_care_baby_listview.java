package com.xqj.lovebabies.adapters;

import java.util.*;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.databases.table_my_center_my_care_baby;
import com.xqj.lovebabies.structures.activity_my_center_my_care_baby_item;

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

public class adapter_activity_my_center_my_care_baby_listview extends BaseAdapter {
	private Vector<table_my_center_my_care_baby> list;
	private LayoutInflater inflater;
	private int right_width;
	
	public adapter_activity_my_center_my_care_baby_listview(Context context, int right_width){
		inflater = LayoutInflater.from(context);
		this.right_width = right_width;
		list = new Vector<table_my_center_my_care_baby>();
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

	public void addItem(table_my_center_my_care_baby item){
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
		activity_my_center_my_care_baby_item viewItem = null;
		table_my_center_my_care_baby careItem = (table_my_center_my_care_baby)getItem(position);
		
		if(convertView==null){
			convertView = inflater.inflate(R.layout.activity_my_center_my_care_baby_item, parent, false);
			viewItem = new activity_my_center_my_care_baby_item();
			
			TextView care_baby_name_textview = (TextView)convertView.findViewById(R.id.my_center_my_care_bay_name_textview);
			TextView care_add_date_textview = (TextView)convertView.findViewById(R.id.my_center_my_care_add_date_textview);
			TextView care_relations_textview = (TextView)convertView.findViewById(R.id.my_center_my_care_relations_textview);
			ImageView care_baby_icon_imageview = (ImageView)convertView.findViewById(R.id.my_center_my_care_item_icon_imageview);
			RelativeLayout care_baby_del_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_my_care_item_del_layout);
			RelativeLayout care_baby_left_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_my_care_item_left_layout);
			
			
			viewItem.setIcon_imageview(care_baby_icon_imageview);
			viewItem.setDel_layout(care_baby_del_layout);
			viewItem.setLeft_layout(care_baby_left_layout);
			viewItem.setAdd_date_textview(care_add_date_textview);
			viewItem.setBaby_name_textview(care_baby_name_textview);
			viewItem.setRelations_textview(care_relations_textview);
			
			convertView.setTag(viewItem);
		}else{
			viewItem = (activity_my_center_my_care_baby_item)convertView.getTag();
		}
		
		LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
		viewItem.getLeft_layout().setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(right_width, LayoutParams.MATCH_PARENT);
        viewItem.getDel_layout().setLayoutParams(lp2);
		
        viewItem.getIcon_imageview().setImageResource(careItem.getResource_id());
        viewItem.getBaby_name_textview().setText(careItem.getBaby_name());
        viewItem.getAdd_date_textview().setText(careItem.getBirthday());
        viewItem.getRelations_textview().setText(careItem.getRelations());
        
        viewItem.getDel_layout().setOnClickListener(new OnClickListener() {
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
