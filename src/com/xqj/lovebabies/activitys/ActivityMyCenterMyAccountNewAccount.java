package com.xqj.lovebabies.activitys;

import java.lang.ref.WeakReference;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_get_verify_code_req;
import com.xqj.lovebabies.structures.interface_app_get_verify_code_resp;
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
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMyCenterMyAccountNewAccount extends Activity {
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	// 获取验证码
	private EditText new_account_edittext;
	private Button next_button;
	
	private String user_account;
	private String new_account;
	
	private NetWorkHandler network_handler = new NetWorkHandler(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_my_account_new_account);
		
		init_top_bar();
		user_account = PreferencesUtils.getString(this, "user_phone");
		
		// 获取验证码
		new_account_edittext = (EditText)findViewById(R.id.my_center_enter_account_get_code_edittext);
		next_button = (Button)findViewById(R.id.my_center_enter_account_get_code_btn);
		next_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				f_get_code();
			}
		});
	}
	
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
				Intent intent = new Intent();
				intent.setClass(ActivityMyCenterMyAccountNewAccount.this, ActivityMyCenterMyAccount.class);
				ActivityMyCenterMyAccountNewAccount.this.startActivity(intent);
				ActivityMyCenterMyAccountNewAccount.this.finish();
			}
		});
	}
	
	// 第二步  用户输入新账号，获取验证码
	private int f_get_code(){
		new_account = new_account_edittext.getText().toString();
		if(new_account==null || new_account.length()==0){
			Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
			return -1;
		}
		if(new_account.equals(user_account)){
			Toast.makeText(this, "账号已经被使用", Toast.LENGTH_SHORT).show();
			return -1;
		}
		// 进入下一步,获取验证码
		interface_app_get_verify_code_req req = new interface_app_get_verify_code_req();
		req.setPhone(new_account);
		req.setReason("3");//1-	注册    		2-找回密码      	 3-	更换手机号
		new thread_my_center_get_verify_code(network_handler, req).start();
		return 0;
	}
	
	static class NetWorkHandler extends Handler {
		WeakReference<ActivityMyCenterMyAccountNewAccount> mActivity;
		public NetWorkHandler(ActivityMyCenterMyAccountNewAccount activity){
			mActivity = new WeakReference<ActivityMyCenterMyAccountNewAccount>(activity);  
		}
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_get_verify_code_success){
				interface_app_get_verify_code_resp resp = (interface_app_get_verify_code_resp)msg.obj;
				if(resp!=null){
//					Toast.makeText(mActivity.get(), "获取到验证码："+resp.getVerifyCode(), Toast.LENGTH_SHORT).show();
					if(resp.getVerifyCode().equals("1")){// 发送成功，进入第三步
						// 进入 第三步
						Intent intent = new Intent();
						intent.setClass(mActivity.get(), ActivityMyCenterMyAccountSubmit.class);
						intent.putExtra("new_account", mActivity.get().getNew_account());
						mActivity.get().startActivity(intent);
						mActivity.get().finish();
					}else if(resp.getVerifyCode().equals("-3")){
						Toast.makeText(mActivity.get(), "验证码保存失败", Toast.LENGTH_SHORT).show();
					}else if(resp.getVerifyCode().equals("-1")){
						Toast.makeText(mActivity.get(), "电话号码已被注册", Toast.LENGTH_SHORT).show();
					}else if(resp.getVerifyCode().equals("-2")){
						Toast.makeText(mActivity.get(), "输入的电话号码无效", Toast.LENGTH_SHORT).show();
					}
				}
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(mActivity.get(), "操作失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public String getNew_account() {
		return new_account;
	}
}
