package com.xqj.lovebabies.threads;

import java.util.ArrayList;
import java.util.List;

import com.xqj.lovebabies.commons.*;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_interaction_news;
import com.xqj.lovebabies.structures.*;

import android.os.*;

public class thread_interaction_get_news_list extends Thread {
	private utils_network_interfaces network = new utils_network_interfaces();
	private List<table_interaction_news> list_news = null;
	private interface_app_get_news_resp app_get_news_resp = null;
	private interface_app_get_news_req app_get_news_req = null;
	private Handler handler = null;
	private Message message = null;

	public thread_interaction_get_news_list(Handler handler, interface_app_get_news_req app_get_news_req) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.app_get_news_req = app_get_news_req;
	}

	public void run() {
		message = new Message();
		try {
			app_get_news_resp = network.ios_interface_app_get_news(app_get_news_req);
			app_get_news_resp = app_get_news_resp == null ? new interface_app_get_news_resp() : app_get_news_resp;
			list_news = app_get_news_resp.getNews();
			app_get_news_resp.setPage_number(app_get_news_req.getPage_number());
			list_news = list_news == null ? new ArrayList<table_interaction_news>() : list_news;
			// 这里插入缓存,暂时没做是目前的服务端不能很好地支持缓存
			// -------------------
			message.what = message_what_values.fragment_interaction_news_get_news_list_success;
			message.obj = app_get_news_resp;
		} catch (Exception e) {
			message.what = message_what_values.fragment_interaction_news_get_news_list_failure;

		}

		handler.sendMessage(message);
	}
}
