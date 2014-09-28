package com.xqj.lovebabies.threads;

import java.util.concurrent.locks.ReentrantLock;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_get_invite_code_req;
import com.xqj.lovebabies.structures.interface_app_get_invite_code_resp;
import com.xqj.lovebabies.structures.interface_app_invite_baby_req;
import com.xqj.lovebabies.structures.interface_app_invite_baby_resp;

import android.os.Handler;
import android.os.Message;

/**
 * 输入验证码添加宝宝
 * @author sunshine
 *
 */
public class thread_my_center_invite_baby_code extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Handler handler;
	private interface_app_invite_baby_req req = null;
	private interface_app_invite_baby_resp resp = null;
	private Message message = null;
	
	public thread_my_center_invite_baby_code(Handler handler, interface_app_invite_baby_req request){
		this.handler = handler;
		this.req = request;
	}
	
	public void run() {
		System.out.println("--------------开启输入验证码添加宝宝线程（"+req.getUser_id()+"）---------------");
		try {
			lock.lock();
			resp = network.ios_interface_app_invite_baby_code(req);
			if(resp == null){// 网络异常
				message = new Message();
				message.what = message_what_values.fragment_my_center_get_data_failed;
				handler.sendMessage(message);
			}else{
				message = new Message();
				message.what = message_what_values.activity_my_center_invite_baby_success;
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
