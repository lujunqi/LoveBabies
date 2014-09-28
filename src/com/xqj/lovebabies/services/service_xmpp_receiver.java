package com.xqj.lovebabies.services;

import com.xqj.lovebabies.commons.utils_xmpp_client;
import com.xqj.lovebabies.threads.thread_interaction_xmpp_user_login;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class service_xmpp_receiver extends Service {
	private thread_interaction_xmpp_user_login t_login = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		try {
			t_login = new thread_interaction_xmpp_user_login(this);
			thread_interaction_xmpp_user_login.flag = true;
			t_login.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			thread_interaction_xmpp_user_login.flag = false;// Í£Ö¹Ïß³Ì
			utils_xmpp_client.getInstance().close_connection();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
