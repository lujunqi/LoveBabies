package com.xqj.lovebabies.activitys;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.TimeUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_activity_my_center_my_baby_listview;
import com.xqj.lovebabies.adapters.adapter_activity_my_center_my_baby_listview.onDelItemClickListener;
import com.xqj.lovebabies.adapters.adapter_activity_my_center_my_care_baby_listview;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_album_my_baby;
import com.xqj.lovebabies.databases.table_my_center_my_care_baby;
import com.xqj.lovebabies.structures.interface_app_add_and_set_my_baby_req;
import com.xqj.lovebabies.structures.interface_app_delete_baby_req;
import com.xqj.lovebabies.structures.interface_app_get_my_baby_req;
import com.xqj.lovebabies.structures.interface_app_get_my_baby_resp;
import com.xqj.lovebabies.threads.thread_album_delete_baby_growth;
import com.xqj.lovebabies.threads.thread_album_get_my_baby_list;
import com.xqj.lovebabies.threads.thread_my_center_delete_my_baby;
import com.xqj.lovebabies.widgets.SwipeListView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

public class ActivityMyCenterMyBaby extends Activity {
	public static final int RESULT_MODIFY_BABY_INFO = 10001;
	public static final int RESULT_ADD_BABY = 10002;
	public static final int RESULT_CANCEL = -100;
	
	// topbar 组件
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	// 主界面组件
	SwipeListView baby_listview = null;// 支持滑动删除的ListView
	private adapter_activity_my_center_my_baby_listview baby_adapter = null;

	private String user_id;
	private interface_app_get_my_baby_req req;
	private List<table_album_my_baby> baby_list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_my_baby);
		
		init_top_bar();
		init_listview();
		
		user_id = PreferencesUtils.getString(this, "user_id");
		f_get_baby_list();
	}
	
	/**
	 * 初始化头部
	 */
	private void init_top_bar(){
		head_btn_right = (ImageView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		head_btn_right.setImageResource(R.drawable.add_baby_growth_selector);
		
		head_title.setText("我的宝宝");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityMyCenterMyBaby.this.finish();
			}
		});
		head_btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityMyCenterMyBaby.this, ActivityMyCenterAddMyBaby.class);
				ActivityMyCenterMyBaby.this.startActivityForResult(intent, RESULT_ADD_BABY);
			}
		});
	}
	
	/**
	 * 初始化ListView
	 */
	private void init_listview(){
		baby_listview = (SwipeListView)findViewById(R.id.my_center_my_baby_listview);
		baby_adapter = new adapter_activity_my_center_my_baby_listview(this, baby_listview.getRightViewWidth());
		baby_adapter.setOnDelItemClickListener(new onDelItemClickListener() {
			@Override
			public void onDelItemClick(View v, int position) {
				// 先隐藏删除按钮，再进行删除
				baby_listview.hideRightView();
				table_album_my_baby item = (table_album_my_baby)baby_adapter.getItem(position);
				f_delete_my_baby(item.getBaby_id());
				baby_adapter.removeItem(position);
			}

			@Override
			public void onModiItemClick(View v, int position) {
				// TODO Auto-generated method stub
				System.out.println("onModiItemClick:"+position);
				baby_listview.hideRightView();
				table_album_my_baby item = (table_album_my_baby)baby_adapter.getItem(position);
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("my_baby", item);
				intent.putExtras(bundle);
				intent.setClass(ActivityMyCenterMyBaby.this, ActivityMyCenterModifyMyBaby.class);
				ActivityMyCenterMyBaby.this.startActivityForResult(intent, RESULT_MODIFY_BABY_INFO);
			}
		});
		
//		table_my_center_my_baby my_baby = new table_my_center_my_baby();
//		my_baby.setResource_id(R.drawable.baby_pic_1);
//		my_baby.setBaby_name("小佳佳");
//		my_baby.setAdd_date("2011-06-05");
//		baby_adapter.addItem(my_baby);
//		
//		my_baby = new table_my_center_my_baby();
//		my_baby.setResource_id(R.drawable.baby_pic_2);
//		my_baby.setBaby_name("小仙仙");
//		my_baby.setAdd_date("2011-12-11");
//		baby_adapter.addItem(my_baby);
//		
//		my_baby = new table_my_center_my_baby();
//		my_baby.setResource_id(R.drawable.baby_pic_4);
//		my_baby.setBaby_name("小甜甜");
//		my_baby.setAdd_date("2011-10-09");
//		baby_adapter.addItem(my_baby);
//		
//		my_baby = new table_my_center_my_baby();
//		my_baby.setResource_id(R.drawable.baby_pic_5);
//		my_baby.setBaby_name("小蘑菇");
//		my_baby.setAdd_date("2013-11-01");
//		baby_adapter.addItem(my_baby);
		
		baby_listview.setAdapter(baby_adapter);
		baby_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				table_album_my_baby item = (table_album_my_baby)baby_adapter.getItem(position);
				
				Intent intent = new Intent();
				intent.setClass(ActivityMyCenterMyBaby.this, ActivityMyCenterBabyRelations.class);
				intent.putExtra("baby_name", item.getBaby_name());
				intent.putExtra("baby_id", item.getBaby_id());
				intent.putExtra("baby_pic", item.getBaby_pic());
				intent.putExtra("relation", item.getRelation());
				System.out.println("relation-->"+item.getRelation());
				ActivityMyCenterMyBaby.this.startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_CANCEL){
			// 用户取消操作
		}else if(resultCode==RESULT_MODIFY_BABY_INFO){// 修改宝宝信息
			f_get_baby_list();
		}else if(resultCode==RESULT_ADD_BABY){// 添加宝宝
			f_get_baby_list();
		}
	}
	
	/**
	 * ***********************  网络交互部分   ***********************
	 */
	private Handler network_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_album_my_baby_get_data_success){
				interface_app_get_my_baby_resp resp = (interface_app_get_my_baby_resp)msg.obj;
				baby_list = resp.getList();
				if(baby_list!=null){
					baby_adapter.removeAll();
					for(int i=0;i<baby_list.size();i++){
						System.out.println("getMyBaby_baby_id-->"+baby_list.get(i).getBaby_id());
						baby_adapter.addItem(baby_list.get(i));
					}
				}
			}else if(msg.what == message_what_values.activity_album_get_data_timeout){
				Toast.makeText(ActivityMyCenterMyBaby.this, "查询失败", Toast.LENGTH_SHORT).show();
			}else if(msg.what == message_what_values.activity_my_center_delete_baby_success){
				// 删除成功
				Toast.makeText(ActivityMyCenterMyBaby.this, "删除成功", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	/**
	 * 删除宝宝
	 * @param baby_id
	 */
	private void f_delete_my_baby(String baby_id){
		if(baby_id!=null && baby_id.length()>0
				&& user_id!=null && user_id.length()>0){
			interface_app_delete_baby_req req = new interface_app_delete_baby_req();
			req.setBaby_id(baby_id);
			req.setUser_id(user_id);
			new thread_my_center_delete_my_baby(network_handler, req).start();
		}
	}
	

	// 查询我的宝宝列表
	private void f_get_baby_list(){
		req = new interface_app_get_my_baby_req();
		req.setUser_id(user_id);
		new thread_album_get_my_baby_list(this, network_handler, req).start();
	}
	
}
