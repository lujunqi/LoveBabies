package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;

import android.os.*;
import android.view.View;
import android.view.View.*;

public class listener_activity_chat_item_button_click implements OnClickListener {
	private Handler handler = null;
	private Message message = null;
	private String uri = null;

	public listener_activity_chat_item_button_click(String uri, Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.uri = uri;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.activity_chat_send_button_voice_content_layout) {
			message = new Message();
			message.what = message_what_values.activity_interaction_chat_message_play_voice;
			message.obj = uri;
			handler.sendMessage(message);
		}
	}
}
