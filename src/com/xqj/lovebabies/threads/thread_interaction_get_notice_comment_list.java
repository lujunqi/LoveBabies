package com.xqj.lovebabies.threads;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_interaction_notice_comment;
import com.xqj.lovebabies.structures.*;

public class thread_interaction_get_notice_comment_list extends Thread {
	private utils_network_interfaces network = new utils_network_interfaces();
	private interface_app_get_notice_comment_list_req get_notice_comment_list_req = null;
	private interface_app_get_notice_comment_list_resp get_notice_comment_list_resp = null;
	private Handler handler = null;
	private Message message = null;

	public thread_interaction_get_notice_comment_list(Handler handler, interface_app_get_notice_comment_list_req req) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.get_notice_comment_list_req = req;
	}

	public void run() {
		try {
			message = new Message();
			get_notice_comment_list_resp = network.ios_interface_app_get_notice_comment(get_notice_comment_list_req);
			get_notice_comment_list_resp = get_notice_comment_list_resp == null ? new interface_app_get_notice_comment_list_resp() : get_notice_comment_list_resp;
			List<table_interaction_notice_comment> list = get_notice_comment_list_resp.getComments();
			list = list == null ? new ArrayList<table_interaction_notice_comment>() : list;
			if (list.size() > 0) {
				message.what = message_what_values.activity_interaction_notice_detail_get_comment_list_success;
				message.obj = get_notice_comment_list_resp;
			} else {
				message.what = message_what_values.activity_interaction_notice_detail_get_comment_list_failture;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
