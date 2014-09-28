package com.xqj.lovebabies.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.dbs_album_baby_growth;
import com.xqj.lovebabies.databases.dbs_health_information;
import com.xqj.lovebabies.databases.dbs_health_information_top;
import com.xqj.lovebabies.databases.table_album_baby_growth;
import com.xqj.lovebabies.databases.table_health_information;
import com.xqj.lovebabies.databases.table_health_information_top_pic_info;
import com.xqj.lovebabies.structures.interface_app_get_health_information_req;
import com.xqj.lovebabies.structures.interface_app_get_health_information_resp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class thread_health_get_information_by_page_list extends Thread {
	public static ReentrantLock lock = new ReentrantLock();
	private utils_network_interfaces network = new utils_network_interfaces();
	private Context context;
	private Handler handler;
	private interface_app_get_health_information_req req = null;
	private interface_app_get_health_information_resp resp = null;
	private Message message = null;
	private int new_data = 1;// 1 查询最新数据    0 查询历史记录
	private dbs_health_information_top db_health_information_top;
	private dbs_health_information db_health_information;
	
	public thread_health_get_information_by_page_list(Context context, Handler handler, 
			interface_app_get_health_information_req request, int new_data){
		this.context = context;
		this.handler = handler;
		this.req = request;
		this.new_data = new_data;
	}
	
	public void run() {
		System.out.println("--------------开启查询线程（"+req.getAction()+"）---------------");
		try {
			lock.lock();
			resp = network.ios_interface_app_get_health_infomation_by_page(req);
			if(resp == null){// 网络异常
				message = new Message();
				message.what = message_what_values.fragment_health_get_data_failed;
				handler.sendMessage(message);
			}else{
				List<table_health_information> list = resp.getList();
				List<table_health_information_top_pic_info> top_list = resp.getTop_list();
				list = list == null ? new ArrayList<table_health_information>() : list;
				top_list = top_list == null ? new ArrayList<table_health_information_top_pic_info>() : top_list;
				if(req.getAction()!=null && req.getAction().equals("top")){//头条
					db_health_information_top = new dbs_health_information_top(context);
					for (int i = 0; i < top_list.size(); i++) {
						table_health_information_top_pic_info health_top_info = top_list.get(i);
						if (!isin(health_top_info, db_health_information_top)) {
							db_health_information_top.do_insert_data(health_top_info);
						}
					}
				}else{//普通资讯
					db_health_information = new dbs_health_information(context);
					for (int i = 0; i < list.size(); i++) {
						table_health_information health_info = list.get(i);
						if (!isin(health_info, db_health_information)) {
							db_health_information.do_insert_data(health_info);
						}
					}
				}
				// --
				message = new Message();
				message.what = message_what_values.fragment_health_get_information_list_success;
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
	//判断普通健康育儿资讯是否已经存在
	private boolean isin(table_health_information n, dbs_health_information dbs) {
		try {
			List<table_health_information> list = dbs.do_select_data("select * from t_health_information where content_id='" + n.getContent_id() + "'");
			list = list == null ? new ArrayList<table_health_information>() : list;
			if (list.get(0) != null) { return true; }
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	//判断头条健康育儿资讯是否已经存在
	private boolean isin(table_health_information_top_pic_info n, dbs_health_information_top dbs) {
		try {
			List<table_health_information_top_pic_info> list = dbs.do_select_data("select * from t_health_information_top where top_id='" + n.getTop_id() + "'");
			list = list == null ? new ArrayList<table_health_information_top_pic_info>() : list;
			if (list.get(0) != null) { return true; }
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
}
