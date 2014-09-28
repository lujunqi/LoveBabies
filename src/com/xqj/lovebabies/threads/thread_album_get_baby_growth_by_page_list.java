package com.xqj.lovebabies.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.dbs_album_baby_growth;
import com.xqj.lovebabies.databases.table_album_baby_growth;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_by_page_req;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_by_page_resp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class thread_album_get_baby_growth_by_page_list extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Context context;
	private Handler handler;
	private interface_app_get_baby_growth_by_page_req req = null;
	private interface_app_get_baby_growth_by_page_resp resp = null;
	private Message message = null;
	private int new_data = 1;// 1 查询最新数据    0 查询历史记录
	private dbs_album_baby_growth dbs_baby_growth;
	private table_album_baby_growth t_baby_growth;
	
	public thread_album_get_baby_growth_by_page_list(Context context, Handler handler, 
			interface_app_get_baby_growth_by_page_req request, int new_data){
		this.context = context;
		this.handler = handler;
		this.req = request;
		this.new_data = new_data;
	}
	
	public void run() {
		System.out.println("--------------开启查询分页成长记录线程（"+req.getUser_id()+"）---------------");
		try {
			lock.lock();
			resp = network.ios_interface_app_get_baby_growth_by_page(req);
			if(resp == null){// 网络异常
				message = new Message();
				message.what = message_what_values.activity_album_get_data_timeout;
				handler.sendMessage(message);
			}else{
				List<table_album_baby_growth> list = resp.getList();
				list = list == null ? new ArrayList<table_album_baby_growth>() : list;
				dbs_baby_growth = new dbs_album_baby_growth(context);
				for (int i = 0; i < list.size(); i++) {
					t_baby_growth = list.get(i);
					System.out.println(t_baby_growth.getBaby_id()+
							"--------------Thread----------------"+t_baby_growth.getRecord_id()+"--"+
							t_baby_growth.getWord_record());
					if (!isin(t_baby_growth, dbs_baby_growth)) {
						dbs_baby_growth.do_insert_data(t_baby_growth);
					}
				}
				// --
				message = new Message();
				message.what = message_what_values.activity_album_baby_growth_by_page_get_data_success;
				message.obj = resp;
				message.arg1 = new_data;
				handler.sendMessage(message);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			lock.unlock();
		}
	}
	
	private boolean isin(table_album_baby_growth n, dbs_album_baby_growth dbs) {
		try {
			List<table_album_baby_growth> list = dbs.do_select_data("select * from t_album_baby_growth where record_id='" + n.getRecord_id() + "'");
			list = list == null ? new ArrayList<table_album_baby_growth>() : list;
			if (list.get(0) != null) { return true; }
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
}
