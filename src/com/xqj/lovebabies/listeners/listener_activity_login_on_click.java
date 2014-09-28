package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class listener_activity_login_on_click implements OnClickListener {
	private Handler handler = null;
	private Message message = null;

	public listener_activity_login_on_click(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try {
			message = new Message();
			if (v.getId() == R.id.activity_login_button_login) {
				message.what = message_what_values.activity_login_button_login_on_click;
			}
			if (v.getId() == R.id.activity_login_button_visitor) {
				message.what = message_what_values.activity_login_button_visitor_on_click;
			}
			if (v.getId() == R.id.activity_login_textview_register_user) {
				message.what = message_what_values.activity_login_button_register_user_on_click;
			}
			if (v.getId() == R.id.activity_login_textview_find_password) {
				message.what = message_what_values.activity_login_button_find_password_on_click;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
