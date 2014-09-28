package com.xqj.lovebabies.activitys;

import java.util.List;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_activity_my_center_baby_relations_listview;
import com.xqj.lovebabies.adapters.adapter_activity_my_center_baby_relations_listview.onDelItemClickListener;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_my_center_baby_relations;
import com.xqj.lovebabies.databases.table_my_center_my_care_baby;
import com.xqj.lovebabies.structures.interface_app_get_baby_relations_req;
import com.xqj.lovebabies.structures.interface_app_get_baby_relations_resp;
import com.xqj.lovebabies.structures.interface_app_get_my_care_baby_req;
import com.xqj.lovebabies.structures.interface_app_get_my_care_baby_resp;
import com.xqj.lovebabies.threads.thread_my_center_get_baby_relations_list;
import com.xqj.lovebabies.threads.thread_my_center_get_my_care_baby_list;
import com.xqj.lovebabies.widgets.SwipeListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityMyCenterBabyRelations extends Activity {
	// topbar ���
	private TextView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	// ���������
	SwipeListView relation_listview = null;// ֧�ֻ���ɾ����ListView
	private adapter_activity_my_center_baby_relations_listview relation_adapter = null;

	private String baby_name = "";
	private String baby_id;
	private String baby_pic;
	private String relation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_relations);
		
		baby_name = getIntent().getStringExtra("baby_name");
		baby_id = getIntent().getStringExtra("baby_id");
		baby_pic = getIntent().getStringExtra("baby_pic");
		relation = getIntent().getStringExtra("relation");
		
		System.out.println("ActivityMyCenterBabyRelations:baby_id-->"+baby_id);

		init_top_bar();
		init_listview();
		f_get_my_baby_relations();
	}

	
	/**
	 * ��ʼ��ͷ��
	 */
	private void init_top_bar(){
		head_btn_right = (TextView)findViewById(R.id.head_right_textview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		
		head_title.setText(baby_name + "�ķ�˿��");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityMyCenterBabyRelations.this.finish();
			}
		});
		head_btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityMyCenterBabyRelations.this, ActivityMyCenterInviteFamily.class);
				intent.putExtra("baby_id", baby_id);
				intent.putExtra("baby_name", baby_name);
				intent.putExtra("baby_pic", baby_pic);
				intent.putExtra("relation", relation);
				System.out.println("relation-->"+relation);
				ActivityMyCenterBabyRelations.this.startActivity(intent);
			}
		});
	}
	
	/**
	 * ��ʼ��ListView
	 */
	private void init_listview(){
		relation_listview = (SwipeListView)findViewById(R.id.my_center_relations_listview);
		relation_adapter = new adapter_activity_my_center_baby_relations_listview(this, relation_listview.getRightViewWidth());
		relation_adapter.setOnDelItemClickListener(new onDelItemClickListener() {
			@Override
			public void onDelItemClick(View v, int position) {
				// ������ɾ����ť���ٽ���ɾ��
				relation_listview.hideRightView();
				relation_adapter.removeItem(position);
			}
		});
		
//		table_my_center_baby_relations my_relation = new table_my_center_baby_relations();
//		my_relation.setResource_id(R.drawable.baby_pic_1);
//		my_relation.setUser_nick_name("����ͯ");
//		my_relation.setRelation("үү");
//		relation_adapter.addItem(my_relation);
//		
//		my_relation = new table_my_center_baby_relations();
//		my_relation.setResource_id(R.drawable.baby_pic_2);
//		my_relation.setUser_nick_name("������");
//		my_relation.setRelation("����");
//		relation_adapter.addItem(my_relation);
//		
//		my_relation = new table_my_center_baby_relations();
//		my_relation.setResource_id(R.drawable.baby_pic_4);
//		my_relation.setUser_nick_name("�ӼӼ�");
//		my_relation.setRelation("С��");
//		relation_adapter.addItem(my_relation);
		
		
		relation_listview.setAdapter(relation_adapter);
		relation_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				System.out.println("position:"+position);
			}
		});
	}
	
	/**
	 * ****************   ���罻������   *********************************
	 */
	
	private Handler network_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_get_baby_relations_success){
				// ��ѯ���������б�ɹ�
				interface_app_get_baby_relations_resp resp = (interface_app_get_baby_relations_resp)msg.obj;
				if(resp!=null){
					List<table_my_center_baby_relations> list = resp.getList();
					if(list!=null && list.size()>0){
						relation_adapter.removeAll();
						for(int i=0;i<list.size();i++){
							relation_adapter.addItem(list.get(i));
						}
					}
				}
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				// ��ѯʧ��
				Toast.makeText(ActivityMyCenterBabyRelations.this, "��ѯʧ��", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	/**
	 * �����ѯ���������б�
	 */
	private void f_get_my_baby_relations(){
		interface_app_get_baby_relations_req req = new interface_app_get_baby_relations_req();
		req.setBaby_id(baby_id);
		new thread_my_center_get_baby_relations_list(this, network_handler, req).start();
	}
}
