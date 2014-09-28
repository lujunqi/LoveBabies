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
	
	// ��ȡ��֤��
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
		
		// ��ȡ��֤��
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
				Intent intent = new Intent();
				intent.setClass(ActivityMyCenterMyAccountNewAccount.this, ActivityMyCenterMyAccount.class);
				ActivityMyCenterMyAccountNewAccount.this.startActivity(intent);
				ActivityMyCenterMyAccountNewAccount.this.finish();
			}
		});
	}
	
	// �ڶ���  �û��������˺ţ���ȡ��֤��
	private int f_get_code(){
		new_account = new_account_edittext.getText().toString();
		if(new_account==null || new_account.length()==0){
			Toast.makeText(this, "�������˺�", Toast.LENGTH_SHORT).show();
			return -1;
		}
		if(new_account.equals(user_account)){
			Toast.makeText(this, "�˺��Ѿ���ʹ��", Toast.LENGTH_SHORT).show();
			return -1;
		}
		// ������һ��,��ȡ��֤��
		interface_app_get_verify_code_req req = new interface_app_get_verify_code_req();
		req.setPhone(new_account);
		req.setReason("3");//1-	ע��    		2-�һ�����      	 3-	�����ֻ���
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
//					Toast.makeText(mActivity.get(), "��ȡ����֤�룺"+resp.getVerifyCode(), Toast.LENGTH_SHORT).show();
					if(resp.getVerifyCode().equals("1")){// ���ͳɹ������������
						// ���� ������
						Intent intent = new Intent();
						intent.setClass(mActivity.get(), ActivityMyCenterMyAccountSubmit.class);
						intent.putExtra("new_account", mActivity.get().getNew_account());
						mActivity.get().startActivity(intent);
						mActivity.get().finish();
					}else if(resp.getVerifyCode().equals("-3")){
						Toast.makeText(mActivity.get(), "��֤�뱣��ʧ��", Toast.LENGTH_SHORT).show();
					}else if(resp.getVerifyCode().equals("-1")){
						Toast.makeText(mActivity.get(), "�绰�����ѱ�ע��", Toast.LENGTH_SHORT).show();
					}else if(resp.getVerifyCode().equals("-2")){
						Toast.makeText(mActivity.get(), "����ĵ绰������Ч", Toast.LENGTH_SHORT).show();
					}
				}
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(mActivity.get(), "����ʧ��", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public String getNew_account() {
		return new_account;
	}
}
