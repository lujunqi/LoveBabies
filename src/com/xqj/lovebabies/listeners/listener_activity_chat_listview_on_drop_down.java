package com.xqj.lovebabies.listeners;

import android.os.Handler;
import android.os.Message;
import com.xqj.lovebabies.contants.*;
import cn.trinea.android.common.view.DropDownListView.*;

public class listener_activity_chat_listview_on_drop_down implements OnDropDownListener {
	private Handler handler = null;
	private Message message = null;

	public listener_activity_chat_listview_on_drop_down(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onDropDown() {
		// TODO Auto-generated method stub
		try {
			message = new Message();
			message.what = message_what_values.activity_interaction_chat_listview_message_drop_down;
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
