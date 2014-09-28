package com.xqj.lovebabies.activitys;

import java.util.List;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_activity_my_center_favorite_listview;
import com.xqj.lovebabies.adapters.adapter_activity_my_center_my_care_baby_listview;
import com.xqj.lovebabies.adapters.adapter_activity_my_center_my_care_baby_listview.onDelItemClickListener;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_my_center_my_care_baby;
import com.xqj.lovebabies.structures.interface_app_get_my_care_baby_req;
import com.xqj.lovebabies.structures.interface_app_get_my_care_baby_resp;
import com.xqj.lovebabies.threads.thread_my_center_get_my_care_baby_list;
import com.xqj.lovebabies.widgets.SwipeListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityMyCenterMyCareBaby extends Activity {
	public static final int RESULT_ADD_BABY = 10001;
	public static final int RESULT_CANCEL = -100;
	// topbar���
	private TextView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	// ���������
	SwipeListView care_baby_listview = null;// ֧�ֻ���ɾ����ListView
	private adapter_activity_my_center_my_care_baby_listview care_baby_adapter = null;
	
	private String user_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_my_care_baby);
		
		user_id = PreferencesUtils.getString(this, "user_id");
		init_top_bar();
		init_listview();
		f_get_my_care_baby();
	}
	
	/**
	 * ��ʼ��ͷ��
	 */
	private void init_top_bar(){
		head_btn_right = (TextView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityMyCenterMyCareBaby.this.finish();
			}
		});
		head_btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ������֤����ӱ���
				Intent intent = new Intent(ActivityMyCenterMyCareBaby.this, ActivityAddBabyByEcode.class);
				startActivityForResult(intent, RESULT_ADD_BABY);
			}
		});
	}
	
	/**
	 * ��ʼ��ListView
	 */
	private void init_listview(){
		care_baby_listview = (SwipeListView)findViewById(R.id.my_center_my_care_baby_listview);
		care_baby_adapter = new adapter_activity_my_center_my_care_baby_listview(this, care_baby_listview.getRightViewWidth());
		care_baby_adapter.setOnDelItemClickListener(new onDelItemClickListener() {
			@Override
			public void onDelItemClick(View v, int position) {
				// ������ɾ����ť���ٽ���ɾ��
				care_baby_listview.hideRightView();
				care_baby_adapter.removeItem(position);
			}
		});
		
//		table_my_center_my_care_baby my_care_baby = new table_my_center_my_care_baby();
//		my_care_baby.setResource_id(R.drawable.baby_pic_1);
//		my_care_baby.setBaby_name("С�Ѽ�");
//		my_care_baby.setBirthday("2011-06-05");
//		my_care_baby.setRelations("(����TA�Ĵ�үŶ~)");
//		care_baby_adapter.addItem(my_care_baby);
//		
//		my_care_baby = new table_my_center_my_care_baby();
//		my_care_baby.setResource_id(R.drawable.baby_pic_2);
//		my_care_baby.setBaby_name("С����");
//		my_care_baby.setBirthday("2011-12-11");
//		my_care_baby.setRelations("(����TA������Ŷ~)");
//		care_baby_adapter.addItem(my_care_baby);
//		
//		my_care_baby = new table_my_center_my_care_baby();
//		my_care_baby.setResource_id(R.drawable.baby_pic_4);
//		my_care_baby.setBaby_name("С����");
//		my_care_baby.setBirthday("2011-10-09");
//		my_care_baby.setRelations("(����TA��үүŶ~)");
//		care_baby_adapter.addItem(my_care_baby);
//		
//		my_care_baby = new table_my_center_my_care_baby();
//		my_care_baby.setResource_id(R.drawable.baby_pic_5);
//		my_care_baby.setBaby_name("СĢ��");
//		my_care_baby.setBirthday("2013-11-01");
//		my_care_baby.setRelations("(����TA������Ŷ~)");
//		care_baby_adapter.addItem(my_care_baby);
		
		care_baby_listview.setAdapter(care_baby_adapter);
		care_baby_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				System.out.println("position:"+position);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_ADD_BABY){// ˢ���ҹ�ע�ı����б�
			f_get_my_care_baby();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	/**
	 * **********   ���罻������   ******************
	 */
	
	private Handler network_handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_get_my_care_baby_success){
				// ��ѯ�ҹ�ע�ı����ɹ�
				interface_app_get_my_care_baby_resp resp = (interface_app_get_my_care_baby_resp)msg.obj;
				if(resp!=null){
					List<table_my_center_my_care_baby> list = resp.getList();
					if(list!=null && list.size()>0){
						care_baby_adapter.removeAll();
						for(int i=0;i<list.size();i++){
							care_baby_adapter.addItem(list.get(i));
						}
					}
				}
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				// ��ѯʧ��
				Toast.makeText(ActivityMyCenterMyCareBaby.this, "��ѯʧ��", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	/**
	 * �����ѯ�ҹ�ע�ı���
	 */
	private void f_get_my_care_baby(){
		interface_app_get_my_care_baby_req req = new interface_app_get_my_care_baby_req();
		req.setUser_id(user_id);
		new thread_my_center_get_my_care_baby_list(this, network_handler, req).start();
	}

}
