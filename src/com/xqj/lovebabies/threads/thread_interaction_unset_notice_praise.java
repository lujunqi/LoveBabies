package com.xqj.lovebabies.threads;

import android.os.Handler;
import android.os.Message;
import cn.trinea.android.common.util.StringUtils;

import com.xqj.lovebabies.commons.*;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.*;

public class thread_interaction_unset_notice_praise extends Thread {
	private utils_network_interfaces network = new utils_network_interfaces();
	private interface_app_unset_notice_praise_req unset_notice_praise_req = null;
	private interface_app_unset_notice_praise_resp unset_notice_praise_resp = null;
	private Handler handler = null;
	private Message message = null;

	public thread_interaction_unset_notice_praise(Handler handler, interface_app_unset_notice_praise_req req) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.unset_notice_praise_req = req;
	}

	public void run() {
		try {
			message = new Message();
			unset_notice_praise_resp = network.ios_interface_app_unset_notice_praise_resp(unset_notice_praise_req);
			if (StringUtils.isEquals(unset_notice_praise_resp.getResult_code(), "1")) {
				message.what = message_what_values.activity_interaction_notice_detail_unset_praise_success;
			} else {
				message.what = message_what_values.activity_interaction_notice_detail_unset_praise_failture;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
