package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.*;

public class listener_activity_interaction_news_detail_on_click implements OnClickListener {
	private Handler handler = null;
	private Message message = null;

	public listener_activity_interaction_news_detail_on_click(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		try {
			message = new Message();
			if (arg0.getId() == R.id.widget_activity_main_action_bar_imagebutton_menu) {
				message.what = message_what_values.widget_top_action_bar_button_menu_click;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
