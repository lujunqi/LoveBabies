package com.xqj.lovebabies.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityInteractionChatB;
import com.xqj.lovebabies.adapters.adapter_fragment_interaction_dialogue_listview;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.dbs_interaction_contacts;
import com.xqj.lovebabies.databases.table_interaction_contacts;
import com.xqj.lovebabies.listeners.listener_fragment_interaction_dialogue_listview_on_item_click;
import com.xqj.lovebabies.widgets.UIListView;
import com.xqj.lovebabies.widgets.UIListView.OnRefreshListener;

import cn.trinea.android.common.util.TimeUtils;
import cn.trinea.android.common.view.DropDownListView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentInteractionDialogue extends Fragment {
	private UIListView cmd_listview_interaction_dialogue = null;
	private adapter_fragment_interaction_dialogue_listview dialogue_listview_adapter = null;
	private List<table_interaction_contacts> list_contacts = null;
	private dbs_interaction_contacts db_interaction_contacts = null;
	private table_interaction_contacts t_interaction_contacts = null;
	private View cmd_view_container = null;

	public FragmentInteractionDialogue() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			list_contacts = new ArrayList<table_interaction_contacts>();
			cmd_view_container = inflater.inflate(R.layout.fragment_interaction_dialogue, null);
			// --
			cmd_listview_interaction_dialogue = (UIListView) cmd_view_container.findViewById(R.id.fragment_interaction_dialogue_listview_container);
			dialogue_listview_adapter = new adapter_fragment_interaction_dialogue_listview(getActivity());
			cmd_listview_interaction_dialogue.setAdapter(dialogue_listview_adapter);
			cmd_listview_interaction_dialogue.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int index, long arg3) {
					t_interaction_contacts = (table_interaction_contacts) dialogue_listview_adapter.getItem(index);
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("chat_receiver", t_interaction_contacts);
					intent.putExtra("chat_receiver", bundle);
					intent.setClass(getActivity(), ActivityInteractionChatB.class);
					startActivity(intent);
				}
			});
			cmd_listview_interaction_dialogue.setonRefreshListener(new OnRefreshListener() {// 下拉刷新
				@Override
				public void onRefresh() {
					new AsyncTask<Void, Void, Void>() {
						protected Void doInBackground(Void... params) {
							try {
								f_action_refesh_data();//查询本地数据
								} catch (Exception e) {
								e.printStackTrace();
							}
							return null;
						}
						protected void onPostExecute(Void result) {
							System.out.println("onPostExecute...");
							dialogue_listview_adapter.notifyDataSetChanged();
							cmd_listview_interaction_dialogue.onRefreshComplete();
						}
					}.execute(null);
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		f_action_refesh_data();//查询本地数据
		
		return cmd_view_container;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void f_action_refesh_data() {
		String sql = null;
		try {
			db_interaction_contacts = new dbs_interaction_contacts(getActivity());
			sql = "select * from t_interaction_contacts where 1=1 ";
			sql += "and user_last_session_time is not null";
			System.out.println("---------查询最新会话---------");
			System.out.println(sql);
			list_contacts = db_interaction_contacts.do_select_data(sql);
			// --
			if(list_contacts!=null && list_contacts.size()>0){
				dialogue_listview_adapter.removeAll();
				for(int i=0;i<list_contacts.size();i++){
					System.out.println(list_contacts.get(i).getUser_last_session_content());
					dialogue_listview_adapter.addItem(list_contacts.get(i));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


}
