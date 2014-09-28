package com.xqj.lovebabies.broadcasts;

import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_interaction_message;
import com.xqj.lovebabies.listeners.utils_xmpp_client_message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

//在activity_chat中动态地注册的消息接收广播接收器，用于接收消息更新聊天界面
public class activity_chat_message_receiver extends BroadcastReceiver {
	
	private table_interaction_message t_interaction_message = null;
	private Handler handler = null;
	private Message message = null;

	public activity_chat_message_receiver(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try {
			if (intent.getAction() == utils_xmpp_client_message.ACTION_RECEIVE_INTERACTION_MESSAGE) {
				Bundle bundle = intent.getBundleExtra("interaction_message");
				t_interaction_message = (table_interaction_message) bundle.getSerializable("received_interaction_message");
				message = new Message();
				message.what = message_what_values.activity_interaction_chat_message_receive_message;
				message.obj = t_interaction_message;
				handler.sendMessage(message);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
