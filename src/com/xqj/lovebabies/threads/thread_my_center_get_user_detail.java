package com.xqj.lovebabies.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_get_user_detail_req;
import com.xqj.lovebabies.structures.interface_app_get_user_detail_resp;

public class thread_my_center_get_user_detail extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Context context = null;
	private Handler handler = null;
	private interface_app_get_user_detail_resp resp = null;
	private interface_app_get_user_detail_req req = null;
	private Message message = null;
	
	public thread_my_center_get_user_detail(Context context, Handler handler, interface_app_get_user_detail_req req) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
		this.req = req;
	}

	public void run() {
		System.out.println("--------------开启查询线程（"+req.getUser_id()+"）---------------");
		try {
			lock.lock();
			resp = network.ios_interface_app_get_user_detail(req);
			if(resp == null){
				message = new Message();
				message.what = message_what_values.fragment_my_center_get_data_failed;
				handler.sendMessage(message);
			}else{
				// --
				message = new Message();
				message.what = message_what_values.fragment_my_center_get_user_detail_success;
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
