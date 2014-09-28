package com.xqj.lovebabies.listeners;

import android.os.Handler;
import android.os.Message;

import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.widgets.ContactsSideBar.OnTouchingLetterChangedListener;

public class listener_fragment_interaction_contacts_sidebar_on_letter_change implements OnTouchingLetterChangedListener {
	private Handler handler = null;
	private Message message = null;

	public listener_fragment_interaction_contacts_sidebar_on_letter_change(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onTouchingLetterChanged(String s) {
		// TODO Auto-generated method stub
		try {
			message = new Message();
			message.what = message_what_values.fragment_interaction_contacts_sidebar_on_letter_change;
			message.obj = s;
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
