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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMyCenterChangeNickname extends Activity {
	
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	private EditText user_nick_edittext;
	
	private String user_id;
	private String nick_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_change_nickname);
		
		user_nick_edittext = (EditText)findViewById(R.id.my_center_change_nick_edittext);
		
		user_id = getIntent().getStringExtra("user_id");
		nick_name = getIntent().getStringExtra("user_nick");
		user_nick_edittext.setText(nick_name);
		init_top_bar();
	}
	
	/**
	 * 初始化头部
	 */
	private void init_top_bar(){
		head_btn_right = (ImageView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		
		head_title.setText("修改昵称");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(ActivityPersonalInfo.RESULT_CANCEL);
				ActivityMyCenterChangeNickname.this.finish();
			}
		});
		head_btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				f_update_user_nick();
			}
		});
	}

	//--------------  网络交互部分  -----------------------
	private Handler network_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_update_user_detail_success){
				Toast.makeText(ActivityMyCenterChangeNickname.this, "保存成功", Toast.LENGTH_SHORT).show();
				Intent result=new Intent(null,Uri.parse(user_nick_edittext.getText().toString().trim()));
				// 这就是返回的信息了，前者结果码，后者返回的数据。
				setResult(ActivityPersonalInfo.RESULT_CHANGE_NICK_NAME, result);
				ActivityMyCenterChangeNickname.this.finish();
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(ActivityMyCenterChangeNickname.this, "保存失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private int f_update_user_nick(){
		String new_nick_name = user_nick_edittext.getText().toString();
		System.out.println("f_update_user_nick----------new_nick_name:"+new_nick_name);
		if(new_nick_name==null || new_nick_name.length()==0){
			Toast.makeText(this, "请输入您的昵称", Toast.LENGTH_SHORT).show();
			return -1;
		}else if(user_id==null || user_id.length()==0){
			Toast.makeText(this, "获取用户信息失败，请重新登录", Toast.LENGTH_SHORT).show();
			return -1;
		}else{
			interface_app_update_user_detail_req req = new interface_app_update_user_detail_req();
			req.setNick_name(new_nick_name);
			req.setUser_id(user_id);
			new thread_my_center_update_user_detail(this, network_handler, req).start();
			return 0;
		}
	}
}
