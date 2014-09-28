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
	
	// �ύ�޸�
	private EditText code_edittext;
	private TextView time_textview;
	private Button reget_code_button;
	private Button save_button;
	
	private String user_id;
	private String user_account;
	private String user_pwd;
	private String new_account;
	private String v_code;
	private int get_code_count=0;//��ȡ��֤��Ĵ���
	
	private int time_count = 30;//30���ʱ
	
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
		
		// �ύ�޸�
		code_edittext = (EditText)findViewById(R.id.my_center_enter_account_code_edittext);
		time_textview = (TextView)findViewById(R.id.my_center_enter_account_time_textview);
		save_button = (Button)findViewById(R.id.my_center_enter_account_submit_code_btn);
		save_button.setOnClickListener(new OnClickListener() {// �ύ��֤��
			@Override
			public void onClick(View arg0) {
				f_submit_code();
			}
		});
		reget_code_button = (Button)findViewById(R.id.my_center_enter_account_reget_code_btn);
		reget_code_button.setOnClickListener(new OnClickListener() {// ���»�ȡ��֤��
			@Override
			public void onClick(View arg0) {
				reget_code_button.setVisibility(View.INVISIBLE);
				time_textview.setVisibility(View.VISIBLE);
				timer_handler.postDelayed(timer_runnable, 1000);// ����ʱ��ʼ
				f_reget_code();
			}
		});
		reget_code_button.setVisibility(View.INVISIBLE);
		
		timer_handler.postDelayed(timer_runnable, 1000);// ����ʱ��ʼ
	}
	
	/**
	 * ��ʱ
	 */
	Handler timer_handler = new Handler();
	Runnable timer_runnable = new Runnable() {
		@Override
		public void run() {
			time_count--;
			time_textview.setText(time_count+"���ɻ����֤��");
			timer_handler.postDelayed(this, 1000);
			if(time_count == 0){
				reget_code_button.setVisibility(View.VISIBLE);
				time_textview.setVisibility(View.INVISIBLE);
				timer_handler.removeCallbacks(this);
				time_count = 30;
				time_textview.setText(time_count+"���ɻ����֤��");
			}
		}
	};
	
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
				ActivityMyCenterMyAccountSubmit.this.finish();
			}
		});
	}
	
	// �ڶ���  �û��������˺ţ���ȡ��֤��
	private int f_reget_code(){
		// ��ȡ��֤��
		interface_app_get_verify_code_req req = new interface_app_get_verify_code_req();
		req.setPhone(new_account);
		req.setReason("3");//1-	ע��    		2-�һ�����      	 3-	�����ֻ���
		new thread_my_center_get_verify_code(network_handler, req).start();
		return 0;
	}
	
	// ������ �ύ
	// �û��������˺�
	private int f_submit_code(){
		String enter_code = code_edittext.getText().toString();
		if(enter_code==null || enter_code.length()==0){
			Toast.makeText(this, "��������֤��", Toast.LENGTH_SHORT).show();
			return -1;
		}
		// ������һ��,�ύ�޸�
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
					if(resp.getVerifyCode().equals("1")){// �ط���֤��ɹ�
						
					}else if(resp.getVerifyCode().equals("-3")){
						Toast.makeText(mActivity.get(), "��֤�뱣��ʧ��", Toast.LENGTH_SHORT).show();
					}else if(resp.getVerifyCode().equals("-1")){
						Toast.makeText(mActivity.get(), "�绰�����ѱ�ע��", Toast.LENGTH_SHORT).show();
					}else if(resp.getVerifyCode().equals("-2")){
						Toast.makeText(mActivity.get(), "����ĵ绰������Ч", Toast.LENGTH_SHORT).show();
					}
				}
			}else if(msg.what == message_what_values.activity_my_center_change_phone_success){
				
				interface_app_change_phone_submit_resp resp = (interface_app_change_phone_submit_resp)msg.obj;
				if(resp!=null && resp.getReturnCode().equals("1")){//�޸ĳɹ�
					Toast.makeText(mActivity.get(), "�޸ĳɹ�", Toast.LENGTH_SHORT).show();
					// �����˺ű��浽����
					PreferencesUtils.putString(mActivity.get(), "user_phone", mActivity.get().getNew_account());
					PreferencesUtils.putString(mActivity.get(), "user_name", mActivity.get().getNew_account());
					Intent result=new Intent(null,Uri.parse(mActivity.get().getNew_account().trim()));
					// ����Ƿ��ص���Ϣ�ˣ�ǰ�߽���룬���߷��ص����ݡ�
					mActivity.get().setResult(ActivityPersonalInfo.RESULT_CHANGE_PHONE, result);
					mActivity.get().finish();
				}else if(resp!=null && resp.getReturnCode().equals("-1")){
					Toast.makeText(mActivity.get(), "����ʧ��", Toast.LENGTH_SHORT).show();
				}else if(resp!=null && resp.getReturnCode().equals("-2")){
					Toast.makeText(mActivity.get(), "��֤�벻����", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mActivity.get(), "�޸�ʧ��", Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(mActivity.get(), "����ʧ��", Toast.LENGTH_SHORT).show();
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
