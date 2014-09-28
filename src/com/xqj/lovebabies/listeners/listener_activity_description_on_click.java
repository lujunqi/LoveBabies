package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class listener_activity_description_on_click implements OnClickListener {
	private Handler handler = null;
	private Message message = null;

	public listener_activity_description_on_click(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.activity_description_imagebutton_enter) {
			message = new Message();
			message.what = message_what_values.activity_description_imagebutton_enter_click;
			handler.sendMessage(message);
		}
	}
}
