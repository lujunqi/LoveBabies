package com.xqj.lovebabies.adapters;

import java.util.*;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.databases.table_album_my_baby;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class adapter_fragment_album_popwindow extends BaseAdapter {
	private Vector<table_album_my_baby> data_list = null;
	private Context context;
	private LayoutInflater inflater = null;
	
	public adapter_fragment_album_popwindow(Vector<table_album_my_baby> list, Context context){
		this.data_list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * Ìí¼ÓÔªËØ
	 */
	public void addItem(table_album_my_baby baby){
		data_list.add(baby);
		this.notifyDataSetChanged();
	}
	
	/**
	 * ¸ù¾Ý±¦±¦ID²éÑ¯±¦±¦
	 * @param baby_id
	 * @return
	 */
	public table_album_my_baby getItemById(String baby_id){
		if(data_list!=null && data_list.size()>0){
			for(int i=0; i<data_list.size(); i++){
				table_album_my_baby baby = data_list.get(i);
				if(baby_id!=null && baby_id.equals(baby.getBaby_id())){
					return baby;
				}
			}
		}
		return null;
	}
	
	/**
	 * Çå³ýËùÓÐÔªËØ
	 */
	public void removeAll(){
		if(data_list!=null && data_list.size()>0){
			for(int i=0; i<data_list.size(); i++){
				data_list.remove(i);
			}
		}
		this.notifyDataSetChanged();
	}
	
	public void removeItem(String baby_id){
		if(data_list!=null && data_list.size()>0){
			for(int i=0; i<data_list.size(); i++){
				table_album_my_baby baby = (table_album_my_baby)data_list.get(i);
				if(baby_id!=null && baby_id.equals(baby.getBaby_id())){
					data_list.remove(i);
				}
			}
		}
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		TextView babyName = null;
		table_album_my_baby babyItem = data_list.get(position);
		String nameStr = babyItem.getBaby_name();
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.fragment_album_pop_window_item, parent, false);
			babyName = (TextView)convertView.findViewById(R.id.fragment_album_pop_window_item_baby_name);
			convertView.setTag(babyName);
		}else{
			babyName = (TextView) convertView.getTag();
		}
//		babyName.setText(nameStr);
//		if(position == 0){
//			babyName.setText(nameStr);
//			babyName.setBackgroundResource(R.drawable.album_pop_window_top);
//		}else if(position == (data_list.size()-1)){// ÊäÈëÑûÇëÂë
//			babyName.setBackgroundResource(R.drawable.album_pop_window_switch03);
//		}else if(position == (data_list.size()-2)){// ÑûÇëÇ×ÓÑ
//			babyName.setBackgroundResource(R.drawable.album_pop_window_switch02);
//		}else if(position == (data_list.size()-3)){// Ìí¼Ó±¦±¦
//			babyName.setBackgroundResource(R.drawable.album_pop_window_switch01);
//		}else{
//			babyName.setText(nameStr);
//			babyName.setBackgroundResource(R.drawable.album_pop_window_middle);
//		}
		if(position == (data_list.size()-1)){
			babyName.setText(nameStr);
			babyName.setBackgroundResource(R.drawable.album_pop_window_switch03);
		}else{
			babyName.setText(nameStr);
			babyName.setBackgroundResource(R.drawable.album_pop_window_middle);
		}
		
		return convertView;
	}

}
