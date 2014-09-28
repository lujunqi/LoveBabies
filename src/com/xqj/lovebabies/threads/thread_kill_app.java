package com.xqj.lovebabies.threads;

import android.app.Activity;

import com.xqj.lovebabies.commons.utils_common_tools;

public class thread_kill_app extends Thread {

	private static thread_kill_app instance = null;
	private kill_app_listener listener = null;
	private boolean bl_start_time = false;// ��ʼ��ʱ
	private int time_count = 0;// ��ʱ
	private final int WAIT_TIME = 3*60;//3����
	private Activity activity;

	public static thread_kill_app getInstance() {
		if (instance == null) {
			instance = new thread_kill_app();
		}
		return instance;
	}
	
	

	@Override
	public void run() {
		try {
			for (;;) {
				if(!isOnTop()){
					//�������ת���̨����ʼ��ʱ
					bl_start_time = true;
				}else{
					bl_start_time = false;
				}
				if (bl_start_time) {// ��ʼ��ʱ
					time_count++;
//					System.out.println("time_count-->" + time_count);
					if (time_count < WAIT_TIME) {
						Thread.sleep(1000);
					} else {
						if (listener != null) {
							System.out.println("ɱ�����̣�listener.kill_app()");
							listener.kill_app();
						}
						break;
					}
				} else {// ����ǰ̨����
//					System.out.println("����ǰ̨����");
					time_count = 0;
					Thread.sleep(1000);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * �жϳ����Ƿ���ǰ̨����
	 * @return
	 */
	private boolean isOnTop(){
		if(activity==null){
			return false;
		}else{
			return utils_common_tools.isTopActivity(activity);
		}
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}



	public boolean isBl_start_time() {
		return bl_start_time;
	}

	public void setBl_start_time(boolean bl_start_time) {
		this.bl_start_time = bl_start_time;
	}

	public kill_app_listener getListener() {
		return listener;
	}

	public void setListener(kill_app_listener listener) {
		this.listener = listener;
	}

	public interface kill_app_listener {
		void kill_app();
	}
	
	
}
