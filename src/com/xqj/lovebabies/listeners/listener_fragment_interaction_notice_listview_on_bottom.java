package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.contants.message_what_values;

import android.os.*;
import android.view.View;
import android.view.View.*;

public class listener_fragment_interaction_notice_listview_on_bottom implements OnClickListener {
	private Handler handler = null;
	private Message message = null;

	public listener_fragment_interaction_notice_listview_on_bottom(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onClick(View v) {
		try {
			message = new Message();
			message.what = message_what_values.fragment_interaction_notice_on_bottom_click;
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
