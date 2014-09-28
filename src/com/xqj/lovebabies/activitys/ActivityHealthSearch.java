package com.xqj.lovebabies.activitys;

import java.util.List;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_fragment_health_search_listview;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_health_information;
import com.xqj.lovebabies.structures.interface_app_search_health_information_req;
import com.xqj.lovebabies.structures.interface_app_search_health_information_resp;
import com.xqj.lovebabies.threads.thread_health_search_information_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityHealthSearch extends Activity {
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	private EditText search_key_edittext;
	private Button search_button;
	private ListView search_listview;
	
	private adapter_fragment_health_search_listview search_adapter = null;
	
	private String user_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_health_search);
		
		user_id = PreferencesUtils.getString(this, "user_id");
		
		init_top_bar();
		search_key_edittext = (EditText)findViewById(R.id.health_search_edittext);
		search_button = (Button)findViewById(R.id.health_search_button);
		search_listview = (ListView)findViewById(R.id.fragment_health_search_listview);
		search_adapter = new adapter_fragment_health_search_listview(this);
		search_listview.setAdapter(search_adapter);
		search_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				f_search_health_info();
			}
		});
		search_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				table_health_information info = (table_health_information)search_adapter.getItem(position);
				Intent intent = new Intent();
				intent.setClass(ActivityHealthSearch.this, ActivityHealthDetail.class);
				intent.putExtra("content_id", info.getContent_id());
				ActivityHealthSearch.this.startActivity(intent);
			}
		});
	}
	
	/**
	 * 初始化头部UI
	 */
	private void init_top_bar(){
		head_btn_right = (ImageView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		
		head_title.setText("搜 索");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(ActivityPersonalInfo.RESULT_CANCEL);
				ActivityHealthSearch.this.finish();
			}
		});
	}

	
	/***
	 * ***********************  网络交互部分   ************************
	 */
	private Handler network_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.fragment_health_search_information_success){
				interface_app_search_health_information_resp resp = (interface_app_search_health_information_resp)msg.obj;
				if(resp!=null){
					List<table_health_information> list = resp.getList();
					if(list!=null && list.size()>0){
						search_adapter.removeAll();
						for(int i=0;i<list.size();i++){
							search_adapter.addItemTail(list.get(i));
						}
					}
				}
			}else if(msg.what == message_what_values.fragment_health_get_data_failed){
				Toast.makeText(ActivityHealthSearch.this, "查询失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	/**
	 * 搜索资讯
	 */
	private int f_search_health_info(){
		String key_string = search_key_edittext.getText().toString();
		if(key_string==null || key_string.length()==0){
			return -1;
		}
		interface_app_search_health_information_req req = new interface_app_search_health_information_req();
		req.setCondition(key_string);
		new thread_health_search_information_list(this, network_handler, req).start();
		return 0;
	}

}
