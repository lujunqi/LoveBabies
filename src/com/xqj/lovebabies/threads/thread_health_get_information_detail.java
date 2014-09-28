package com.xqj.lovebabies.threads;

import java.util.concurrent.locks.ReentrantLock;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_get_health_information_detail_req;
import com.xqj.lovebabies.structures.interface_app_get_health_information_detail_resp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class thread_health_get_information_detail extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Context context;
	private Handler handler;
	private interface_app_get_health_information_detail_req req = null;
	private interface_app_get_health_information_detail_resp resp = null;
	private Message message = null;
	
	public thread_health_get_information_detail(Context context, Handler handler, 
			interface_app_get_health_information_detail_req request){
		this.context = context;
		this.handler = handler;
		this.req = request;
	}
	
	public void run() {
		System.out.println("--------------开启查询健康资讯详情线程（"+req.getId()+","+req.getUser_id()+"）---------------");
		try {
			lock.lock();
			resp = network.ios_interface_app_get_health_infomation_detail(req);
			if(resp == null){// 网络异常
				message = new Message();
				message.what = message_what_values.fragment_health_get_data_failed;
				handler.sendMessage(message);
			}else{
				// --
				message = new Message();
				message.what = message_what_values.fragment_health_get_information_detail_success;
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
