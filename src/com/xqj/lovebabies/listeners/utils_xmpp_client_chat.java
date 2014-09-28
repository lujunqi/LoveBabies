package com.xqj.lovebabies.listeners;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;

import android.content.Context;

public class utils_xmpp_client_chat implements ChatManagerListener {
	private Context context = null;

	public utils_xmpp_client_chat(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public void chatCreated(Chat arg0, boolean arg1) {
		// TODO Auto-generated method stub
		try {
			arg0.addMessageListener(new utils_xmpp_client_message(context));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
