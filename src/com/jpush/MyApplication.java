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

		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		JPushInterface.setAlias(this, "1234", null);
		
		
	}
}
