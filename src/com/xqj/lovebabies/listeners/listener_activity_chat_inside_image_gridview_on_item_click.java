package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.contants.message_what_values;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class listener_activity_chat_inside_image_gridview_on_item_click implements OnItemClickListener {
	private Handler handler = null;
	private Message message = null;

	public listener_activity_chat_inside_image_gridview_on_item_click(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		message = new Message();
		message.what = message_what_values.activity_interaction_chat_message_send_image_inside;
		message.arg1 = arg2;
		handler.sendMessage(message);
	}
}
