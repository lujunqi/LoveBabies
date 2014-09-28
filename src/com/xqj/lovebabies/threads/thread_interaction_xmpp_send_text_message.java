package com.xqj.lovebabies.threads;

import android.os.Handler;
import android.os.Message;
import com.xqj.lovebabies.commons.*;
import com.xqj.lovebabies.contants.*;
import com.xqj.lovebabies.databases.*;

public class thread_interaction_xmpp_send_text_message extends Thread {
	private table_interaction_message message_bean = null;
	private Handler handler = null;
	private Message message = null;

	public thread_interaction_xmpp_send_text_message(table_interaction_message message_bean, Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.message_bean = message_bean;
	}

	public void run() {
		try {
			message = new Message();
			if (utils_xmpp_client.getInstance().send_message(message_bean.getMessage_receiver(), message_bean.getMessage_content())) {
				message.what = message_what_values.activity_interaction_chat_message_send_text_success;
			} else {
				message.what = message_what_values.activity_interaction_chat_message_send_text_failure;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
