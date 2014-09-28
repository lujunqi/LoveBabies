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

public class ActivityRegister extends Activity {
	public static final int REGISTER_SUCCESS = 10001;
	public static final int READ_RULE_CHECK = 10002;
	// topbar
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;

	// ������

	private EditText enter_phone_edittext;
	private TextView read_rule_front_textview;
	private ImageView read_rule_imageview;
	private TextView read_rule_textview;

	private Button submit_phone_btn;

	private boolean bl_check_read_rule = false;// �Ƿ����Ķ����ϰ���������
	private String new_account = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

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
				intent.setClass(ActivityRegister.this, ActivityLogin.class);
				ActivityRegister.this.startActivity(intent);
				ActivityRegister.this.finish();
			}
		});
	}

	/**
	 * ��ʼ��������
	 */
	private void init_main_page() {
		enter_phone_edittext = (EditText) findViewById(R.id.register_enter_phone_edittext);
		read_rule_front_textview = (TextView) findViewById(R.id.register_read_rule_front_textview);
		read_rule_imageview = (ImageView) findViewById(R.id.register_read_rule_imageview);
		read_rule_textview = (TextView) findViewById(R.id.register_read_rule_textview);

		read_rule_front_textview.setOnClickListener(new OnClickListener() {// ѡ���Ƿ����Ķ�����������
					@Override
					public void onClick(View arg0) {
						Message msg = new Message();
						msg.what = READ_RULE_CHECK;
						change_ui_handler.sendMessage(msg);
					}
				});
		read_rule_imageview.setOnClickListener(new OnClickListener() {// ѡ���Ƿ����Ķ�����������
					@Override
					public void onClick(View arg0) {
						Message msg = new Message();
						msg.what = READ_RULE_CHECK;
						change_ui_handler.sendMessage(msg);
					}
				});
		read_rule_textview.setOnClickListener(new OnClickListener() {// ����򿪰���������
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setClass(ActivityRegister.this, ActivityReadRules.class);
						ActivityRegister.this.startActivity(intent);
					}
				});
		submit_phone_btn = (Button) findViewById(R.id.register_read_rule_btn);

		submit_phone_btn.setOnClickListener(new OnClickListener() {// ��ȡ��֤��
					@Override
					public void onClick(View arg0) {
						new_account = enter_phone_edittext.getText().toString();
						f_get_code();// ����ڶ���
					}
				});
	}

	/**
	 * UI ����
	 */
	private Handler change_ui_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == READ_RULE_CHECK) {
				if (bl_check_read_rule) {// ������Ķ���Ϊδ��
					bl_check_read_rule = false;
					read_rule_imageview.setImageResource(R.drawable.regist_unchecked_icon);
				} else {
					bl_check_read_rule = true;
					read_rule_imageview.setImageResource(R.drawable.regist_checked_icon);
				}
			}
		}
	};


	/**
	 * ************* ���罻������ ***************
	 */
	private Handler network_handler = new Handler() {
		@Override
		public void handleMessage(Message msg){
			if (msg.what == message_what_values.activity_my_center_get_verify_code_success) {
				interface_app_get_verify_code_resp resp = (interface_app_get_verify_code_resp) msg.obj;
				if (resp != null) {
					if (resp.getVerifyCode().equals("1")) {// ���ͳɹ�������ڶ���
						Intent intent = new Intent();
						intent.setClass(ActivityRegister.this, ActivityRegisterCheckCode.class);
						intent.putExtra("new_account", new_account);
						ActivityRegister.this.startActivity(intent);
						ActivityRegister.this.finish();
					} else if (resp.getVerifyCode().equals("-3")) {
						Toast.makeText(ActivityRegister.this, "��֤�뱣��ʧ��", Toast.LENGTH_SHORT).show();
					} else if (resp.getVerifyCode().equals("-1")) {
						Toast.makeText(ActivityRegister.this, "�绰�����ѱ�ע��", Toast.LENGTH_SHORT).show();
					} else if (resp.getVerifyCode().equals("-2")) {
						Toast.makeText(ActivityRegister.this, "����ĵ绰������Ч", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(ActivityRegister.this, "��ȡ��֤��ʧ��", Toast.LENGTH_SHORT).show();
				}
			} else if (msg.what == message_what_values.fragment_my_center_get_data_failed) {
				Toast.makeText(ActivityRegister.this, "��ѯʧ��", Toast.LENGTH_SHORT).show();
			}
		}
	};

	// �����ֻ��ţ���ȡ��֤��
	private int f_get_code() {
		if (!bl_check_read_rule) {
			Toast.makeText(ActivityRegister.this, "�����Ķ����ϰ�������˽����", Toast.LENGTH_SHORT).show();
			return -1;
		}
		if (new_account == null || new_account.length() == 0) {
			Toast.makeText(this, "�������ֻ���", Toast.LENGTH_SHORT).show();
			return -1;
		}
		// ������һ��,��ȡ��֤��
		interface_app_get_verify_code_req req = new interface_app_get_verify_code_req();
		req.setPhone(new_account);
		req.setReason("1");// 1- ע�� 2-�һ����� 3- �����ֻ���
		new thread_my_center_get_verify_code(network_handler, req).start();
		return 0;
	}

}
