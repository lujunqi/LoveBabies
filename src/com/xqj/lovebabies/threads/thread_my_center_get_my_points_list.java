package com.xqj.lovebabies.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.dbs_my_center_my_points;
import com.xqj.lovebabies.databases.table_my_center_my_points;
import com.xqj.lovebabies.structures.interface_app_get_my_points_req;
import com.xqj.lovebabies.structures.interface_app_get_my_points_resp;

public class thread_my_center_get_my_points_list extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Context context = null;
	private Handler handler = null;
	private interface_app_get_my_points_resp resp = null;
	private interface_app_get_my_points_req req = null;
	private Message message = null;
	private dbs_my_center_my_points db_my_center_my_points;
	private table_my_center_my_points t_my_center_my_points;
	
	public thread_my_center_get_my_points_list(Context context, Handler handler, interface_app_get_my_points_req req) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
		this.req = req;
	}

	public void run() {
		System.out.println("--------------开启查询我的积分线程（"+req.getUser_id()+"）---------------");
		try {
			lock.lock();
			resp = network.ios_interface_app_get_my_points(req);
			if(resp == null){
				message = new Message();
				message.what = message_what_values.fragment_my_center_get_data_failed;
				handler.sendMessage(message);
			}else{
				List<table_my_center_my_points> list = resp.getList();
				list = list == null ? new ArrayList<table_my_center_my_points>() : list;
				db_my_center_my_points = new dbs_my_center_my_points(context);
				for (int i = 0; i < list.size(); i++) {
					t_my_center_my_points = list.get(i);
					if (!isin(t_my_center_my_points, db_my_center_my_points)) {
						db_my_center_my_points.do_insert_data(t_my_center_my_points);
					}
				}
				// --
				message = new Message();
				message.what = message_what_values.activity_my_center_get_my_points_success;
				message.obj = resp;
				handler.sendMessage(message);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			lock.unlock();
		}
	}
	
	private boolean isin(table_my_center_my_points n, dbs_my_center_my_points dbs) {
		try {
			List<table_my_center_my_points> list = dbs.do_select_data("select * from t_my_center_my_points where user_id='" + n.getUser_id() + "' and time='" + n.getTime() + "'");
			list = list == null ? new ArrayList<table_my_center_my_points>() : list;
			if (list.get(0) != null) { return true; }
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}
