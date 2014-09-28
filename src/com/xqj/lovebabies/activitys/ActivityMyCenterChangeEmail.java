package com.xqj.lovebabies.activitys;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_update_user_detail_req;
import com.xqj.lovebabies.threads.thread_my_center_update_user_detail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMyCenterChangeEmail extends Activity {

	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	private EditText user_email_edittext;
	
	private String user_id;
	private String user_email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_change_email);
		
		user_email_edittext = (EditText)findViewById(R.id.my_center_change_email_edittext);
		user_id = getIntent().getStringExtra("user_id");
		user_email = getIntent().getStringExtra("user_email");
		user_email_edittext.setText(user_email);
		init_top_bar();
	}

	/**
	 * 初始化头部
	 */
	private void init_top_bar(){
		head_btn_right = (ImageView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		
		head_title.setText("邮箱");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(ActivityPersonalInfo.RESULT_CANCEL);
				ActivityMyCenterChangeEmail.this.finish();
			}
		});
		head_btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				f_update_user_email();
			}
		});
	}
	
	//--------------  网络交互部分  -----------------------
	private Handler network_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_update_user_detail_success){
				Toast.makeText(ActivityMyCenterChangeEmail.this, "保存成功", Toast.LENGTH_SHORT).show();
				Intent result=new Intent(null,Uri.parse(user_email_edittext.getText().toString().trim()));
				// 这就是返回的信息了，前者结果码，后者返回的数据。
				setResult(ActivityPersonalInfo.RESULT_CHANGE_EMAIL, result);
				ActivityMyCenterChangeEmail.this.finish();
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				setResult(ActivityPersonalInfo.RESULT_CANCEL);
				Toast.makeText(ActivityMyCenterChangeEmail.this, "保存失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private int f_update_user_email(){
		String new_email = user_email_edittext.getText().toString();
		if(new_email==null || new_email.length()==0){
			Toast.makeText(this, "请输入邮箱地址", Toast.LENGTH_SHORT).show();
			return -1;
		}else if(user_id==null || user_id.length()==0){
			Toast.makeText(this, "获取用户信息失败，请重新登录", Toast.LENGTH_SHORT).show();
			return -1;
		}else{
			interface_app_update_user_detail_req req = new interface_app_update_user_detail_req();
			req.setUser_email(new_email);
			req.setUser_id(user_id);
			new thread_my_center_update_user_detail(this, network_handler, req).start();
			return 0;
		}
	}
}
