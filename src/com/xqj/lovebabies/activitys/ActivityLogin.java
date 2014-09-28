package com.xqj.lovebabies.activitys;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.StringUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.listeners.listener_activity_login_on_click;
import com.xqj.lovebabies.structures.interface_app_user_login_req;
import com.xqj.lovebabies.structures.interface_app_user_login_resp;
import com.xqj.lovebabies.threads.thread_common_system_user_login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityLogin extends Activity {
	private utils_common_tools tools = new utils_common_tools();
	private EditText cmd_edittext_user_name = null;
	private EditText cmd_edittext_user_password = null;
	private Button cmd_button_login = null;
	private Button cmd_button_visitor = null;
	private TextView cmd_textview_register_user = null;
	private TextView cmd_textciew_find_password = null;
	private interface_app_user_login_req app_user_login_req = null;
	private interface_app_user_login_resp app_user_login_resp = null;

	private String user_account = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setContentView(R.layout.activity_login);
			
			user_account = getIntent().getStringExtra("new_account");
			if(user_account==null || user_account.length()==0){
				user_account = PreferencesUtils.getString(this, "user_name");
			}
			user_account = user_account==null?"":user_account;
			init();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void init() {
		try {
			cmd_edittext_user_name = (EditText) findViewById(R.id.activity_login_edittext_user_name);
			cmd_edittext_user_password = (EditText) findViewById(R.id.activity_login_edittext_user_password);
			cmd_button_login = (Button) findViewById(R.id.activity_login_button_login);
			cmd_button_login.setOnClickListener(new listener_activity_login_on_click(handler));
			cmd_button_visitor = (Button) findViewById(R.id.activity_login_button_visitor);
			cmd_button_visitor.setOnClickListener(new listener_activity_login_on_click(handler));
			cmd_textview_register_user = (TextView) findViewById(R.id.activity_login_textview_register_user);
			cmd_textview_register_user.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			cmd_textview_register_user.setOnClickListener(new listener_activity_login_on_click(handler));
			cmd_textciew_find_password = (TextView) findViewById(R.id.activity_login_textview_find_password);
			cmd_textciew_find_password.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			cmd_textciew_find_password.setOnClickListener(new listener_activity_login_on_click(handler));
			if(user_account!=null && user_account.length()>0){
				cmd_edittext_user_name.setText(user_account);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == message_what_values.activity_login_button_find_password_on_click) {
				f_action_to_find_password();
			}
			if (msg.what == message_what_values.activity_login_button_login_on_click) {
				f_action_to_login();
			}
			if (msg.what == message_what_values.activity_login_button_register_user_on_click) {
				f_action_to_register_user();
			}
			if (msg.what == message_what_values.activity_login_button_visitor_on_click) {
				f_action_to_visitor();
			}
			if (msg.what == message_what_values.activity_login_app_user_login_result_success) {
				// 登录成功
				app_user_login_resp = (interface_app_user_login_resp) msg.obj;
				f_action_login_success(app_user_login_req, app_user_login_resp);
			}
			if (msg.what == message_what_values.activity_login_app_user_login_result_failure) {
				// 登录失败
				f_action_login_failure();
			}
		};
	};

	private void f_action_to_register_user() {// 用户注册
		try {
			Intent intent = new Intent();
			intent.setClass(this, ActivityRegister.class);
			this.startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_to_find_password() {// 找回密码
		try {
			Intent intent = new Intent();
			intent.setClass(this, ActivityResetPassword.class);
			this.startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_to_visitor() {// 游客方式登录
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_to_login() {
		try {
			app_user_login_req = new interface_app_user_login_req();
			app_user_login_req.setUser_name(cmd_edittext_user_name.getText().toString());
			app_user_login_req.setUser_password(cmd_edittext_user_password.getText().toString());
			app_user_login_req.setLogin_code(tools.get_phone_imei(this));
			// --校验
			boolean isReady = true;
			if (!utils_common_tools.get_network_status(this)) {
				isReady = false;
				Toast.makeText(this, "网络异常!", Toast.LENGTH_LONG).show();
			} else if (StringUtils.isBlank(app_user_login_req.getUser_name())) {
				isReady = false;
				Toast.makeText(this, "输入的用户名不能为空!", Toast.LENGTH_LONG).show();
			} else if (StringUtils.isBlank(app_user_login_req.getUser_password())) {
				isReady = false;
				Toast.makeText(this, "输入的用户密码不能为空!", Toast.LENGTH_LONG).show();
			}
			// --登录
			new thread_common_system_user_login(handler, app_user_login_req).start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_login_success(interface_app_user_login_req req, interface_app_user_login_resp resp) {
		try {
			// 用户信息写入缓存
			PreferencesUtils.putString(this, "user_id", resp.getUser_id());
			PreferencesUtils.putString(this, "user_icon", resp.getUser_icon());
			PreferencesUtils.putString(this, "user_type", resp.getUser_type());
			PreferencesUtils.putString(this, "user_name", req.getUser_name());
			PreferencesUtils.putString(this, "user_password", req.getUser_password());
			
			// 跳转到activity_main
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.setClass(this, ActivityMain.class);
			startActivity(intent);
			finish();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_login_failure() {
		try {
			Toast.makeText(this, "登录失败!", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
