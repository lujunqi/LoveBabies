package com.xqj.lovebabies.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.dbs_baby_my_baby;
import com.xqj.lovebabies.databases.dbs_interaction_notice;
import com.xqj.lovebabies.databases.table_album_my_baby;
import com.xqj.lovebabies.databases.table_interaction_notice;
import com.xqj.lovebabies.structures.interface_app_get_my_baby_req;
import com.xqj.lovebabies.structures.interface_app_get_my_baby_resp;

public class thread_album_get_my_baby_list extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Context context = null;
	private Handler handler = null;
	private interface_app_get_my_baby_resp resp = null;
	private interface_app_get_my_baby_req req = null;
	private table_album_my_baby t_album_my_baby = null;
	private dbs_baby_my_baby dbs_my_baby = null;
	private Message message = null;
	
	public thread_album_get_my_baby_list(Context context, Handler handler, interface_app_get_my_baby_req req) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
		this.req = req;
	}

	public void run() {
		System.out.println("--------------开启查询我的宝宝线程（"+req.getUser_id()+"）---------------");
		try {
			lock.lock();
			resp = network.ios_interface_app_get_my_baby(req);
			if(resp == null){
				message = new Message();
				message.what = message_what_values.activity_album_get_data_timeout;
				handler.sendMessage(message);
			}else{
				List<table_album_my_baby> list = resp.getList();
				list = list == null ? new ArrayList<table_album_my_baby>() : list;
				dbs_my_baby = new dbs_baby_my_baby(context);
				for (int i = 0; i < list.size(); i++) {
					t_album_my_baby = list.get(i);
					boolean delete_result = isinDelete(t_album_my_baby, dbs_my_baby);
					System.out.println("删除宝贝【"+t_album_my_baby.getBaby_name()+"】["+delete_result+"]");
					dbs_my_baby.do_insert_data(t_album_my_baby);
				}
				// --
				message = new Message();
				message.what = message_what_values.activity_album_my_baby_get_data_success;
				message.obj = resp;
				handler.sendMessage(message);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			lock.unlock();
		}
	}
	
	private boolean isinDelete(table_album_my_baby n, dbs_baby_my_baby dbs) {
		try {
			List<table_album_my_baby> list = dbs.do_select_data("select * from t_baby_my_baby where baby_id='" + n.getBaby_id() + "'");
			list = list == null ? new ArrayList<table_album_my_baby>() : list;
			if (list.get(0) != null) { 
				dbs.do_execute_sql("delete from t_baby_my_baby where baby_id='" + n.getBaby_id() + "'");
				return true; 
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}
