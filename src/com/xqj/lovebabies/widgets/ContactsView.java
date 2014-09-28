package com.xqj.lovebabies.widgets;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.trinea.android.common.util.TimeUtils;
import cn.trinea.android.common.view.DropDownListView;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityInteractionChatB;
import com.xqj.lovebabies.adapters.adapter_fragment_interaction_contacts_listview;
import com.xqj.lovebabies.commons.utils_character_comparator;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.dbs_interaction_contacts;
import com.xqj.lovebabies.databases.table_interaction_contacts;
import com.xqj.lovebabies.listeners.listener_fragment_interaction_contacts_listview_on_drop_down;
import com.xqj.lovebabies.listeners.listener_fragment_interaction_contacts_listview_on_item_click;
import com.xqj.lovebabies.listeners.listener_fragment_interaction_contacts_sidebar_on_letter_change;
import com.xqj.lovebabies.threads.thread_interaction_get_contacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 家园互动，联系人栏目主页面
 * @author sunshine
 *
 */
public class ContactsView {
	private utils_common_tools tools = new utils_common_tools();
	private View cmd_view_container = null;
	private DropDownListView cmd_listview_contacts = null;
	private ContactsSideBar cmd_sidebar_letters = null;
	private TextView cmd_textview_selected_letters = null;
	private List<table_interaction_contacts> list_contacts = null;
	private dbs_interaction_contacts dbs_interaction_contacts = null;
	private adapter_fragment_interaction_contacts_listview list_contacts_adapter = null;
	
	private Context context;
	private LayoutInflater inflater;
	
	public ContactsView(Context context){
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}
	
	public View getView(){
		try {
			//
			cmd_view_container = inflater.inflate(R.layout.fragment_interaction_contacts, null);
			cmd_listview_contacts = (DropDownListView) cmd_view_container.findViewById(R.id.fragment_interaction_contacts_listview_contacts);
			list_contacts = new ArrayList<table_interaction_contacts>();
			list_contacts_adapter = new adapter_fragment_interaction_contacts_listview(list_contacts, context);
			cmd_listview_contacts.setAdapter(list_contacts_adapter);
			cmd_listview_contacts.setOnDropDownListener(new listener_fragment_interaction_contacts_listview_on_drop_down(handler));
			cmd_listview_contacts.setOnItemClickListener(new listener_fragment_interaction_contacts_listview_on_item_click(handler));
			// --
			cmd_textview_selected_letters = (TextView) cmd_view_container.findViewById(R.id.fragment_interaction_contacts_textview_selected_letters);
			//
			cmd_sidebar_letters = (ContactsSideBar) cmd_view_container.findViewById(R.id.fragment_interaction_contacts_sidebar_letters);
			cmd_sidebar_letters.setTextView(cmd_textview_selected_letters);
			cmd_sidebar_letters.setOnTouchingLetterChangedListener(new listener_fragment_interaction_contacts_sidebar_on_letter_change(handler));
			// f_action_refesh_contacts();
			f_action_refesh_contacts_complete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cmd_view_container;
	}
	
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				if (msg.what == message_what_values.fragment_interaction_contacts_listview_on_drop_down) {
					f_action_refesh_contacts();
				}
				if (msg.what == message_what_values.fragment_interaction_contacts_sidebar_on_letter_change) {
					f_action_jump_letter_position(msg.obj.toString());
				}
				if (msg.what == message_what_values.fragment_interaction_contacts_refesh_complete) {
					f_action_refesh_contacts_complete();
				}
				if (msg.what == message_what_values.fragment_interaction_contacts_listview_on_item_click) {
					f_action_to_chat(msg);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	};

	private void f_action_to_chat(Message msg) {
		table_interaction_contacts contacts = null;
		try {
			contacts = (table_interaction_contacts) msg.obj;
			try {
				dbs_interaction_contacts = new dbs_interaction_contacts(context);
				List<table_interaction_contacts> list = dbs_interaction_contacts.do_select_data("select * from t_interaction_contacts where user_id='" + contacts.getUser_id() + "'");
				contacts = list.get(0);
			} catch (Exception ex) {
			}
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("chat_receiver", contacts);
			intent.putExtra("chat_receiver", bundle);
			intent.setClass(context, ActivityInteractionChatB.class);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void f_action_jump_letter_position(String letter) {
		try {
			int position = list_contacts_adapter.getPositionForSection(letter.charAt(0));
			cmd_listview_contacts.setSelection(position);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_refesh_contacts() {
		try {
			new thread_interaction_get_contacts(context, handler).start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void f_action_refesh_contacts_complete() {
		try {
			dbs_interaction_contacts = new dbs_interaction_contacts(context);
			list_contacts = dbs_interaction_contacts.do_select_data("select * from t_interaction_contacts");
			list_contacts = list_contacts == null ? new ArrayList<table_interaction_contacts>() : list_contacts;
			Collections.sort(list_contacts, new utils_character_comparator());
			list_contacts_adapter.refesh(list_contacts);
			cmd_listview_contacts.onDropDownComplete("更新于：" + TimeUtils.getCurrentTimeInString(new SimpleDateFormat("MM-dd HH:mm:ss")));
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

}
