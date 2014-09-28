package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class listener_activity_chat_on_click implements OnClickListener {
	private Handler handler = null;
	private Message message = null;

	public listener_activity_chat_on_click(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try {
			message = new Message();
			if (v.getId() == R.id.activity_chat_button_option_text_message) {
				message.what = message_what_values.activity_interaction_chat_button_option_text_on_click;
			}
			if (v.getId() == R.id.activity_chat_button_option_voice_message) {
				message.what = message_what_values.activity_interaction_chat_button_option_voice_on_click;
			}
			if (v.getId() == R.id.activity_chat_button_option_image_message) {
				message.what = message_what_values.activity_interaction_chat_button_option_image_on_click;
			}
			if (v.getId() == R.id.activity_chat_button_add_image_from_camera) {
				message.what = message_what_values.activity_interaction_chat_button_option_image_from_camera_on_click;
			}
			if (v.getId() == R.id.activity_chat_button_add_image_from_local) {
				message.what = message_what_values.activity_interaction_chat_button_option_image_from_local_on_click;
			}
			if (v.getId() == R.id.activity_chat_button_add_image_from_inside) {
				message.what = message_what_values.activity_interaction_chat_button_option_image_from_inside_on_click;
			}
			if (v.getId() == R.id.activity_chat_button_send_text_message) {
				message.what = message_what_values.activity_interaction_chat_button_send_text_on_click;
			}
			if (v.getId() == R.id.widget_activity_main_action_bar_imagebutton_more) {
				message.what = message_what_values.widget_top_action_bar_button_more_click;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
