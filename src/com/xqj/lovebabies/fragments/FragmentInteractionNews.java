package com.xqj.lovebabies.fragments;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.common.util.*;
import cn.trinea.android.common.view.*;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_fragment_interaction_news_listview;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_interaction_news;
import com.xqj.lovebabies.listeners.listener_fragment_interaction_news_listview_on_bottom;
import com.xqj.lovebabies.listeners.listener_fragment_interaction_news_listview_on_drop_down;
import com.xqj.lovebabies.listeners.listener_fragment_interaction_news_listview_on_item_click;
import com.xqj.lovebabies.structures.interface_app_get_news_req;
import com.xqj.lovebabies.structures.interface_app_get_news_resp;
import com.xqj.lovebabies.threads.thread_interaction_get_news_list;
import com.xqj.lovebabies.widgets.UIListView;
import com.xqj.lovebabies.widgets.UIListView.OnRefreshListener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class FragmentInteractionNews extends Fragment {
	private View cmd_view_container = null;
	private UIListView cmd_listview_news = null;
//	private View bottom_view;
	private List<table_interaction_news> list_news = new ArrayList<table_interaction_news>();
	private List<table_interaction_news> list_temp = null;
	private interface_app_get_news_req get_news_req = null;
	private interface_app_get_news_resp get_news_resp = null;
	private adapter_fragment_interaction_news_listview adapter_listview = null;
	private table_interaction_news t_interaction_news = null;
	private int news_page_number = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			cmd_view_container = inflater.inflate(R.layout.fragment_interaction_news, null);
			// --
			cmd_listview_news = (UIListView) cmd_view_container.findViewById(R.id.fragment_interaction_news_listview_news);
			adapter_listview = new adapter_fragment_interaction_news_listview(getActivity());
//			bottom_view = (View)inflater.inflate(R.layout.bottom_view_more, null);
//			bottom_view.setVisibility(View.INVISIBLE);
//			cmd_listview_news.addFooterView(bottom_view);
			cmd_listview_news.setAdapter(adapter_listview);
			cmd_listview_news.setOnItemClickListener(new listener_fragment_interaction_news_listview_on_item_click(handler));
			cmd_listview_news.setonRefreshListener(new OnRefreshListener() {// ����ˢ��
				@Override
				public void onRefresh() {
					new AsyncTask<Void, Void, Void>() {
						protected Void doInBackground(Void... params) {
							try {
								// �����ȡ�ɳ���¼
								Message message = new Message();
								message.what = message_what_values.fragment_interaction_news_listview_on_drop_down;
								handler.sendMessage(message);
								Thread.sleep(1000);
							} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						}
						protected void onPostExecute(Void result) {
							adapter_listview.notifyDataSetChanged();
							cmd_listview_news.onRefreshComplete();
						}
					}.execute(null);
//					bottom_view.setVisibility(View.VISIBLE);
				}
			});
//			bottom_view.setOnClickListener(new OnClickListener() {// �鿴����
//				@Override
//				public void onClick(View arg0) {
//					Message message = new Message();
//					message.what = message_what_values.fragment_interaction_news_listview_on_bottom;
//					handler.sendMessage(message);
//				}
//			});
			// --
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cmd_view_container;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		f_action_get_news_list();//��ѯ��������
		super.onResume();
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == message_what_values.fragment_interaction_news_get_news_list_success) {
				// ��ȡ��̬�����б�ɹ�
				get_news_resp = (interface_app_get_news_resp) msg.obj;
				f_action_get_news_list_success(get_news_resp);
			}
			if (msg.what == message_what_values.fragment_interaction_news_get_news_list_failure) {
				// ��ȡ��̬�����б�ʧ��
				f_action_get_news_list_failure();
			}
			if (msg.what == message_what_values.fragment_interaction_news_listview_on_item_click) {
				// ��ȡlistview��item���
				f_action_listview_on_item_click(msg);
			}
			if (msg.what == message_what_values.fragment_interaction_news_listview_on_bottom) {
				// listview���ײ������¼�
				f_action_listview_on_bottom();
			}
			if (msg.what == message_what_values.fragment_interaction_news_listview_on_drop_down) {
				// listview�������������¼�
				f_action_listview_drop_down();
			}
		};
	};

	// �����ϻ��߻����ȡ��̬�б�
	private void f_action_get_news_list() {
		try {
			news_page_number = news_page_number == 0 ? 1 : news_page_number;
			get_news_req = new interface_app_get_news_req();
			get_news_req.setUser_id(PreferencesUtils.getString(getActivity(), "user_id"));
			get_news_req.setPage_number(news_page_number);
			// --
			new thread_interaction_get_news_list(handler, get_news_req).start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ��ȡ��̬�б�ɹ�
	private void f_action_get_news_list_success(interface_app_get_news_resp resp) {
		try {
			list_temp = resp.getNews();
			if (list_temp!=null && list_temp.size() > 0) {
				adapter_listview.removeAll();
				for(int i=0;i<list_temp.size();i++){
					adapter_listview.addItem(list_temp.get(i));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ��ȡ��̬�б�ʧ��
	private void f_action_get_news_list_failure() {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ���listview��item����
	private void f_action_listview_on_item_click(Message message) {
		try {
//			t_interaction_news = (table_interaction_news) message.obj;
//			Intent intent = new Intent();
//			intent.setClass(getActivity(), ActivityInteractionNewsDetail.class);
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("news", t_interaction_news);
//			intent.putExtra("news", bundle);
//			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// listview����ˢ��
	private void f_action_listview_drop_down() {
		try {
			news_page_number = 1;
			list_news.clear();
			f_action_get_news_list();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// listview��������
	private void f_action_listview_on_bottom() {
		try {
			news_page_number++;
			f_action_get_news_list();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
