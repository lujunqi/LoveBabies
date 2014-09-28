package com.xqj.lovebabies.activitys;

import com.xqj.lovebabies.commons.CrashHandler;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		super.onCreate(savedInstanceState);
	}

}
