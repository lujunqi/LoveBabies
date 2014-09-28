package com.xqj.lovebabies.listeners;

import com.xqj.lovebabies.contants.message_what_values;

import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.Animation.*;

public class listener_activity_logo_animation implements AnimationListener {
	private Handler handler = null;
	private Message message = null;

	public listener_activity_logo_animation(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		try {
			message = new Message();
			message.what = message_what_values.activity_logo_animation_end;
			handler.sendMessage(message);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

}
