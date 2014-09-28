package com.xqj.lovebabies.threads;

import java.util.ArrayList;
import java.util.List;

import com.xqj.lovebabies.commons.*;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_interaction_news;
import com.xqj.lovebabies.structures.*;

import android.os.Handler;
import android.os.Message;

public class thread_interaction_get_news_detail extends Thread {
	private utils_network_interfaces network = new utils_network_interfaces();
	private interface_app_get_news_req get_news_req = null;
	private interface_app_get_news_resp get_news_resp = null;
	private List<table_interaction_news> list_interaction_news = null;
	private table_interaction_news t_interaction_news = null;
	private Handler handler;
	private Message message = null;

	public thread_interaction_get_news_detail(Handler handler, interface_app_get_news_req req) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.get_news_req = req;
	}

	public void run() {
		try {
			message = new Message();
			get_news_resp = network.ios_interface_app_get_news(get_news_req);
			get_news_resp = get_news_resp == null ? new interface_app_get_news_resp() : get_news_resp;
			list_interaction_news = get_news_resp.getNews();
			list_interaction_news = list_interaction_news == null ? new ArrayList<table_interaction_news>() : list_interaction_news;
			t_interaction_news = list_interaction_news.get(0);
			if (null != t_interaction_news) {
				message.what = message_what_values.activity_interaction_news_detail_get_news_detail_success;
				message.obj = t_interaction_news;
			} else {
				message.what = message_what_values.activity_interaction_news_detail_get_news_detail_failture;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
