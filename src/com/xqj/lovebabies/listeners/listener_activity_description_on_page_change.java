package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.contants.message_what_values;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.*;

public class listener_activity_description_on_page_change implements OnPageChangeListener {
	private Handler handler = null;
	private Message message = null;

	public listener_activity_description_on_page_change(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		try {
			message = new Message();
			if (arg0 >= 3) {
				message.what = message_what_values.activity_description_viewpager_container_page_change_show_enter_button;
			} else {
				message.what = message_what_values.activity_description_viewpager_container_page_change_hide_enter_button;
			}
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
