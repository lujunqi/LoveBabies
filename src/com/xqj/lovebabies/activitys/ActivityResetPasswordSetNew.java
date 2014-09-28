package com.xqj.lovebabies.activitys;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_check_verify_code_req;
import com.xqj.lovebabies.structures.interface_app_check_verify_code_resp;
import com.xqj.lovebabies.structures.interface_app_get_verify_code_req;
import com.xqj.lovebabies.structures.interface_app_get_verify_code_resp;
import com.xqj.lovebabies.structures.interface_app_reset_password_req;
import com.xqj.lovebabies.structures.interface_app_reset_password_resp;
import com.xqj.lovebabies.threads.thread_my_center_check_verify_code;
import com.xqj.lovebabies.threads.thread_my_center_get_verify_code;
import com.xqj.lovebabies.threads.thread_my_center_reset_password;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 找回密码：设置新密码
 * @author sunshine
 *
 */
public class ActivityResetPasswordSetNew extends Activity {
	// topbar
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	private EditText set_pwd_edittext;
	private EditText set_pwd_again_edittext;

	private Button submit_code_btn;

	private int time_count = 30;//30秒计时
	private String phone_number = "";
	private String v_code = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reset_password_set_new);

		phone_number = getIntent().getStringExtra("phone_number");
		v_code = getIntent().getStringExtra("v_code");
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
				intent.setClass(ActivityResetPasswordSetNew.this, ActivityResetPassword.class);
				ActivityResetPasswordSetNew.this.startActivity(intent);
				ActivityResetPasswordSetNew.this.finish();
			}
		});
	}
	
	/**
	 * 初始化主界面
	 */
	private void init_main_page() {
		set_pwd_edittext = (EditText) findViewById(R.id.reset_password_set_pwd_edittext);
		set_pwd_again_edittext = (EditText) findViewById(R.id.reset_password_set_pwd_again_edittext);

		submit_code_btn = (Button) findViewById(R.id.reset_pwd_new_pwd_submit_btn);

		submit_code_btn.setOnClickListener(new OnClickListener() {// 提交新密码
					@Override
					public void onClick(View arg0) {
						f_submit_new_pwd();
					}
				});
	}
	
	/**
	 * ************* 网络交互部分 ***************
	 */
	private Handler network_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_reset_password_success){
				interface_app_reset_password_resp resp = (interface_app_reset_password_resp)msg.obj;
				if(resp!=null){
					if(resp.getReturnCode().equals("1")){// 重置成功，进入登录页面
						Intent intent = new Intent();
						intent.setClass(ActivityResetPasswordSetNew.this, ActivityLogin.class);
						ActivityResetPasswordSetNew.this.startActivity(intent);
						intent.putExtra("new_account", phone_number);
						ActivityResetPasswordSetNew.this.finish();
					}else if(resp.getReturnCode().equals("-1")){
						Toast.makeText(ActivityResetPasswordSetNew.this, "保存失败", Toast.LENGTH_SHORT).show();
					}else if(resp.getReturnCode().equals("-2")){
						Toast.makeText(ActivityResetPasswordSetNew.this, "验证码不存在", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(ActivityResetPasswordSetNew.this, "提交失败", Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(ActivityResetPasswordSetNew.this, "查询失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	// 验证手机号码以及验证码
	private int f_submit_new_pwd(){
		String set_pwd = set_pwd_edittext.getText().toString();
		String set_pwd_again = set_pwd_again_edittext.getText().toString();
		if(set_pwd==null||set_pwd.length()==0){
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			return -1;
		}
		if(set_pwd_again==null||set_pwd_again.length()==0){
			Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
			return -1;
		}
		if(set_pwd!=null&&set_pwd_again!=null&&!set_pwd_again.equals(set_pwd)){
			Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
			return -1;
		}
		interface_app_reset_password_req req = new interface_app_reset_password_req();
		req.setPhone(phone_number);
		req.setV_code(v_code);
		req.setPassword(set_pwd);
		new thread_my_center_reset_password(network_handler, req).start();
		return 0;
	}
	
}
