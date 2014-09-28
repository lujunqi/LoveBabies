package com.xqj.lovebabies.activitys;

import com.xqj.lovebabies.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ע�����
 * 
 * @author sunshine
 *
 */
public class ActivityRegisterFinished extends Activity {
	// topbar
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;

	private TextView register_success_textview;
	private Button finish_btn;

	private String new_account;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register_finished);

		new_account = getIntent().getStringExtra("new_account");
		init_top_bar();
		init_main_page();
	}

	/**
	 * ��ʼ��ͷ��
	 */
	private void init_top_bar() {
		head_btn_right = (ImageView) findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView) findViewById(R.id.head_left_imageview);
		head_title = (TextView) findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		head_title.setText("ע��");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityRegisterFinished.this, ActivityLogin.class);
				intent.putExtra("new_account", new_account);
				ActivityRegisterFinished.this.startActivity(intent);
				ActivityRegisterFinished.this.finish();
			}
		});
	}

	/**
	 * ��ʼ��������
	 */
	private void init_main_page() {
		register_success_textview = (TextView) findViewById(R.id.register_success_textview);
		finish_btn = (Button) findViewById(R.id.register_finish_btn);
		finish_btn.setOnClickListener(new OnClickListener() {// ע����ɣ���ת����½ҳ��
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setClass(ActivityRegisterFinished.this, ActivityLogin.class);
						intent.putExtra("new_account", new_account);
						ActivityRegisterFinished.this.startActivity(intent);
						ActivityRegisterFinished.this.finish();
					}
				});
	}
}
