package com.xqj.lovebabies.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_album_growth_comment;
import com.xqj.lovebabies.databases.table_album_growth_praise;
import com.xqj.lovebabies.databases.table_album_my_baby;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_comment_req;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_comment_resp;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_praise_req;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_praise_resp;
import com.xqj.lovebabies.structures.interface_app_get_my_baby_req;
import com.xqj.lovebabies.structures.interface_app_get_my_baby_resp;

public class thread_album_get_growth_praise_list extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Handler handler = null;
	private interface_app_get_baby_growth_praise_resp resp = null;
	private interface_app_get_baby_growth_praise_req req = null;
	private Message message = null;
	
	public thread_album_get_growth_praise_list(Handler handler, interface_app_get_baby_growth_praise_req req) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.req = req;
	}

	public void run() {
		System.out.println("--------------开启查询成长记录点赞列表（"+req.getUser_id()+"）（"+req.getRecord_id()+"）---------------");
		try {
			lock.lock();
			resp = network.ios_interface_app_get_baby_growth_praise(req);
			if(resp == null){
				message = new Message();
				message.what = message_what_values.activity_album_get_data_timeout;
				handler.sendMessage(message);
			}else{
				List<table_album_growth_praise> list = resp.getList();
				list = list == null ? new ArrayList<table_album_growth_praise>() : list;
//				db_interaction_notice = new dbs_interaction_notice(context);
//				for (int i = 0; i < list.size(); i++) {
//					t_album_my_baby = list.get(i);
//					if (!isin(t_album_my_baby, db_interaction_notice)) {
//						db_interaction_notice.do_insert_data(t_album_my_baby);
//					}
//				}
				// --
				message = new Message();
				message.what = message_what_values.activity_get_baby_growth_praise_list_success;
				message.obj = resp;
				handler.sendMessage(message);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			lock.unlock();
		}
	}
}
