package com.xqj.lovebabies.threads;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.*;

import com.xqj.lovebabies.commons.*;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.dbs_interaction_notice;
import com.xqj.lovebabies.databases.dbs_interaction_notice_comment;
import com.xqj.lovebabies.databases.dbs_interaction_notice_praise;
import com.xqj.lovebabies.databases.table_interaction_notice;
import com.xqj.lovebabies.databases.table_interaction_notice_comment;
import com.xqj.lovebabies.databases.table_interaction_notice_praise;
import com.xqj.lovebabies.structures.*;

public class thread_interaction_get_notice_detail extends Thread {
	private utils_network_interfaces network = new utils_network_interfaces();
	private interface_app_get_notice_detail_resp resp_notice_detail = null;
	private interface_app_get_notice_detail_req req_notice_detail = null;
	private interface_app_get_notice_comment_list_resp resp_notice_comment = null;
	private interface_app_get_notice_comment_list_req req_notice_comment = null;
	private interface_app_get_notice_praise_list_req req_notice_praise = null;
	private interface_app_get_notice_praise_list_resp resp_notice_praise = null;
	private table_interaction_notice t_interaction_notice = null;
	private table_interaction_notice_comment t_interaction_notice_comment = null;
	private table_interaction_notice_praise t_interaction_praise = null;
	private dbs_interaction_notice db_interaction_notice = null;
	private dbs_interaction_notice_praise db_interaction_notice_praise = null;
	private dbs_interaction_notice_comment db_interaction_notice_comment = null;
	private String sql = null;
	private Context context = null;
	private Handler handler = null;
	private Message message = null;
	private List<table_interaction_notice> list_interaction_notice = null;

	public thread_interaction_get_notice_detail(Context context, Handler handler, interface_app_get_notice_detail_req req) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.context = context;
		this.req_notice_detail = req;
	}

	public void run() {
		try {
			resp_notice_detail = new interface_app_get_notice_detail_resp();
			// 开始在缓存中获取
			sql = "select * from t_interaction_notice where notice_id='" + req_notice_detail.getNotice_id() + "'";
			db_interaction_notice = new dbs_interaction_notice(context);
			list_interaction_notice = db_interaction_notice.do_select_result(sql);
			list_interaction_notice = list_interaction_notice == null ? new ArrayList<table_interaction_notice>() : list_interaction_notice;
			t_interaction_notice = list_interaction_notice.get(0);
			if (t_interaction_notice != null) {// 在缓存中
				try {
					resp_notice_detail.setNotice(t_interaction_notice);
				} catch (Exception e) {
					// TODO: handle exception
				}
				// 实时获取点赞列表
				try {
					req_notice_praise = new interface_app_get_notice_praise_list_req();
					req_notice_praise.setNotice_id(req_notice_detail.getNotice_id());
					req_notice_praise.setUser_id(req_notice_detail.getUser_id());
					resp_notice_praise = network.ios_interface_app_get_notice_praise(req_notice_praise);
					resp_notice_detail.setPraises(resp_notice_praise.getPraises());
				} catch (Exception e) {
					// TODO: handle exception
				}
				// 实时获取评论列表
				try {
					req_notice_comment = new interface_app_get_notice_comment_list_req();
					req_notice_comment.setNotice_id(req_notice_detail.getNotice_id());
					req_notice_comment.setUser_id(req_notice_detail.getUser_id());
					resp_notice_comment = network.ios_interface_app_get_notice_comment(req_notice_comment);
					resp_notice_detail.setComments(resp_notice_comment.getComments());
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else {// 不在缓存中
				resp_notice_detail = network.ios_interface_app_get_notice_detail(req_notice_detail);
				t_interaction_notice = resp_notice_detail.getNotice();
				db_interaction_notice.do_insert_data(t_interaction_notice);
			}
			// --
			message = new Message();
			if (resp_notice_detail != null) {
				message.what = message_what_values.activity_interaction_notice_detail_get_data_success;
				message.obj = resp_notice_detail;
			} else {
				message.what = message_what_values.activity_interaction_notice_detail_get_data_failure;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
