package com.xqj.lovebabies.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.xqj.lovebabies.commons.*;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.dbs_interaction_notice;
import com.xqj.lovebabies.databases.table_interaction_notice;
import com.xqj.lovebabies.structures.*;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class thread_interaction_get_notice_list extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Context context = null;
	private Handler handler = null;
	private interface_app_get_notice_list_req req = null;
	private interface_app_get_notice_list_resp resp = null;
	private dbs_interaction_notice db_interaction_notice = null;
	private table_interaction_notice t_interaction_notice = null;
	private Message message = null;

	public thread_interaction_get_notice_list(Context context, Handler handler, interface_app_get_notice_list_req req) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
		this.req = req;
	}

	public void run() {

		message = new Message();
		try {
			lock.lock();
			resp = network.ios_interface_app_get_notice_list(req);
			resp = resp == null ? new interface_app_get_notice_list_resp() : resp;
			List<table_interaction_notice> list = resp.getNotices();
			list = list == null ? new ArrayList<table_interaction_notice>() : list;
			db_interaction_notice = new dbs_interaction_notice(context);
			for (int i = 0; i < list.size(); i++) {
				t_interaction_notice = list.get(i);
				if (!isin(t_interaction_notice, db_interaction_notice)) {
					db_interaction_notice.do_insert_data(t_interaction_notice);
				}
			}
			// --
			message.what = message_what_values.fragment_interaction_notice_refesh_network_success;
			message.obj = resp;
			message.arg1 = req.getPage_number();
		} catch (Exception e) {
			message.what = message_what_values.fragment_interaction_notice_refesh_network_failure;
			// TODO: handle exception
		} finally {
			lock.unlock();
		}
		handler.sendMessage(message);
	}

	private boolean isin(table_interaction_notice n, dbs_interaction_notice dbs) {
		try {
			List<table_interaction_notice> lll = dbs.do_select_result("select * from t_interaction_notice where notice_id='" + n.getNotice_id() + "'");
			lll = lll == null ? new ArrayList<table_interaction_notice>() : lll;
			if (lll.get(0) != null) { return true; }
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}
