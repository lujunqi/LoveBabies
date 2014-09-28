package com.xqj.lovebabies.threads;

import com.xqj.lovebabies.contants.*;

import android.os.Handler;
import android.os.Message;

public class thread_interaction_update_voice_amp_indicator extends Thread {
	public static boolean thread_status = true;
	private Handler handler = null;
	private Message message = null;

	public thread_interaction_update_voice_amp_indicator(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	public void run() {
		for (;;) {
			try {
				if (thread_status) {
					message = new Message();
					message.what = message_what_values.activity_interaction_chat_message_update_voice_amplitude;
					handler.sendMessage(message);
				} else {
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				try {
					Thread.sleep(100);
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
	}
}
