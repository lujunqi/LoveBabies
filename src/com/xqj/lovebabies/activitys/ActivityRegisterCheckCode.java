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
 * 获取验证码，验证手机号
 * @author sunshine
 *
 */
public class ActivityRegisterCheckCode extends Activity {
	// topbar
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	private EditText code_edittext;
	private TextView reget_code_timer_textview;

	private Button reget_code_btn;
	private Button submit_code_btn;

	private int time_count = 30;//30秒计时
	private String new_account = "";
	private String v_code = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register_check_code);

		new_account = getIntent().getStringExtra("new_account");
		init_top_bar();
		init_main_page();
		timer_handler.postDelayed(timer_runnable, 1000);// 倒计时开始
	}
	
	/**
	 * 初始化头部
	 */
	private void init_top_bar() {
		head_btn_right = (ImageView) findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView) findViewById(R.id.head_left_imageview);
		head_title = (TextView) findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		head_title.setText("注册");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityRegisterCheckCode.this, ActivityRegister.class);
				ActivityRegisterCheckCode.this.startActivity(intent);
				ActivityRegisterCheckCode.this.finish();
			}
		});
	}
	
	/**
	 * 初始化主界面
	 */
	private void init_main_page() {
		code_edittext = (EditText) findViewById(R.id.register_phone_edittext);
		reget_code_timer_textview = (TextView) findViewById(R.id.register_reget_code_timer_textview);

		reget_code_btn = (Button) findViewById(R.id.register_reget_code_btn);
		submit_code_btn = (Button) findViewById(R.id.register_submit_code_btn);
		reget_code_btn.setVisibility(View.INVISIBLE);

		reget_code_btn.setOnClickListener(new OnClickListener() {// 重新获取验证码
					@Override
					public void onClick(View arg0) {
						reget_code_btn.setVisibility(View.INVISIBLE);
						reget_code_timer_textview.setVisibility(View.VISIBLE);
						timer_handler.postDelayed(timer_runnable, 1000);// 倒计时开始
						f_re_get_code();
					}
				});
		submit_code_btn.setOnClickListener(new OnClickListener() {// 提交验证码
					@Override
					public void onClick(View arg0) {
						f_check_code();
					}
				});
	}
	
	/**
	 * 计时
	 */
	Handler timer_handler = new Handler();
	Runnable timer_runnable = new Runnable() {
		@Override
		public void run() {
			time_count--;
			reget_code_timer_textview.setText(time_count+"秒后可获得验证码");
			timer_handler.postDelayed(this, 1000);
			if(time_count == 0){
				reget_code_btn.setVisibility(View.VISIBLE);
				reget_code_timer_textview.setVisibility(View.INVISIBLE);
				timer_handler.removeCallbacks(this);
				time_count = 30;
				reget_code_timer_textview.setText(time_count+"秒后可获得验证码");
			}
		}
	};
	
	/**
	 * ************* 网络交互部分 ***************
	 */
	private Handler network_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_get_verify_code_success){
				interface_app_get_verify_code_resp resp = (interface_app_get_verify_code_resp)msg.obj;
				if(resp!=null){
					if(resp.getVerifyCode().equals("1")){//获取验证码成功
//						timer_handler.postDelayed(timer_runnable, 1000);// 倒计时开始
					}else if(resp.getVerifyCode().equals("-3")){
						Toast.makeText(ActivityRegisterCheckCode.this, "验证码保存失败", Toast.LENGTH_SHORT).show();
					}else if(resp.getVerifyCode().equals("-1")){
						Toast.makeText(ActivityRegisterCheckCode.this, "电话号码已被注册", Toast.LENGTH_SHORT).show();
					}else if(resp.getVerifyCode().equals("-2")){
						Toast.makeText(ActivityRegisterCheckCode.this, "输入的电话号码无效", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(ActivityRegisterCheckCode.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == message_what_values.activity_my_center_check_verify_code_success){
				// 验证手机号和验证码
				interface_app_check_verify_code_resp resp = (interface_app_check_verify_code_resp)msg.obj;
				if(resp!=null){
					if(resp.getReturnCode().equals("1")){//验证成功，进入第三步
						Intent intent = new Intent();
						intent.setClass(ActivityRegisterCheckCode.this, ActivityRegisterSetPwdNick.class);
						intent.putExtra("v_code", v_code);
						intent.putExtra("new_account", new_account);
						ActivityRegisterCheckCode.this.startActivity(intent);
						ActivityRegisterCheckCode.this.finish();
					}else if(resp.getReturnCode().equals("-1")){//验证码验证不一致
						Toast.makeText(ActivityRegisterCheckCode.this, "验证码验证不一致", Toast.LENGTH_SHORT).show();
					}else if(resp.getReturnCode().equals("-2")){//验证码未获取到
						Toast.makeText(ActivityRegisterCheckCode.this, "验证码未获取到", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(ActivityRegisterCheckCode.this, "验证验证码失败", Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(ActivityRegisterCheckCode.this, "查询失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	// 根据手机号，获取验证码
	private int f_re_get_code(){
		// 获取验证码
		interface_app_get_verify_code_req req = new interface_app_get_verify_code_req();
		req.setPhone(new_account);
		req.setReason("1");//1-	注册    		2-找回密码      	 3-	更换手机号
		new thread_my_center_get_verify_code(network_handler, req).start();
		return 0;
	}
	
	// 验证手机号码以及验证码
	private int f_check_code(){
		v_code = code_edittext.getText().toString();
		if(v_code!=null && v_code.length()>0){
			interface_app_check_verify_code_req req = new interface_app_check_verify_code_req();
			req.setPhone(new_account);
			req.setV_code(v_code);
			new thread_my_center_check_verify_code(network_handler, req).start();
			return 0;
		}else{
			Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
			return -1;
		}
	}
	
}
