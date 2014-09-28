package com.xqj.lovebabies;

import com.xqj.lovebabies.commons.CrashHandler;

import android.app.Application;

public class BaseApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

}
