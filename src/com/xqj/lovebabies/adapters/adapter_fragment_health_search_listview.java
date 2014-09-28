package com.xqj.lovebabies.adapters;

import java.util.Vector;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.databases.table_health_information;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class adapter_fragment_health_search_listview extends BaseAdapter {
	private TextView condition_textview = null;
	private table_health_information health_info = null;
	private LayoutInflater inflater = null;
	private Vector<table_health_information> list_health = null;
	private Context context = null;
	
	public adapter_fragment_health_search_listview(Context context){
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
			convertView = inflater.inflate(R.layout.fragment_health_search_item, null);
			condition_textview = (TextView)convertView.findViewById(R.id.health_search_item_title_textview);;
			convertView.setTag(condition_textview);
		}else{
			condition_textview = (TextView)convertView.getTag();
		}
		condition_textview.setText(health_info.getTitle());;
		return convertView;
	}
	
	
}
