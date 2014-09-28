package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class listener_fragment_interaction_on_click implements OnClickListener {
	private Handler handler = null;
	private Message message = null;
	private int type = 0;

	public listener_fragment_interaction_on_click(Handler handler, int type) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.type = type;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try {
			message = new Message();
			if (v.getId() == R.id.fragment_interaction_button_dialogue) {
				message.what = message_what_values.fragment_menu_button_dialogue_on_click;
			}
			if (v.getId() == R.id.fragment_interaction_button_notice) {
				message.what = message_what_values.fragment_menu_button_notice_on_click;
			}
			if (v.getId() == R.id.fragment_interaction_button_news) {
				message.what = message_what_values.fragment_menu_button_news_on_click;
			}
			if (v.getId() == R.id.fragment_interaction_button_contacts) {
				message.what = message_what_values.fragment_menu_button_contacts_on_click;
			}
			if (v.getId() == R.id.widget_activity_main_action_bar_imagebutton_more) {
				message.what = message_what_values.widget_top_action_bar_button_more_click;
				message.arg1 = type;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
