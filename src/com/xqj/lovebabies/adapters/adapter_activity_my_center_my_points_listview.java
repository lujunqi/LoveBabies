package com.xqj.lovebabies.adapters;

import java.util.*;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.databases.table_my_center_my_points;
import com.xqj.lovebabies.structures.activity_my_center_my_points_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class adapter_activity_my_center_my_points_listview extends BaseAdapter {
	private Vector<table_my_center_my_points> list;
	private LayoutInflater inflater;
	
	public adapter_activity_my_center_my_points_listview(Context context){
		inflater = LayoutInflater.from(context);
		list = new Vector<table_my_center_my_points>();
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

	public void addItem(table_my_center_my_points item){
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
		activity_my_center_my_points_item viewItem = null;
		table_my_center_my_points pointItem = (table_my_center_my_points)getItem(position);
		
		if(convertView==null){//
			viewItem = new activity_my_center_my_points_item();
			convertView = inflater.inflate(R.layout.activity_my_center_points_history_item, parent, false);
			TextView event_textview = (TextView)convertView.findViewById(R.id.my_center_points_history_tablerow_event);
			TextView points_textview = (TextView)convertView.findViewById(R.id.my_center_points_history_tablerow_points);
			TextView time_textview = (TextView)convertView.findViewById(R.id.my_center_points_history_tablerow_time);
			TextView remark_textview = (TextView)convertView.findViewById(R.id.my_center_points_history_tablerow_remark);
			viewItem.setEvent_textview(event_textview);
			viewItem.setPoints_textview(points_textview);
			viewItem.setRemark_textview(remark_textview);
			viewItem.setTime_textview(time_textview);
			System.out.println("viewItem.setEvent_textview(event_textview);");
			System.out.println("viewItem.setPoints_textview(points_textview);");
			System.out.println("viewItem.setRemark_textview(remark_textview);");
			convertView.setTag(viewItem);
		}else{
			viewItem = (activity_my_center_my_points_item)convertView.getTag();
		}
		System.out.println("viewItem--->"+viewItem);
		System.out.println("pointItem--->"+pointItem);
		viewItem.getEvent_textview().setText(pointItem.getConsume_style());
		viewItem.getPoints_textview().setText("+"+pointItem.getIntegral_count());
		viewItem.getTime_textview().setText(pointItem.getTime());
		viewItem.getRemark_textview().setText(pointItem.getRemark());
		return convertView;
	}
}
