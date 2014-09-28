package com.xqj.lovebabies.threads;

import java.util.concurrent.locks.ReentrantLock;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.*;

import android.os.Handler;
import android.os.Message;

public class thread_album_delete_baby_growth_comment extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Handler handler;
	private interface_app_delete_baby_growth_comment_req req = null;
	private interface_app_delete_baby_growth_comment_resp resp = null;
	private Message message = null;
	
	public thread_album_delete_baby_growth_comment(Handler handler, interface_app_delete_baby_growth_comment_req request){
		this.handler = handler;
		this.req = request;
	}
	
	public void run() {
		System.out.println("--------------开启删除宝宝成长记录评论线程（"+req.getUser_id()+"）---------------");
		try {
			lock.lock();
			resp = network.interface_app_delete_baby_growth_comment(req);
			if(resp == null){// 网络异常
				message = new Message();
				message.what = message_what_values.activity_album_get_data_timeout;
				handler.sendMessage(message);
			}else{
				message = new Message();
				message.what = message_what_values.activity_delete_baby_growth_comment_success;
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
