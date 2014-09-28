package com.xqj.lovebabies.threads;

import android.content.Context;
import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.commons.utils_xmpp_client;

public class thread_interaction_xmpp_user_login extends Thread {
	private Context context = null;
	public static boolean flag = true;

	public thread_interaction_xmpp_user_login(Context context) {
		this.context = context;
	}

	public void run() {
		System.out.println("thread_interaction_xmpp_user_login start ...");
//		for (;;) {
//			try {
//				if (!flag) break;
//				// 获取用户信息
//				// System.out.println("-----------------user_id:" + user_name);
//				utils_xmpp_client.getInstance().create_connection();
//				if (!utils_xmpp_client.getInstance().get_connection().isAuthenticated()) {
//					utils_xmpp_client.getInstance().login_user(PreferencesUtils.getString(context, "user_id"), 
//							PreferencesUtils.getString(context, "user_password"), context);
//					System.out.println("utils_xmpp_client.getInstance().login_user("
//							+PreferencesUtils.getString(context, "user_id")+","
//							+PreferencesUtils.getString(context, "user_password")+")");
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					Thread.sleep(1000 * 60 * 5);
//				} catch (Exception e2) {
//					// TODO: handle exception
//				}
//			}
//		}
		
		try {
			utils_xmpp_client.getInstance().create_connection();
			if (!utils_xmpp_client.getInstance().get_connection().isAuthenticated()) {
				utils_xmpp_client.getInstance().login_user(PreferencesUtils.getString(context, "user_id"), 
						PreferencesUtils.getString(context, "user_password"), context);
				System.out.println("utils_xmpp_client.getInstance().login_user("
						+PreferencesUtils.getString(context, "user_id")+","
						+PreferencesUtils.getString(context, "user_password")+")");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
