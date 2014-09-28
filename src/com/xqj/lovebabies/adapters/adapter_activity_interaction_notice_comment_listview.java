package com.xqj.lovebabies.adapters;

import java.util.Vector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityViewGrowthInfo;
import com.xqj.lovebabies.databases.table_interaction_notice_comment;
import com.xqj.lovebabies.structures.activity_interaction_notice_detail_comment_item;

public class adapter_activity_interaction_notice_comment_listview extends BaseAdapter {
	private Vector<table_interaction_notice_comment> list = null;
	private Context context;
	private LayoutInflater inflater = null;
	private activity_interaction_notice_detail_comment_item notice_comment_view = null;
	private int current_index = 0;
	private Handler handler = null;
	private String permission = "";
	
	public adapter_activity_interaction_notice_comment_listview(Context context, Handler widge_show_change_handler, String permission){
		this.context = context;
		this.handler = widge_show_change_handler;
		inflater = LayoutInflater.from(context);
		list = new Vector<table_interaction_notice_comment>();
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
	
	public void addItem(table_interaction_notice_comment item){
		list.add(item);
		this.notifyDataSetChanged();
	}
	
	public void removeItem(int position){
		System.out.println("list.remove("+position+")");
		list.remove(position);
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
		notice_comment_view = new activity_interaction_notice_detail_comment_item();
		table_interaction_notice_comment comment_item = (table_interaction_notice_comment)getItem(position);
		if(convertView==null){
			convertView = inflater.inflate(R.layout.fragment_album_growth_comment_item, parent, false);
			TextView growth_comment_username_textview = (TextView)convertView.findViewById(R.id.growth_comment_username_textview);
			TextView growth_comment_content_textview = (TextView)convertView.findViewById(R.id.growth_comment_content_textview);
			TextView growth_comment_date_textview = (TextView)convertView.findViewById(R.id.growth_comment_date_textview);
			TextView growth_comment_delete_textview = (TextView)convertView.findViewById(R.id.growth_comment_delete_textview);
			notice_comment_view.setCmd_textview_comment_user_name(growth_comment_username_textview);
			notice_comment_view.setCmd_textview_comment_content(growth_comment_content_textview);
			notice_comment_view.setCmd_textview_comment_time(growth_comment_date_textview);
			notice_comment_view.setCmd_textview_comment_delete(growth_comment_delete_textview);
			notice_comment_view.getCmd_textview_comment_delete().setTag(String.valueOf(position));
			convertView.setTag(notice_comment_view);
		}else{
			notice_comment_view = (activity_interaction_notice_detail_comment_item)convertView.getTag();
		}
		notice_comment_view.getCmd_textview_comment_user_name().setText(comment_item.getComment_user_nike_name()+"：");
		notice_comment_view.getCmd_textview_comment_content().setText(comment_item.getComment_content());
		notice_comment_view.getCmd_textview_comment_time().setText(comment_item.getComment_time());
		
		String login_user_id = PreferencesUtils.getString(context, "user_id");
		if(permission==null || 
				(!permission.equals("1") && !login_user_id.equals(comment_item.getComment_user_id()))){
			notice_comment_view.getCmd_textview_comment_delete().setVisibility(View.GONE);
		}else{// 拥有删除权限
			notice_comment_view.getCmd_textview_comment_delete().setVisibility(View.VISIBLE);
		}
		notice_comment_view.getCmd_textview_comment_delete().setOnClickListener(new OnClickListener() {
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
