package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.contants.message_what_values;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.*;

public class listener_fragment_interaction_news_listview_on_bottom implements OnClickListener {
	private Handler handler = null;
	private Message message = null;

	public listener_fragment_interaction_news_listview_on_bottom(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		message = new Message();
		message.what = message_what_values.fragment_interaction_news_listview_on_bottom;
		handler.sendMessage(message);
	}

}
