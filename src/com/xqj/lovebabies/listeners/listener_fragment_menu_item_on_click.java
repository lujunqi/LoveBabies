package com.xqj.lovebabies.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import com.xqj.lovebabies.contants.*;
import android.os.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.*;

public class listener_fragment_menu_item_on_click implements OnItemClickListener {
	private Handler handler = null;
	private Message message = null;
	private ArrayList<HashMap<String, Object>> list = null;

	public listener_fragment_menu_item_on_click(Handler handler, ArrayList<HashMap<String, Object>> list) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.list = list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		try {
			message = new Message();
			message.what = message_what_values.fragment_menu_listview_on_item_click;
			message.arg1 = (Integer) list.get(position).get("menu_logo");			
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
