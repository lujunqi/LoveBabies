package com.xqj.lovebabies.activitys;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_check_and_regist_user_req;
import com.xqj.lovebabies.structures.interface_app_check_and_regist_user_resp;
import com.xqj.lovebabies.structures.interface_app_check_verify_code_req;
import com.xqj.lovebabies.structures.interface_app_check_verify_code_resp;
import com.xqj.lovebabies.structures.interface_app_get_verify_code_req;
import com.xqj.lovebabies.structures.interface_app_get_verify_code_resp;
import com.xqj.lovebabies.threads.thread_my_center_check_and_regist_user;
import com.xqj.lovebabies.threads.thread_my_center_check_verify_code;
import com.xqj.lovebabies.threads.thread_my_center_get_verify_code;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityResetPassword extends Activity {
	public static final int REGISTER_SUCCESS = 10001;
	public static final int READ_RULE_CHECK = 10002;
	// topbar
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;

	// 主界面

	private EditText enter_phone_edittext;

	private Button submit_phone_btn;

	private String phone_number = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reset_password);

		init_top_bar();
		init_main_page();
	}

	/**
	 * 初始化头部
	 */
	private void init_top_bar() {
		head_btn_right = (ImageView) findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView) findViewById(R.id.head_left_imageview);
		head_title = (TextView) findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		head_title.setText("找回密码");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityResetPassword.this, ActivityLogin.class);
				ActivityResetPassword.this.startActivity(intent);
				ActivityResetPassword.this.finish();
			}
		});
	}

	/**
	 * 初始化主界面
	 */
	private void init_main_page() {
		enter_phone_edittext = (EditText) findViewById(R.id.reset_pwd_enter_phone_edittext);
		submit_phone_btn = (Button) findViewById(R.id.reset_pwd_get_code_btn);
		submit_phone_btn.setOnClickListener(new OnClickListener() {// 获取验证码
					@Override
					public void onClick(View arg0) {
						phone_number = enter_phone_edittext.getText().toString();
						f_get_code();// 进入第二步
					}
				});
	}

	/**
	 * ************* 网络交互部分 ***************
	 */
	private Handler network_handler = new Handler() {
		@Override
		public void handleMessage(Message msg){
			if (msg.what == message_what_values.activity_my_center_get_verify_code_success) {
				interface_app_get_verify_code_resp resp = (interface_app_get_verify_code_resp) msg.obj;
				if (resp != null) {
					if (resp.getVerifyCode().equals("1")) {// 发送成功，进入第二步
						Intent intent = new Intent();
						intent.setClass(ActivityResetPassword.this, ActivityResetPasswordCheckCode.class);
						intent.putExtra("phone_number", phone_number);
						ActivityResetPassword.this.startActivity(intent);
						ActivityResetPassword.this.finish();
					} else if (resp.getVerifyCode().equals("-3")) {
						Toast.makeText(ActivityResetPassword.this, "验证码保存失败", Toast.LENGTH_SHORT).show();
					} else if (resp.getVerifyCode().equals("-1")) {
						Toast.makeText(ActivityResetPassword.this, "电话号码已被注册", Toast.LENGTH_SHORT).show();
					} else if (resp.getVerifyCode().equals("-2")) {
						Toast.makeText(ActivityResetPassword.this, "输入的电话号码无效", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(ActivityResetPassword.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
				}
			} else if (msg.what == message_what_values.fragment_my_center_get_data_failed) {
				Toast.makeText(ActivityResetPassword.this, "查询失败", Toast.LENGTH_SHORT).show();
			}
		}
	};

	// 提交手机号，获取验证码
	private int f_get_code() {
		if (phone_number == null || phone_number.length() == 0) {
			Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
			return -1;
		}
		// 进入下一步,获取验证码
		interface_app_get_verify_code_req req = new interface_app_get_verify_code_req();
		req.setPhone(phone_number);
		req.setReason("2");// 1- 注册 2-找回密码 3- 更换手机号
		new thread_my_center_get_verify_code(network_handler, req).start();
		return 0;
	}

}
