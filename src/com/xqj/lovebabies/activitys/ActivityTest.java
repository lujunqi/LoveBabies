package com.xqj.lovebabies.activitys;

import com.xqj.lovebabies.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ActivityTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atest);
		System.out.println("activity_atest ...");
		Button btn = (Button)findViewById(R.id.test_btn);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("onClick ...");
				Log.v("TEST", "onClick ...");
			}
		});
		btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				System.out.println("onTouch ...");
				Log.v("TEST", "onTouch ...");
				return false;
			}
		});
		btn.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("onLongClick...");
				Log.v("TEST", "onLongClick ...");
				return false;
			}
		});
	}

}
