package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.*;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

public class listener_activity_interaction_notice_detail_comment_item_button_delete_on_click implements OnClickListener {
	private int index = 0;
	private Handler handler = null;
	private Message message = null;
	private table_interaction_notice_comment t_interaction_notice_comment = null;

	public listener_activity_interaction_notice_detail_comment_item_button_delete_on_click(Handler handler, table_interaction_notice_comment comment, int index) {
		// TODO Auto-generated constructor stub
		this.t_interaction_notice_comment = comment;
		this.handler = handler;
		this.index = index;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.activity_interaction_notice_detail_comment_item_delete) {
			message = new Message();
			message.what = message_what_values.activity_interaction_notice_detail_comment_delete_on_click;
			message.obj = t_interaction_notice_comment;
			message.arg1 = index;
			handler.sendMessage(message);
		}
	}
}
