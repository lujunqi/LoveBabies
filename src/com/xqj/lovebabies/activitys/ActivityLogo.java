package com.xqj.lovebabies.activitys;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.listeners.listener_activity_logo_animation;
import com.xqj.lovebabies.structures.interface_app_user_login_req;
import com.xqj.lovebabies.structures.interface_app_user_login_resp;
import com.xqj.lovebabies.threads.thread_common_system_user_login;

//启动程序首先显示的logo
public class ActivityLogo extends Activity {
	private utils_common_tools tools = new utils_common_tools();
	private ImageView cmd_imageview_logo_container = null;
	private TextView cmd_textview_app_version = null;
	private AlphaAnimation animation_show_logo = null;
	private Bitmap bitmap_logo = null;
	private interface_app_user_login_req app_user_login_req = null;
	private interface_app_user_login_resp app_user_login_resp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_logo);
		utils_picture_caches.getInstance().init(this);// 初始化图片缓存
		init_folder_env();
		init();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			bitmap_logo.recycle();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void init() {
		try {
			// --
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;// 图片压缩倍数
			bitmap_logo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher, options);

			// --
			animation_show_logo = new AlphaAnimation(0.0f, 1.0f);
			animation_show_logo.setDuration(5000);
			animation_show_logo.setAnimationListener(new listener_activity_logo_animation(handler));
			// --
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
			cmd_imageview_logo_container = (ImageView) findViewById(R.id.activity_logo_imageview_logo_container);
			cmd_imageview_logo_container.setLayoutParams(params);
			cmd_imageview_logo_container.setImageBitmap(bitmap_logo);
			cmd_imageview_logo_container.setAnimation(animation_show_logo);
			// --
			cmd_textview_app_version = (TextView) findViewById(R.id.activity_logo_textview_app_version);
			cmd_textview_app_version.setText("掌上爱宝贝V2.0");
			cmd_textview_app_version.setTextSize(20);
			cmd_textview_app_version.setTextColor(Color.GREEN);
			cmd_textview_app_version.setAnimation(animation_show_logo);
			// --

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void init_folder_env() {
		try {

			String sd_path = tools.get_application_dir_path(this);
			PreferencesUtils.putString(this, global_contants.SYS_PATH_SD_CARD, sd_path);
			PreferencesUtils.putString(this, global_contants.SYS_PATH_APP_FOLDER, "/Lovebabies");
			PreferencesUtils.putString(this, global_contants.SYS_PATH_CACHE, "/cache");
			PreferencesUtils.putString(this, global_contants.SYS_PATH_TEMP, "/temp");
			PreferencesUtils.putString(this, global_contants.SYS_PATH_CRASH, "/crash");
			PreferencesUtils.putString(this, global_contants.SYS_PATH_UP, "/up");
			// --
			String app_path = PreferencesUtils.getString(this, global_contants.SYS_PATH_SD_CARD) + PreferencesUtils.getString(this, "sys_path_app_folder");
			String cache_path = app_path + PreferencesUtils.getString(this, global_contants.SYS_PATH_CACHE);
			String temp_path = app_path + PreferencesUtils.getString(this, global_contants.SYS_PATH_TEMP);
			String crash_path = app_path + PreferencesUtils.getString(this, global_contants.SYS_PATH_CRASH);
			String up_path = app_path + PreferencesUtils.getString(this, global_contants.SYS_PATH_UP);
			// --
			File folder_root = new File(app_path);
			if (!folder_root.exists()) {
				folder_root.mkdirs();
			}
			// --
			File folder_cache = new File(cache_path);
			if (!folder_cache.exists()) {
				folder_cache.mkdirs();
			}
			// --
			File folder_temp = new File(temp_path);
			if (!folder_temp.exists()) {
				folder_temp.mkdirs();
			}
			//--
			File folder_crash = new File(crash_path);
			if (!folder_crash.exists()) {
				folder_crash.mkdirs();
			}
			//--
			File up_crash = new File(up_path);
			if (!up_crash.exists()) {
				up_crash.mkdirs();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == message_what_values.activity_logo_animation_end) {
				// 开机logo动画播放完毕后
				f_action_logo_animation_end();
			}
			if (msg.what == message_what_values.activity_login_app_user_login_result_success) {
				// 登录成功
				app_user_login_resp = (interface_app_user_login_resp) msg.obj;
				f_action_auto_login_success(app_user_login_resp, app_user_login_req);
			}

			if (msg.what == message_what_values.activity_login_app_user_login_result_failure) {
				// 登录失败
				f_action_auto_login_failure();
			}
		};
	};

	private void f_action_logo_animation_end() {
		if (PreferencesUtils.getBoolean(this, "is_first_launch", true)) {
			f_action_to_activity_description();
		} else {
			if(!utils_common_tools.get_network_status(this)){
				//没有网络,离线登录
				String user_id = PreferencesUtils.getString(this, "user_id");
				String user_type = PreferencesUtils.getString(this, "user_type");
				String user_name = PreferencesUtils.getString(this, "user_name");
				String user_password = PreferencesUtils.getString(this, "user_password");
				if(user_id!=null && user_id.length()>0 
						&&user_type!=null && user_type.length()>0
						&&user_name!=null && user_name.length()>0
						&&user_password!=null && user_password.length()>0){
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.setClass(this, ActivityMain.class);
					startActivity(intent);
					finish();
				}else{
					f_action_to_auto_login();
				}
			}else{
				f_action_to_auto_login();
			}
		}
	}

	private void f_action_to_activity_description() {
		try {
			Intent intent = new Intent();
			intent.setClass(this, ActivityDescription.class);
			startActivity(intent);
			finish();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_to_auto_login() {
		try {
			app_user_login_req = new interface_app_user_login_req();
			app_user_login_req.setUser_name(PreferencesUtils.getString(this, "user_name"));
			app_user_login_req.setUser_password(PreferencesUtils.getString(this, "user_password"));
			app_user_login_req.setLogin_code(tools.get_phone_imei(this));
			new thread_common_system_user_login(handler, app_user_login_req).start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_auto_login_success(interface_app_user_login_resp resp, interface_app_user_login_req req) {
		try {
			PreferencesUtils.putString(this, "user_id", resp.getUser_id());
			PreferencesUtils.putString(this, "user_icon", resp.getUser_icon());
			PreferencesUtils.putString(this, "user_type", resp.getUser_type());
			PreferencesUtils.putString(this, "user_name", req.getUser_name());
			PreferencesUtils.putString(this, "user_password", req.getUser_password());
			// --
			Intent intent = new Intent();

			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.setClass(this, ActivityMain.class);
			startActivity(intent);
			finish();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_auto_login_failure() {
		try {
			Intent intent = new Intent();
			intent.setClass(this, ActivityLogin.class);
			startActivity(intent);
			finish();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
