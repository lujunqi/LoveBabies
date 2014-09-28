package com.xqj.lovebabies.activitys;

import java.io.File;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_cache_clear_manager;
import com.xqj.lovebabies.commons.utils_common_file_size_tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMyCenterSetting extends Activity {

	// topBar
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	// main page
	private RelativeLayout feedback_layout;
	private RelativeLayout clear_cache_layout;
	private RelativeLayout check_update_layout;
	private RelativeLayout about_us_layout;
	private Button logout_btn;
	
	private String lovebaby_cache_path;
	
	private Dialog clean_cache_dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_setting);
		
		lovebaby_cache_path = "";
		lovebaby_cache_path += PreferencesUtils.getString(this, "sys_path_sd_card");
		lovebaby_cache_path += PreferencesUtils.getString(this, "sys_path_app_folder");
		init_top_bar();
		init_main_page();
	}

	/**
	 * ��ʼ��ͷ��
	 */
	private void init_top_bar(){
		head_btn_right = (ImageView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		
		head_title.setText("����");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityMyCenterSetting.this.finish();
			}
		});
	}
	
	// ��ʼ�� main page
	private void init_main_page(){
		feedback_layout = (RelativeLayout)findViewById(R.id.my_center_feedback_layout);
		clear_cache_layout = (RelativeLayout)findViewById(R.id.my_center_clear_cache_layout);
		check_update_layout = (RelativeLayout)findViewById(R.id.my_center_check_update_layout);
		about_us_layout = (RelativeLayout)findViewById(R.id.my_center_about_us_layout);
		logout_btn = (Button)findViewById(R.id.my_center_setting_logout_btn);
		
		feedback_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ActivityMyCenterSetting.this, "�û�����", Toast.LENGTH_SHORT).show();
			}
		});
		clear_cache_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clear_cache_alert();
			}
		});
		check_update_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ActivityMyCenterSetting.this, "����°汾", Toast.LENGTH_SHORT).show();
			}
		});
		about_us_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityMyCenterSetting.this, ActivityMyCenterSettingAboutUs.class);
				ActivityMyCenterSetting.this.startActivity(intent);
			}
		});
		logout_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ActivityMyCenterSetting.this, "�˳���¼", Toast.LENGTH_SHORT).show();
				ActivityMyCenterSetting.this.finish();
			}
		});
	}
	
	// ������
	private void clear_cache_alert(){
		double cache_size = utils_common_file_size_tool.getFileOrFilesSize(lovebaby_cache_path, 
				utils_common_file_size_tool.SIZETYPE_MB);
		clean_cache_dialog = new AlertDialog.Builder(this)
				.setTitle("������").setMessage("�ѷ���"+cache_size+"M���棬��ȷ��Ҫ�����")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						f_clear_cache();
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						f_dismiss_dialog();
					}
				}).create();
		clean_cache_dialog.show();
	}
	
	// ������
	private void f_clear_cache(){
		File file = new File(lovebaby_cache_path);
		utils_common_cache_clear_manager.deleteAllFiles(file);// ɾ�������ļ�
		// ������ݿ⻺���¼
		utils_common_cache_clear_manager.deleteDBDatas(this);
		
		Toast.makeText(this, "�������", Toast.LENGTH_SHORT).show();
	}
	// �ر���ʾ��
	private void f_dismiss_dialog(){
		clean_cache_dialog.dismiss();
	}
}
