package com.xqj.lovebabies.threads;

import cn.trinea.android.common.util.ShellUtils;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_user_login_req;
import com.xqj.lovebabies.structures.interface_app_user_login_resp;

import android.os.Handler;
import android.os.Message;

public class thread_common_system_user_login extends Thread {
	private utils_network_interfaces network = new utils_network_interfaces();
	private Handler handler = null;
	private Message message = null;
	private interface_app_user_login_req req = null;
	private interface_app_user_login_resp resp = null;

	public thread_common_system_user_login(Handler handler, interface_app_user_login_req req) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.req = req;
	}

	public void run() {
		try {
			resp = network.ios_interface_app_user_login(req);
			message = new Message();
			if (resp == null) {
				message.what = message_what_values.activity_login_app_user_login_result_failure;
			} else {
				message.what = message_what_values.activity_login_app_user_login_result_success;
				message.obj = resp;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
