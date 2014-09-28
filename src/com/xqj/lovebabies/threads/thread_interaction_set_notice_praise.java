package com.xqj.lovebabies.threads;

import cn.trinea.android.common.util.StringUtils;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_set_notice_praise_req;
import com.xqj.lovebabies.structures.interface_app_set_notice_praise_resp;

import android.os.*;

public class thread_interaction_set_notice_praise extends Thread {
	private Handler handler = null;
	private Message message = null;
	private utils_network_interfaces network = new utils_network_interfaces();
	private interface_app_set_notice_praise_resp set_notice_praise_resp = null;
	private interface_app_set_notice_praise_req set_notice_praise_req = null;

	public thread_interaction_set_notice_praise(Handler handler, interface_app_set_notice_praise_req req) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.set_notice_praise_req = req;
	}

	public void run() {
		try {
			message = new Message();
			set_notice_praise_resp = network.ios_interface_app_set_notice_praise_resp(set_notice_praise_req);
			if (StringUtils.isEquals(set_notice_praise_resp.getResult_code(), "1")) {
				message.what = message_what_values.activity_interaction_notice_detail_set_praise_success;
			} else {
				message.what = message_what_values.activity_interaction_notice_detail_set_praise_failture;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
