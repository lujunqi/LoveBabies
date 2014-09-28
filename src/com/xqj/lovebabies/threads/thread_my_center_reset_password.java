package com.xqj.lovebabies.threads;

import java.util.concurrent.locks.ReentrantLock;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_reset_password_req;
import com.xqj.lovebabies.structures.interface_app_reset_password_resp;

import android.os.Handler;
import android.os.Message;

/**
 * 重置密码
 *
 */
public class thread_my_center_reset_password extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Handler handler;
	private interface_app_reset_password_req req = null;
	private interface_app_reset_password_resp resp = null;
	private Message message = null;
	
	public thread_my_center_reset_password(Handler handler, interface_app_reset_password_req request){
		this.handler = handler;
		this.req = request;
	}
	
	public void run() {
		System.out.println("--------------开启验证验证码线程（"+req.getPhone()+"）---------------");
		try {
			lock.lock();
			resp = network. ios_interface_app_reset_password(req);
			if(resp == null){// 网络异常
				message = new Message();
				message.what = message_what_values.fragment_my_center_get_data_failed;
				handler.sendMessage(message);
			}else{
				message = new Message();
				message.what = message_what_values.activity_my_center_reset_password_success;
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
