package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_interaction_contacts;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class listener_fragment_interaction_contacts_listview_on_item_click implements OnItemClickListener {
	private table_interaction_contacts t_interaction_contacts_list = null;
	private Handler handler = null;
	private Message message = null;

	public listener_fragment_interaction_contacts_listview_on_item_click(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		try {
			message = new Message();
			t_interaction_contacts_list = new table_interaction_contacts();
			message.what = message_what_values.fragment_interaction_contacts_listview_on_item_click;
			TextView tv_user_id = (TextView) view.findViewById(com.xqj.lovebabies.R.id.fragment_interaction_contacts_item_user_id);
			TextView tv_user_real_name = (TextView) view.findViewById(com.xqj.lovebabies.R.id.fragment_interaction_contacts_item_user_real_name);

			t_interaction_contacts_list.setUser_id(tv_user_id.getText().toString().trim());
			t_interaction_contacts_list.setUser_real_name(tv_user_real_name.getText().toString().trim());
			message.obj = t_interaction_contacts_list;
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
