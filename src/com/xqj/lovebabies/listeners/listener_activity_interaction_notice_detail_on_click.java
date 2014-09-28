package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.*;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.*;

public class listener_activity_interaction_notice_detail_on_click implements OnClickListener {
	private Handler handler = null;
	private Message message = null;
	private String content = null;

	public listener_activity_interaction_notice_detail_on_click(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		message = new Message();
		if (v.getId() == R.id.widget_activity_main_action_bar_imagebutton_menu) {
			message.what = message_what_values.widget_top_action_bar_button_menu_click;
		}
		if (v.getId() == R.id.activity_interaction_notice_detail_button_praise) {
			message.what = message_what_values.activity_interaction_notice_detail_button_praise_on_click;
		}
		if (v.getId() == R.id.activity_interaction_notice_detail_button_comment) {
			message.what = message_what_values.activity_interaction_notice_detail_button_comment_on_click;
		}
		if (v.getId() == R.id.activity_interaction_notice_detail_dialog_submit) {
			message.what = message_what_values.activity_interaction_notice_detail_button_submit_comment_on_click;		
		}
		if (v.getId() == R.id.activity_interaction_notice_detail_dialog_reset) {
			message.what = message_what_values.activity_interaction_notice_detail_button_reset_comment_on_click;
		}
		handler.sendMessage(message);
	}

}
