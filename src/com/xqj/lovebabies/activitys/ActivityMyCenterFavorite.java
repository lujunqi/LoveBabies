package com.xqj.lovebabies.activitys;

import java.util.List;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_activity_my_center_favorite_listview;
import com.xqj.lovebabies.adapters.adapter_activity_my_center_favorite_listview.onDelItemClickListener;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_my_center_favorite;
import com.xqj.lovebabies.structures.interface_app_delete_my_favorite_req;
import com.xqj.lovebabies.structures.interface_app_get_my_favorite_req;
import com.xqj.lovebabies.structures.interface_app_get_my_favorite_resp;
import com.xqj.lovebabies.threads.thread_my_center_delete_my_favorite;
import com.xqj.lovebabies.threads.thread_my_center_get_my_favorite_list;
import com.xqj.lovebabies.widgets.SwipeListView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMyCenterFavorite extends Activity {
	// 头部组件
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	// 主界面组件
	SwipeListView favorites_listview = null;// 支持滑动删除的ListView
	private adapter_activity_my_center_favorite_listview favorite_adapter = null;
	
	private String user_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_favorite);
		
		user_id = PreferencesUtils.getString(this, "user_id");
		init_top_bar();
		
		init_listview();
		f_get_my_favorite();
	}
	
	/**
	 * 初始化头部
	 */
	private void init_top_bar(){
		head_btn_right = (ImageView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		
		head_title.setText("我的收藏");
		head_btn_right.setVisibility(View.INVISIBLE);
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityMyCenterFavorite.this.finish();
			}
		});
	}
	
	/**
	 * 初始化ListView
	 */
	private void init_listview(){
		favorites_listview = (SwipeListView)findViewById(R.id.my_center_favorite_listview);
		favorite_adapter = new adapter_activity_my_center_favorite_listview(this, favorites_listview.getRightViewWidth());
		favorite_adapter.setOnDelItemClickListener(new onDelItemClickListener() {
			@Override
			public void onDelItemClick(View v, int position) {
				// 先隐藏删除按钮，再进行删除
				favorites_listview.hideRightView();
				table_my_center_favorite item = (table_my_center_favorite)favorite_adapter.getItem(position);
				f_delete_favorite(item);
				favorite_adapter.removeItem(position);
			}
		});
		
		favorites_listview.setAdapter(favorite_adapter);
		favorites_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				System.out.println("position:"+position);
			}
		});
	}
	
	//
	/***
	 * ***********************  网络交互部分   ************************
	 */
	private Handler network_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_get_my_favorite_success){
				interface_app_get_my_favorite_resp resp = (interface_app_get_my_favorite_resp)msg.obj;
				if(resp!=null){
					List<table_my_center_favorite> list = resp.getList();
					if(list!=null && list.size()>0){
						favorite_adapter.removeAll();
						for(int i=0;i<list.size();i++){
							favorite_adapter.addItem(list.get(i));
						}
					}
				}
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(ActivityMyCenterFavorite.this, "查询失败", Toast.LENGTH_SHORT).show();
			} else if(msg.what == message_what_values.activity_my_center_delete_my_favorite_success){
				Toast.makeText(ActivityMyCenterFavorite.this, "操作成功", Toast.LENGTH_SHORT).show();
			} 
		}
	};
	// 查询我的收藏
	private void f_get_my_favorite(){
		interface_app_get_my_favorite_req req = new interface_app_get_my_favorite_req();
		req.setUser_id(user_id);
		new thread_my_center_get_my_favorite_list(this, network_handler, req).start();
	}
	// 删除我的收藏
	private void f_delete_favorite(table_my_center_favorite item){
		interface_app_delete_my_favorite_req req = new interface_app_delete_my_favorite_req();
		req.setId(item.getId());
		req.setContent_id(item.getContent_id());
		req.setUser_id(user_id);
		new thread_my_center_delete_my_favorite(network_handler, req).start();
	}
}
