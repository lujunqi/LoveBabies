package com.xqj.lovebabies.threads;

import android.content.Context;
import com.xqj.lovebabies.structures.*;

public class thread_interaction_get_notice_receiver extends Thread {
	private interface_app_get_notice_receiver_list_req get_notice_receiver_list_req = null;
	private interface_app_get_notice_receiver_list_resp get_notice_receiver_list_resp = null;
	private Context context = null;

	public thread_interaction_get_notice_receiver(Context context, interface_app_get_notice_receiver_list_req get_notice_receiver_list_req) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.get_notice_receiver_list_req = get_notice_receiver_list_req;
	}

	public void run() {
		try {
						
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
