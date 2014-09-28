package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_interaction_contacts;
import com.xqj.lovebabies.structures.fragment_interaction_dialogue_item;

import android.os.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.*;

public class listener_fragment_interaction_dialogue_listview_on_item_click implements OnItemClickListener {
	private Handler handler = null;
	private Message message = null;
	private table_interaction_contacts t_interaction_contacts = null;
	private fragment_interaction_dialogue_item item = null;

	public listener_fragment_interaction_dialogue_listview_on_item_click(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		try {
			// --
			item = new fragment_interaction_dialogue_item();
			item.setDialogue_user_id((TextView) view.findViewById(R.id.fragment_interaction_dialogue_item_textview_user_id));
			item.setDialogue_user_name((TextView) view.findViewById(R.id.fragment_interaction_dialogue_item_textview_user_name));
			item.setDialogue_user_icon_path((TextView) view.findViewById(R.id.fragment_interaction_dialogue_item_textview_user_icon_path));
			// --
			t_interaction_contacts = new table_interaction_contacts();
			t_interaction_contacts.setUser_id(item.getDialogue_user_id().getText().toString());
			t_interaction_contacts.setUser_real_name(item.getDialogue_user_name().getText().toString());
			t_interaction_contacts.setUser_icon_path(item.getDialogue_user_icon_path().getText().toString());
			// --
			message = new Message();
			message.what = message_what_values.fragment_interaction_dialogue_listview_on_item_click;
			message.obj = t_interaction_contacts;
			handler.sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

}
