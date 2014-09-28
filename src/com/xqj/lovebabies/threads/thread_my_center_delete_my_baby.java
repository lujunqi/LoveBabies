package com.xqj.lovebabies.threads;

import java.util.concurrent.locks.ReentrantLock;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.*;

import android.os.Handler;
import android.os.Message;

public class thread_my_center_delete_my_baby extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Handler handler;
	private interface_app_delete_baby_req req = null;
	private interface_app_delete_baby_resp resp = null;
	private Message message = null;
	
	public thread_my_center_delete_my_baby(Handler handler, interface_app_delete_baby_req request){
		this.handler = handler;
		this.req = request;
	}
	
	public void run() {
		System.out.println("--------------¿ªÆôÉ¾³ı±¦±¦Ïß³Ì£¨"+req.getUser_id()+"£©---------------");
		try {
			lock.lock();
			resp = network.interface_app_delete_baby(req);
			if(resp == null){// ÍøÂçÒì³£
				message = new Message();
				message.what = message_what_values.fragment_my_center_get_data_failed;
				handler.sendMessage(message);
			}else{
				message = new Message();
				message.what = message_what_values.activity_my_center_delete_baby_success;
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
