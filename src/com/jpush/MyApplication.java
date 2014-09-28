package com.jpush;

import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
	private static final String TAG = "JPush";

	@Override
	public void onCreate() {
		Log.d(TAG, "[MyApplication] onCreate");
		super.onCreate();

		JPushInterface.setDebugMode(true); // ���ÿ�����־,����ʱ��ر���־
		JPushInterface.init(this); // ��ʼ�� JPush
		JPushInterface.setAlias(this, "1234", null);
		
		
	}
}
