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
 * �������롢�ǳƣ�ע���û�
 * @author sunshine
 *
 */
public class ActivityRegisterSetPwdNick extends Activity {
	// topbar
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	private EditText set_pwd_edittext;
	private EditText set_pwd_again_edittext;
	private EditText set_nick_edittext;

	private Button submit_pwd_btn;

	private String new_account = "";
	private String v_code = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register_set_pwd_nick);

		v_code = getIntent().getStringExtra("v_code");
		new_account = getIntent().getStringExtra("new_account");
		init_top_bar();
		init_main_page();
	}
	
	/**
	 * ��ʼ��ͷ��
	 */
	private void init_top_bar() {
		head_btn_right = (ImageView) findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView) findViewById(R.id.head_left_imageview);
		head_title = (TextView) findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		head_title.setText("ע��");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityRegisterSetPwdNick.this, ActivityLogin.class);
				ActivityRegisterSetPwdNick.this.startActivity(intent);
				ActivityRegisterSetPwdNick.this.finish();
			}
		});
	}
	
	/**
	 * ��ʼ��������
	 */
	private void init_main_page() {
		set_pwd_edittext = (EditText) findViewById(R.id.register_set_pwd_edittext);
		set_pwd_again_edittext = (EditText) findViewById(R.id.register_set_pwd_again_edittext);
		set_nick_edittext = (EditText) findViewById(R.id.register_set_nick_edittext);

		submit_pwd_btn = (Button) findViewById(R.id.register_submit_pwd_btn);

		submit_pwd_btn.setOnClickListener(new OnClickListener() {// �ύ����,�ǳƽ���ע��
					@Override
					public void onClick(View arg0) {// ������Ĳ�
						f_set_pwd();
					}
				});
	}
	
	/**
	 * ************* ���罻������ ***************
	 */
	private Handler network_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_check_and_add_user_success){
				interface_app_check_and_regist_user_resp resp = (interface_app_check_and_regist_user_resp)msg.obj;
				if(resp!=null){
					if(resp.getReturnCode().equals("1")){//ע��ɹ�
						Intent intent = new Intent();
						intent.setClass(ActivityRegisterSetPwdNick.this, ActivityRegisterFinished.class);
						intent.putExtra("new_account", new_account);
						ActivityRegisterSetPwdNick.this.startActivity(intent);
						ActivityRegisterSetPwdNick.this.finish();
					}else if(resp.getReturnCode().equals("-1")){//����ʧ��
						Toast.makeText(ActivityRegisterSetPwdNick.this, "����ʧ��", Toast.LENGTH_SHORT).show();
					}else if(resp.getReturnCode().equals("-2")){//��֤�벻����
						Toast.makeText(ActivityRegisterSetPwdNick.this, "��֤�벻����", Toast.LENGTH_SHORT).show();
					}
				}
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(ActivityRegisterSetPwdNick.this, "��ѯʧ��", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	// �ύ�ǳơ�����,��֤ͨ��������û�
	private int f_set_pwd(){
		String nick_name = set_nick_edittext.getText().toString();
		String set_pwd = set_pwd_edittext.getText().toString();
		String set_pwd_again = set_pwd_again_edittext.getText().toString();
		if(nick_name==null||nick_name.length()==0){
			Toast.makeText(this, "�������ǳ�", Toast.LENGTH_SHORT).show();
			return -1;
		}
		if(set_pwd==null||set_pwd.length()==0){
			Toast.makeText(this, "����������", Toast.LENGTH_SHORT).show();
			return -1;
		}
		if(set_pwd_again==null||set_pwd_again.length()==0){
			Toast.makeText(this, "������ȷ������", Toast.LENGTH_SHORT).show();
			return -1;
		}
		if(set_pwd!=null&&set_pwd_again!=null&&!set_pwd_again.equals(set_pwd)){
			Toast.makeText(this, "�����������벻һ��", Toast.LENGTH_SHORT).show();
			return -1;
		}
		interface_app_check_and_regist_user_req req = new interface_app_check_and_regist_user_req();
		req.setV_code(v_code);
		req.setPhone(new_account);
		req.setNick_name(nick_name);
		req.setPassword(set_pwd);
		new thread_my_center_check_and_regist_user(network_handler, req).start();
		return 0;
	}
}
