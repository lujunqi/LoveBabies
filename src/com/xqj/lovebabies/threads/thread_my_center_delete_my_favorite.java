package com.xqj.lovebabies.threads;

import java.util.concurrent.locks.ReentrantLock;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.*;

import android.os.Handler;
import android.os.Message;

public class thread_my_center_delete_my_favorite extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Handler handler;
	private interface_app_delete_my_favorite_req req = null;
	private interface_app_delete_my_favorite_resp resp = null;
	private Message message = null;
	
	public thread_my_center_delete_my_favorite(Handler handler, interface_app_delete_my_favorite_req request){
		this.handler = handler;
		this.req = request;
	}
	
	public void run() {
		System.out.println("--------------开启删除我的收藏线程（"+req.getUser_id()+"）---------------");
		try {
			lock.lock();
			resp = network.interface_app_delete_my_favorite(req);
			if(resp == null){// 网络异常
				message = new Message();
				message.what = message_what_values.fragment_my_center_get_data_failed;
				handler.sendMessage(message);
			}else{
				message = new Message();
				message.what = message_what_values.activity_my_center_delete_my_favorite_success;
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
