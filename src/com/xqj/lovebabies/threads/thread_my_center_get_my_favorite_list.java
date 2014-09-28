package com.xqj.lovebabies.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_my_center_favorite;
import com.xqj.lovebabies.structures.interface_app_get_my_favorite_req;
import com.xqj.lovebabies.structures.interface_app_get_my_favorite_resp;

public class thread_my_center_get_my_favorite_list extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Context context = null;
	private Handler handler = null;
	private interface_app_get_my_favorite_resp resp = null;
	private interface_app_get_my_favorite_req req = null;
	private table_my_center_favorite t_my_favorite = null;
	private Message message = null;
	
	public thread_my_center_get_my_favorite_list(Context context, Handler handler, interface_app_get_my_favorite_req req) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
		this.req = req;
	}

	public void run() {
		System.out.println("--------------������ѯ�ҵ��ղ��̣߳�"+req.getUser_id()+"��---------------");
		try {
			lock.lock();
			resp = network.ios_interface_app_get_my_favorite(req);
			if(resp == null){
				message = new Message();
				message.what = message_what_values.fragment_my_center_get_data_failed;
				handler.sendMessage(message);
			}else{
				List<table_my_center_favorite> list = resp.getList();
				list = list == null ? new ArrayList<table_my_center_favorite>() : list;
//				db_interaction_notice = new dbs_interaction_notice(context);
//				for (int i = 0; i < list.size(); i++) {
//					t_album_my_baby = list.get(i);
//					if (!isin(t_album_my_baby, db_interaction_notice)) {
//						db_interaction_notice.do_insert_data(t_album_my_baby);
//					}
//				}
				// --
				message = new Message();
				message.what = message_what_values.activity_my_center_get_my_favorite_success;
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
