package com.xqj.lovebabies.threads;

import cn.trinea.android.common.util.*;
import com.xqj.lovebabies.commons.*;
import com.xqj.lovebabies.contants.*;
import com.xqj.lovebabies.structures.*;
import android.os.Handler;
import android.os.Message;

public class thread_interaction_delete_notice_comment extends Thread {
	private utils_network_interfaces network = new utils_network_interfaces();
	private interface_app_delete_notice_comment_req delete_notice_comment_req = null;
	private interface_app_delete_notice_comment_resp delete_notice_comment_resp = null;
	private Handler handler = null;
	private Message message = null;

	public thread_interaction_delete_notice_comment(Handler handler, interface_app_delete_notice_comment_req req) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.delete_notice_comment_req = req;
	}

	public void run() {
		message = new Message();
		try {
			delete_notice_comment_resp = network.ios_interface_app_delete_notice_comment_resp(delete_notice_comment_req);
			delete_notice_comment_resp = delete_notice_comment_resp == null ? new interface_app_delete_notice_comment_resp() : delete_notice_comment_resp;
			if (null != delete_notice_comment_resp.getResult_code() && StringUtils.isEquals(delete_notice_comment_resp.getResult_code(), "1")) {
				message.what = message_what_values.activity_interaction_notice_detail_delete_comment_success;
			} else {
				message.what = message_what_values.activity_interaction_notice_detail_delete_comment_failture;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		handler.sendMessage(message);
	}
}
