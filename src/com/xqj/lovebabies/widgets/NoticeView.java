package com.xqj.lovebabies.widgets;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityInteractionNoticeDetail;
import com.xqj.lovebabies.adapters.adapter_fragment_interaction_notice_listview;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_interaction_notice;
import com.xqj.lovebabies.structures.interface_app_get_notice_list_req;
import com.xqj.lovebabies.structures.interface_app_get_notice_list_resp;
import com.xqj.lovebabies.threads.thread_interaction_get_notice_list;
import com.xqj.lovebabies.widgets.UIListView.OnRefreshListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 家园互动，公告栏目主页面
 * @author sunshine
 *
 */
public class NoticeView{
	
	private View cmd_view_container = null;
	private View bottom_view;
	private UIListView cmd_listview_container = null;
	private adapter_fragment_interaction_notice_listview notice_listview_adapter = null;
	private interface_app_get_notice_list_req req_notice = null;
	private interface_app_get_notice_list_resp resp_notice = null;
	private table_interaction_notice t_interaction_notice = null;
	private int notice_page_number = 0;
	private LayoutInflater inflater;
	
	private Context context;
	
	public NoticeView(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	public View getView(){
		if(cmd_view_container!=null){
			return cmd_view_container;
		}
		notice_listview_adapter = new adapter_fragment_interaction_notice_listview(context);
		cmd_view_container = inflater.inflate(R.layout.fragment_interaction_notice, null);
		cmd_listview_container = (UIListView) cmd_view_container.findViewById(R.id.fragment_interaction_notice_listview_container);
		cmd_listview_container.setCacheColorHint(Color.TRANSPARENT);
		cmd_listview_container.setAlwaysDrawnWithCacheEnabled(true); 
		try {
			// --
			bottom_view = (View)inflater.inflate(R.layout.bottom_view_more, null);
			bottom_view.setVisibility(View.INVISIBLE);
			cmd_listview_container.addFooterView(bottom_view);
			cmd_listview_container.setAdapter(notice_listview_adapter);
			cmd_listview_container.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					table_interaction_notice notice = (table_interaction_notice)notice_listview_adapter.getItem(position-1);
					System.out.println("notice_id-->"+notice.getNotice_id());
					Intent intent = new Intent();	
					Bundle bundle = new Bundle();
					bundle.putSerializable("notice", notice);
					intent.putExtras(bundle);
					intent.setClass(NoticeView.this.context, ActivityInteractionNoticeDetail.class);
					NoticeView.this.context.startActivity(intent);
				}
			});
			// --
			cmd_listview_container.setonRefreshListener(new OnRefreshListener() {// 下拉刷新
				@Override
				public void onRefresh() {
					new AsyncTask<Void, Void, Void>() {
						protected Void doInBackground(Void... params) {
							try {
								Message message = new Message();
								message.what = message_what_values.fragment_interaction_notice_on_drop_down;
								handler.sendMessage(message);
								Thread.sleep(1000);
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						}
						protected void onPostExecute(Void result) {
							notice_listview_adapter.notifyDataSetChanged();
							cmd_listview_container.onRefreshComplete();
						}
					}.execute(null);
					bottom_view.setVisibility(View.VISIBLE);
				}
			});
			bottom_view.setOnClickListener(new OnClickListener() {// 查看更多
				@Override
				public void onClick(View arg0) {
					Message message = new Message();
					message.what = message_what_values.fragment_interaction_notice_on_bottom_click;
					handler.sendMessage(message);
				}
			});
			
			f_action_get_first_page_notice();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return cmd_view_container;
	}
	
	private void f_action_get_first_page_notice() {
		try {
			notice_page_number = 1;
			get_list_notice_from_network();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void get_list_notice_from_network() {
		try {
			// 网络上返回指定页的数据
			String user_id = PreferencesUtils.getString(context, "user_id");
			notice_page_number = notice_page_number <= 0 ? 1 : notice_page_number;
			req_notice = new interface_app_get_notice_list_req();
			req_notice.setUser_id(Integer.parseInt(user_id));
			req_notice.setPage_number(notice_page_number);
			new thread_interaction_get_notice_list(context, handler, req_notice).start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void get_list_notice_from_network_complate(Message msg) {
		try {
			resp_notice = (interface_app_get_notice_list_resp) msg.obj;
			if (resp_notice.getNotices().size() > 0) {
				if(msg.arg1<=1){//查询最新数据，清除原有数据
					notice_listview_adapter.removeAll();
				}
				for (int i = 0; i < resp_notice.getNotices().size(); i++) {
					notice_listview_adapter.addItem(resp_notice.getNotices().get(i));
				}
				notice_page_number++;
			} else {
				Toast.makeText(context, "没有更多公告", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// --
			if (msg.what == message_what_values.fragment_interaction_notice_refesh_network_success) {
				get_list_notice_from_network_complate(msg);
			}
			// --
			if (msg.what == message_what_values.fragment_interaction_notice_on_bottom_click) {
				get_list_notice_from_network();
			}
			// --
			if (msg.what == message_what_values.fragment_interaction_notice_on_drop_down) {
				f_action_get_first_page_notice();
			}
		};
	};
	
}
