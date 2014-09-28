package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.contants.message_what_values;

import cn.trinea.android.common.util.StringUtils;
import android.os.*;
import android.text.*;

public class listener_activity_chat_text_message_text_change implements TextWatcher {
	private Handler handler = null;
	private Message message = null;

	public listener_activity_chat_text_message_text_change(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		try {
			message = new Message();
			message.what = message_what_values.activity_interaction_chat_edittext_text_on_change;
			if (StringUtils.isBlank(s.toString())) {
				message.arg1 = 0;
			} else {
				message.arg1 = 1;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

}
