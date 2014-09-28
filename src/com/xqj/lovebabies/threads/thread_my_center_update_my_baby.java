package com.xqj.lovebabies.threads;

import java.util.concurrent.locks.ReentrantLock;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_add_and_set_my_baby_req;
import com.xqj.lovebabies.structures.interface_app_add_and_set_my_baby_resp;
import android.os.Handler;
import android.os.Message;

public class thread_my_center_update_my_baby extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Handler handler;
	private interface_app_add_and_set_my_baby_req req = null;
	private interface_app_add_and_set_my_baby_resp resp = null;
	private Message message = null;
	
	public thread_my_center_update_my_baby(Handler handler, interface_app_add_and_set_my_baby_req request){
		this.handler = handler;
		this.req = request;
	}
	
	public void run() {
		System.out.println("--------------�����޸ı����̣߳�"+req.getUser_id()+"��---------------");
		try {
			lock.lock();
			resp = network.ios_interface_app_modify_baby(req);
			if(resp == null){// �����쳣
				message = new Message();
				message.what = message_what_values.fragment_my_center_get_data_failed;
				handler.sendMessage(message);
			}else{
				message = new Message();
				message.what = message_what_values.activity_my_center_update_baby_success;
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
