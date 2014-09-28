package com.xqj.lovebabies.activitys;

import java.lang.ref.WeakReference;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_user_login_req;
import com.xqj.lovebabies.threads.thread_common_system_user_login;

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

public class ActivityMyCenterMyAccount extends Activity {
	private utils_common_tools tools = new utils_common_tools();
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	// �����˺�����
	private EditText password_edittext;
	private EditText account_edittext;
	private Button submit_button;
	
	private String user_id;
	private String user_account;
	private String user_pwd;
	
	private NetWorkHandler network_handler = new NetWorkHandler(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_my_account);
		
		init_top_bar();
		user_id = PreferencesUtils.getString(this, "user_id");
		user_pwd = PreferencesUtils.getString(this, "user_password");
		System.out.println("user_pwd-->"+user_pwd);
		user_account = PreferencesUtils.getString(this, "user_phone");
		
		// �����˺�����
		password_edittext = (EditText)findViewById(R.id.my_center_enter_password_editext);
		account_edittext = (EditText)findViewById(R.id.my_center_enter_account_edittext);
		submit_button = (Button)findViewById(R.id.my_center_enter_account_btn);
		submit_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				f_check_user_account();
			}
		});
		
	}
	
	/**
	 * ��ʼ��ͷ��
	 */
	private void init_top_bar(){
		head_btn_right = (ImageView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		head_title.setText("�ҵ��˺�");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityMyCenterMyAccount.this.finish();
			}
		});
	}
	
	// ��һ��   ��֤�û������˺�����
	private int f_check_user_account(){
		String enter_account = account_edittext.getText().toString();
		String enter_pwd = password_edittext.getText().toString();
		if(enter_account==null || enter_account.length()==0){
			Toast.makeText(this, "�������˺�", Toast.LENGTH_SHORT).show();
			return -1;
		}
		if(enter_pwd==null || enter_pwd.length()==0){
			Toast.makeText(this, "����������", Toast.LENGTH_SHORT).show();
			return -1;
		}
		if(!enter_account.equals(user_account)){
			Toast.makeText(this, "�˺���������", Toast.LENGTH_SHORT).show();
			return -1;
		}
		// ������֤�û�������
		interface_app_user_login_req app_user_login_req = new interface_app_user_login_req();
		app_user_login_req.setUser_name(enter_account);
		app_user_login_req.setUser_password(enter_pwd);
		app_user_login_req.setLogin_code(tools.get_phone_imei(this));
		
		new thread_common_system_user_login(network_handler, app_user_login_req).start();
		
		return 0;
	}
	
	static class NetWorkHandler extends Handler {
		WeakReference<ActivityMyCenterMyAccount> mActivity;
		public NetWorkHandler(ActivityMyCenterMyAccount activity){
			mActivity = new WeakReference<ActivityMyCenterMyAccount>(activity);  
		}
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_login_app_user_login_result_success){
				// ��֤�û�������ɹ�������ڶ���
				Intent intent = new Intent();
				intent.setClass(mActivity.get(), ActivityMyCenterMyAccountNewAccount.class);
				mActivity.get().startActivity(intent);
				mActivity.get().finish();
			}else if(msg.what == message_what_values.activity_login_app_user_login_result_failure){
				// ��֤�û��� ����ʧ��
				Toast.makeText(mActivity.get(), "��֤����ʧ�ܣ�������", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
