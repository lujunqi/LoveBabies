package com.xqj.lovebabies.threads;

import java.util.concurrent.locks.ReentrantLock;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.*;

import android.os.Handler;
import android.os.Message;

public class thread_album_delete_baby_growth extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Handler handler;
	private interface_app_delete_baby_growth_req req = null;
	private interface_app_delete_baby_growth_resp resp = null;
	private Message message = null;
	
	public thread_album_delete_baby_growth(Handler handler, interface_app_delete_baby_growth_req request){
		this.handler = handler;
		this.req = request;
	}
	
	public void run() {
		System.out.println("--------------����ɾ�������ɳ���¼�̣߳�"+req.getUser_id()+"��---------------");
		try {
			lock.lock();
			resp = network.interface_app_delete_baby_growth(req);
			if(resp == null){// �����쳣
				message = new Message();
				message.what = message_what_values.activity_delete_baby_growth_failed;
				handler.sendMessage(message);
			}else{
				resp.setGrowth_id(req.getRecord_id());
				message = new Message();
				message.what = message_what_values.activity_delete_baby_growth_success;
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
