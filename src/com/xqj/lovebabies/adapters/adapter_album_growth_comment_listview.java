package com.xqj.lovebabies.adapters;

import java.util.List;
import java.util.Vector;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityViewGrowthInfo;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.databases.table_album_growth_comment;
import com.xqj.lovebabies.databases.table_album_growth_praise;
import com.xqj.lovebabies.structures.fragment_album_growth_comment_item;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class adapter_album_growth_comment_listview extends BaseAdapter {
	private Vector<table_album_growth_comment> list = null;
	private Context context;
	private LayoutInflater inflater = null;
	private fragment_album_growth_comment_item growth_comment_view = null;
	private int current_index = 0;
	private Handler handler = null;
	private String permission = "";
	
	public adapter_album_growth_comment_listview(Context context, Handler widge_show_change_handler, String permission){
		this.context = context;
		this.handler = widge_show_change_handler;
		inflater = LayoutInflater.from(context);
		list = new Vector<table_album_growth_comment>();
		this.permission = permission;
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
	
	public void addItem(table_album_growth_comment item){
		list.add(item);
		this.notifyDataSetChanged();
	}
	
	public void removeItem(int position){
		System.out.println("list.remove("+position+")");
		list.remove(position);
		System.out.println(list.toArray());
		this.notifyDataSetChanged();
	}
	
	public void removeAll(){
		list.removeAllElements();
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		current_index = position;
		growth_comment_view = new fragment_album_growth_comment_item();
		table_album_growth_comment comment_item = (table_album_growth_comment)getItem(position);
		if(convertView==null){
			convertView = inflater.inflate(R.layout.fragment_album_growth_comment_item, parent, false);
			TextView growth_comment_username_textview = (TextView)convertView.findViewById(R.id.growth_comment_username_textview);
			TextView growth_comment_content_textview = (TextView)convertView.findViewById(R.id.growth_comment_content_textview);
			TextView growth_comment_date_textview = (TextView)convertView.findViewById(R.id.growth_comment_date_textview);
			TextView growth_comment_delete_textview = (TextView)convertView.findViewById(R.id.growth_comment_delete_textview);
			growth_comment_view.setGrowth_comment_username_textview(growth_comment_username_textview);
			growth_comment_view.setGrowth_comment_content_textview(growth_comment_content_textview);
			growth_comment_view.setGrowth_comment_date_textview(growth_comment_date_textview);
			growth_comment_view.setGrowth_comment_delete_textview(growth_comment_delete_textview);
			growth_comment_view.getGrowth_comment_delete_textview().setTag(String.valueOf(position));
			convertView.setTag(growth_comment_view);
		}else{
			growth_comment_view = (fragment_album_growth_comment_item)convertView.getTag();
		}
		growth_comment_view.getGrowth_comment_username_textview().setText(comment_item.getComment_nick_name()+"：");
		growth_comment_view.getGrowth_comment_content_textview().setText(comment_item.getComm_content());
		growth_comment_view.getGrowth_comment_date_textview().setText(comment_item.getComm_time());
		
		String login_user_id = PreferencesUtils.getString(context, "user_id");
		if(permission==null || 
				(!permission.equals("1") && !login_user_id.equals(comment_item.getUser_id()))){
			growth_comment_view.getGrowth_comment_delete_textview().setVisibility(View.GONE);
		}else{// 拥有删除权限
			growth_comment_view.getGrowth_comment_delete_textview().setVisibility(View.VISIBLE);
		}
		growth_comment_view.getGrowth_comment_delete_textview().setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			try{
				current_index = Integer.parseInt((String)v.getTag());
			}catch(Exception ex){
				current_index = -1;
			}
			// 删除评论
			Dialog alertDialog = new AlertDialog.Builder(context).
				    setTitle("确定删除？").
//				    setIcon(R.drawable.ic_launcher).
				    setPositiveButton("确定", positive_button_onclick_listener).
				    setNegativeButton("取消", negtive_button_onclick_listener).
				    create();
				  alertDialog.show();
			}
		});
		return convertView;
	}

	/**
	 * 确定按钮监听
	 */
	private DialogInterface.OnClickListener positive_button_onclick_listener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Message msg = new Message();
			msg.what = ActivityViewGrowthInfo.DELETE_GROWTH_COMMENT;
			msg.arg1 = current_index;
			handler.sendMessage(msg);
			current_index = -1;
		}
	};
	
	/**
	 * 取消按钮监听
	 */
	private DialogInterface.OnClickListener negtive_button_onclick_listener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	};
}
