package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.*;

public class listener_activity_chat_button_on_touch implements OnTouchListener, OnLongClickListener {
	private Handler handler = null;
	private Message message = null;

	public listener_activity_chat_button_on_touch(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		try {
			message = new Message();
			if (event.getAction() == MotionEvent.ACTION_UP) {
				message.what = message_what_values.activity_interaction_chat_button_send_voice_on_touch;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		try {
			message = new Message();
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

}
