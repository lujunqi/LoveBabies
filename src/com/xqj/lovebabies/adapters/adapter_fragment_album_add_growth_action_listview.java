package com.xqj.lovebabies.adapters;

import java.util.Vector;

import com.xqj.lovebabies.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class adapter_fragment_album_add_growth_action_listview extends
		BaseAdapter {
	private Context context = null;
	private LayoutInflater inflater = null;
	private Vector<Integer> list = new Vector<Integer>(); 
	
	public adapter_fragment_album_add_growth_action_listview(Context context){
		this.context = context;
		inflater = LayoutInflater.from(context);
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
	
	public void addItem(Integer item){
		list.add(item);
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		Integer item = (Integer)getItem(position);
		ImageView viewItem = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.fragment_album_add_growth_action_item, parent, false);
			viewItem = (ImageView)convertView.findViewById(R.id.add_growth_action_imageview);
			convertView.setTag(viewItem);
		}else{
			viewItem = (ImageView)convertView.getTag();
		}
		viewItem.setImageResource(item);
		
		return convertView;
	}

}
