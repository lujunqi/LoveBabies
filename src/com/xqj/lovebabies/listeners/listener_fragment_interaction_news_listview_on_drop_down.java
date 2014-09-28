package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.contants.message_what_values;

import android.os.Handler;
import android.os.Message;
import cn.trinea.android.common.view.DropDownListView.*;

public class listener_fragment_interaction_news_listview_on_drop_down implements OnDropDownListener {
	private Handler handler = null;
	private Message message = null;

	public listener_fragment_interaction_news_listview_on_drop_down(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onDropDown() {
		// TODO Auto-generated method stub
		message = new Message();
		message.what = message_what_values.fragment_interaction_news_listview_on_drop_down;
		handler.sendMessage(message);
	}

}
