package com.xqj.lovebabies.threads;

import android.os.Handler;
import android.os.Message;
import cn.trinea.android.common.util.StringUtils;

import com.xqj.lovebabies.commons.utils_network_interfaces;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.*;

public class thread_upload_media_files extends Thread {
	private utils_network_interfaces network = new utils_network_interfaces();
	private interface_app_upload_file_req req = null;
	private interface_app_upload_file_resp resp = null;
	private Handler handler = null;
	private Message message = null;

	public thread_upload_media_files(Handler handler, interface_app_upload_file_req req) {
		this.handler = handler;
		this.req = req;
	}

	public void run() {
		try {
			resp = network.ios_interface_app_upload_media_file(req);
			resp = resp == null ? new interface_app_upload_file_resp() : resp;
			message = new Message();

			if (StringUtils.isEquals("voice", req.getUpload_file_type())) {
				message.what = message_what_values.activity_interaction_chat_message_send_voice_failure;
				if (StringUtils.isEquals(resp.getReturn_str(), "The data submitted to complete!")) {
					message.what = message_what_values.activity_interaction_chat_message_send_voice_success;
				}

			} else if (StringUtils.isEquals("picture", req.getUpload_file_type())) {
				message.what = message_what_values.activity_interaction_chat_message_send_image_failure;
				if (StringUtils.isEquals(resp.getReturn_str(), "The data submitted to complete!")) {
					message.what = message_what_values.activity_interaction_chat_message_send_image_success;
				}
			}

			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
