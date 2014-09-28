package com.xqj.lovebabies.activitys;

import com.xqj.lovebabies.R;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityMyCenterSettingAboutUs extends Activity {

	// topBar
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	// main page
	private TextView company_web_textview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_setting_aboutus);
		
		init_top_bar();
		init_main_page();
	}
	
	/**
	 * 初始化头部
	 */
	private void init_top_bar(){
		head_btn_right = (ImageView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		
		head_title.setText("关于我们");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityMyCenterSettingAboutUs.this.finish();
			}
		});
	}
	
	// 初始化 main  page
	private void init_main_page(){
		company_web_textview = (TextView)findViewById(R.id.my_center_about_us_company_web_textview);
		company_web_textview.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );//底部加横线
	}
}
