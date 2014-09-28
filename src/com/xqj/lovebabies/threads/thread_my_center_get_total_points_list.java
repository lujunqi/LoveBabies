package com.xqj.lovebabies.threads;

import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_get_total_point_req;
import com.xqj.lovebabies.structures.interface_app_get_total_point_resp;

public class thread_my_center_get_total_points_list extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Context context = null;
	private Handler handler = null;
	private interface_app_get_total_point_resp resp = null;
	private interface_app_get_total_point_req req = null;
	private Message message = null;
	
	public thread_my_center_get_total_points_list(Context context, Handler handler, interface_app_get_total_point_req req) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
		this.req = req;
	}

	public void run() {
		System.out.println("--------------开启查询积分总数线程---------------");
		try {
			lock.lock();
			resp = network.ios_interface_app_get_total_points(req);
			if(resp == null){
				message = new Message();
				message.what = message_what_values.fragment_my_center_get_data_failed;
				handler.sendMessage(message);
			}else{
				message = new Message();
				message.what = message_what_values.activity_my_center_get_total_points_success;
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
