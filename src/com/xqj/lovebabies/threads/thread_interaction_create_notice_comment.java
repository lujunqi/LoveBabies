package com.xqj.lovebabies.threads;

import cn.trinea.android.common.util.StringUtils;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.*;

import android.os.Handler;
import android.os.Message;

public class thread_interaction_create_notice_comment extends Thread {
	private utils_network_interfaces network = new utils_network_interfaces();
	private interface_app_create_notice_comment_req create_notice_comment_req = null;
	private interface_app_create_notice_comment_resp create_notice_comment_resp = null;
	private Handler handler = null;
	private Message message = null;

	public thread_interaction_create_notice_comment(Handler handler, interface_app_create_notice_comment_req req) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.create_notice_comment_req = req;
	}

	public void run() {
		message = new Message();
		try {
			create_notice_comment_resp = network.ios_interface_app_create_notice_comment_resp(create_notice_comment_req);
			if (StringUtils.isEquals(create_notice_comment_resp.getResult_code(), "1")) {
				message.what = message_what_values.activity_interaction_notice_detail_submit_comment_success;				
			} else {
				message.what = message_what_values.activity_interaction_notice_detail_submit_comment_failure;
			}
		} catch (Exception e) {
			// TODO: handle exception
			message.what = message_what_values.activity_interaction_notice_detail_submit_comment_failure;
		}
		handler.sendMessage(message);
	}
}
