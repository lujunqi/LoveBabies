package com.xqj.lovebabies.adapters;

import java.util.*;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.databases.table_my_center_record_rules;
import com.xqj.lovebabies.structures.activity_my_center_my_point_rules_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class adapter_activity_my_center_my_point_rules_listview extends BaseAdapter {
	private Vector<table_my_center_record_rules> list;
	private LayoutInflater inflater;
	private Context context;
	
	public adapter_activity_my_center_my_point_rules_listview(Context context){
		inflater = LayoutInflater.from(context);
		list = new Vector<table_my_center_record_rules>();
		this.context = context;
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

	public void addItem(table_my_center_record_rules item){
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
		activity_my_center_my_point_rules_item viewItem = null;
		table_my_center_record_rules point_rules_Item = (table_my_center_record_rules)getItem(position);
		
		if(convertView==null){//
			viewItem = new activity_my_center_my_point_rules_item();
				convertView = inflater.inflate(R.layout.activity_my_center_points_rules_item, parent, false);
				TextView reason_textview = (TextView)convertView.findViewById(R.id.my_center_point_rule_reason_textview);
				TextView points_textview = (TextView)convertView.findViewById(R.id.my_center_point_rule_points_textview);
				TextView limit_textview = (TextView)convertView.findViewById(R.id.my_center_point_rule_limit_textview);
				TextView remark_textview = (TextView)convertView.findViewById(R.id.my_center_point_rule_remark_textview);
				RelativeLayout reason_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_point_rule_reason_layout);
				RelativeLayout points_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_point_rule_points_layout);
				RelativeLayout limit_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_point_rule_limit_layout);
				RelativeLayout remark_layout = (RelativeLayout)convertView.findViewById(R.id.my_center_point_rule_remark_layout);
				viewItem.setReason_textview(reason_textview);
				viewItem.setPoints_textview(points_textview);
				viewItem.setRemark_textview(remark_textview);
				viewItem.setLimit_textview(limit_textview);
				viewItem.setReason_layout(reason_layout);
				viewItem.setPoints_layout(points_layout);
				viewItem.setRemark_layout(remark_layout);
				viewItem.setLimit_layout(limit_layout);
			convertView.setTag(viewItem);
		}else{
			viewItem = (activity_my_center_my_point_rules_item)convertView.getTag();
		}
		viewItem.getReason_textview().setText(point_rules_Item.getIntegral_reason());
		viewItem.getPoints_textview().setText("+"+point_rules_Item.getIntegral_count());
		viewItem.getLimit_textview().setText(point_rules_Item.getCount_limit());
		viewItem.getRemark_textview().setText(point_rules_Item.getRemark());
		if(position%2==1){
			viewItem.getReason_layout().setBackgroundColor(context.getResources().getColor(R.color.gray_bg));
			viewItem.getPoints_layout().setBackgroundColor(context.getResources().getColor(R.color.gray_bg));
			viewItem.getLimit_layout().setBackgroundColor(context.getResources().getColor(R.color.gray_bg));
			viewItem.getRemark_layout().setBackgroundColor(context.getResources().getColor(R.color.gray_bg));
		}else{
			viewItem.getReason_layout().setBackgroundColor(context.getResources().getColor(R.color.green_bg));
			viewItem.getPoints_layout().setBackgroundColor(context.getResources().getColor(R.color.green_bg));
			viewItem.getLimit_layout().setBackgroundColor(context.getResources().getColor(R.color.green_bg));
			viewItem.getRemark_layout().setBackgroundColor(context.getResources().getColor(R.color.green_bg));
		}
		return convertView;
	}
}
