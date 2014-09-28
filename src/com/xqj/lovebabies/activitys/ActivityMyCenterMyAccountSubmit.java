package com.xqj.lovebabies.activitys;

import java.lang.ref.WeakReference;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_change_phone_submit_req;
import com.xqj.lovebabies.structures.interface_app_change_phone_submit_resp;
import com.xqj.lovebabies.structures.interface_app_get_verify_code_req;
import com.xqj.lovebabies.structures.interface_app_get_verify_code_resp;
import com.xqj.lovebabies.structures.interface_app_update_user_detail_req;
import com.xqj.lovebabies.structures.interface_app_user_login_req;
import com.xqj.lovebabies.threads.thread_common_system_user_login;
import com.xqj.lovebabies.threads.thread_my_center_change_phone_submit;
import com.xqj.lovebabies.threads.thread_my_center_get_verify_code;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMyCenterMyAccountSubmit extends Activity {
	private utils_common_tools tools = new utils_common_tools();
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	// 提交修改
	private EditText code_edittext;
	private TextView time_textview;
	private Button reget_code_button;
	private Button save_button;
	
	private String user_id;
	private String user_account;
	private String user_pwd;
	private String new_account;
	private String v_code;
	private int get_code_count=0;//获取验证码的次数
	
	private int time_count = 30;//30秒计时
	
	private NetWorkHandler network_handler = new NetWorkHandler(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_my_account_submit);
		
		init_top_bar();
		user_id = PreferencesUtils.getString(this, "user_id");
		user_pwd = PreferencesUtils.getString(this, "user_password");
		System.out.println("user_pwd-->"+user_pwd);
		user_account = PreferencesUtils.getString(this, "user_phone");
		new_account = getIntent().getStringExtra("new_account");
		
		// 提交修改
		code_edittext = (EditText)findViewById(R.id.my_center_enter_account_code_edittext);
		time_textview = (TextView)findViewById(R.id.my_center_enter_account_time_textview);
		save_button = (Button)findViewById(R.id.my_center_enter_account_submit_code_btn);
		save_button.setOnClickListener(new OnClickListener() {// 提交验证码
			@Override
			public void onClick(View arg0) {
				f_submit_code();
			}
		});
		reget_code_button = (Button)findViewById(R.id.my_center_enter_account_reget_code_btn);
		reget_code_button.setOnClickListener(new OnClickListener() {// 重新获取验证码
			@Override
			public void onClick(View arg0) {
				reget_code_button.setVisibility(View.INVISIBLE);
				time_textview.setVisibility(View.VISIBLE);
				timer_handler.postDelayed(timer_runnable, 1000);// 倒计时开始
				f_reget_code();
			}
		});
		reget_code_button.setVisibility(View.INVISIBLE);
		
		timer_handler.postDelayed(timer_runnable, 1000);// 倒计时开始
	}
	
	/**
	 * 计时
	 */
	Handler timer_handler = new Handler();
	Runnable timer_runnable = new Runnable() {
		@Override
		public void run() {
			time_count--;
			time_textview.setText(time_count+"秒后可获得验证码");
			timer_handler.postDelayed(this, 1000);
			if(time_count == 0){
				reget_code_button.setVisibility(View.VISIBLE);
				time_textview.setVisibility(View.INVISIBLE);
				timer_handler.removeCallbacks(this);
				time_count = 30;
				time_textview.setText(time_count+"秒后可获得验证码");
			}
		}
	};
	
	/**
	 * 初始化头部
	 */
	private void init_top_bar(){
		head_btn_right = (ImageView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		head_title.setText("我的账号");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityMyCenterMyAccountSubmit.this.finish();
			}
		});
	}
	
	// 第二步  用户输入新账号，获取验证码
	private int f_reget_code(){
		// 获取验证码
		interface_app_get_verify_code_req req = new interface_app_get_verify_code_req();
		req.setPhone(new_account);
		req.setReason("3");//1-	注册    		2-找回密码      	 3-	更换手机号
		new thread_my_center_get_verify_code(network_handler, req).start();
		return 0;
	}
	
	// 第三步 提交
	// 用户输入新账号
	private int f_submit_code(){
		String enter_code = code_edittext.getText().toString();
		if(enter_code==null || enter_code.length()==0){
			Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
			return -1;
		}
		// 进入下一步,提交修改
		interface_app_change_phone_submit_req req = new interface_app_change_phone_submit_req();
		req.setNew_phone(new_account);
		req.setV_code(enter_code);
		req.setOld_phone(user_account);
		new thread_my_center_change_phone_submit(network_handler, req).start();
		return 0;
	}
	
	static class NetWorkHandler extends Handler {
		WeakReference<ActivityMyCenterMyAccountSubmit> mActivity;
		public NetWorkHandler(ActivityMyCenterMyAccountSubmit activity){
			mActivity = new WeakReference<ActivityMyCenterMyAccountSubmit>(activity);  
		}
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_get_verify_code_success){
				interface_app_get_verify_code_resp resp = (interface_app_get_verify_code_resp)msg.obj;
				if(resp!=null){
					if(resp.getVerifyCode().equals("1")){// 重发验证码成功
						
					}else if(resp.getVerifyCode().equals("-3")){
						Toast.makeText(mActivity.get(), "验证码保存失败", Toast.LENGTH_SHORT).show();
					}else if(resp.getVerifyCode().equals("-1")){
						Toast.makeText(mActivity.get(), "电话号码已被注册", Toast.LENGTH_SHORT).show();
					}else if(resp.getVerifyCode().equals("-2")){
						Toast.makeText(mActivity.get(), "输入的电话号码无效", Toast.LENGTH_SHORT).show();
					}
				}
			}else if(msg.what == message_what_values.activity_my_center_change_phone_success){
				
				interface_app_change_phone_submit_resp resp = (interface_app_change_phone_submit_resp)msg.obj;
				if(resp!=null && resp.getReturnCode().equals("1")){//修改成功
					Toast.makeText(mActivity.get(), "修改成功", Toast.LENGTH_SHORT).show();
					// 将新账号保存到缓存
					PreferencesUtils.putString(mActivity.get(), "user_phone", mActivity.get().getNew_account());
					PreferencesUtils.putString(mActivity.get(), "user_name", mActivity.get().getNew_account());
					Intent result=new Intent(null,Uri.parse(mActivity.get().getNew_account().trim()));
					// 这就是返回的信息了，前者结果码，后者返回的数据。
					mActivity.get().setResult(ActivityPersonalInfo.RESULT_CHANGE_PHONE, result);
					mActivity.get().finish();
				}else if(resp!=null && resp.getReturnCode().equals("-1")){
					Toast.makeText(mActivity.get(), "保存失败", Toast.LENGTH_SHORT).show();
				}else if(resp!=null && resp.getReturnCode().equals("-2")){
					Toast.makeText(mActivity.get(), "验证码不存在", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mActivity.get(), "修改失败", Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(mActivity.get(), "操作失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public String getNew_account() {
		return new_account;
	}


	public String getV_code() {
		return v_code;
	}

	public void setV_code(String v_code) {
		this.v_code = v_code;
	}

	public Handler getTimer_handler() {
		return timer_handler;
	}

	public Runnable getTimer_runnable() {
		return timer_runnable;
	}

	public int getGet_code_count() {
		return get_code_count;
	}
	
}
