package com.xqj.lovebabies.listeners;

import java.util.List;

import com.xqj.lovebabies.contants.message_what_values;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.*;
import android.widget.Button;

public class listener_fragment_interaction_on_page_change implements OnPageChangeListener {
	private Handler handler = null;
	private Message message = null;

	public listener_fragment_interaction_on_page_change(Handler handler) {
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
			message.what = message_what_values.fragment_interaction_viewpager_on_change;
			message.arg1 = arg0;
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
