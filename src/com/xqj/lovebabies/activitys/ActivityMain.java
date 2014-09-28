package com.xqj.lovebabies.activitys;

import java.util.HashSet;
import java.util.Set;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;

import cn.jpush.android.api.JPushInterface;
import cn.trinea.android.common.util.PreferencesUtils;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.fragments.FragmentEwen;
import com.xqj.lovebabies.fragments.FragmentMenu;
import com.xqj.lovebabies.fragments.FragmentZsabb;
import com.xqj.lovebabies.listeners.listener_activity_main_on_click;
import com.xqj.lovebabies.services.service_xmpp_receiver;
import com.xqj.lovebabies.threads.thread_kill_app;
import com.xqj.lovebabies.threads.thread_kill_app.kill_app_listener;
import com.xqj.lovebabies.widgets.TopActionBar;

public class ActivityMain extends SlidingFragmentActivity{

	private Fragment mContent = null;
	private Fragment fragment_menu = null;
	public static TopActionBar main_action_bar = null;
	public static FragmentManager fragment_manager = null;
	public static SlidingMenu sliding_menu = null;
	private String to_where = null;
	private Intent intent_message_receiver = null;
	public static Set<String> setKey = new HashSet<String>();

	private kill_app_listener kill_app_listener = new kill_app_listener() {
		@Override
		public void kill_app() {
			exitOperator();
		}
	};
	
//	private GestureDetector gestureDetector;
	
	public ActivityMain() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_main);
			// 初始化slidingmenu
			init_slidingmenu();
			init();
			
			thread_kill_app.getInstance().setActivity(this);
			thread_kill_app.getInstance().setListener(kill_app_listener);
			thread_kill_app.getInstance().start();//启动进程
			
			String user_id = PreferencesUtils.getString(this, "user_id");
			JPushInterface.setAlias(this, user_id, null);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
//		gestureDetector = new GestureDetector(gestureListner);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		// 初始化图片缓存
//		utils_picture_caches.getInstance().init(this);
		// --启动一些service
		start_local_services();
		super.onResume();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			// 停止service
			this.stopService(intent_message_receiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void init() {
		try {
			main_action_bar = (TopActionBar) findViewById(R.id.activity_main_actionbar_topbar);
			main_action_bar.getCmd_imagebutton_menu().setOnClickListener(
					new listener_activity_main_on_click(handler));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void init_slidingmenu() {
		try {
			sliding_menu = getSlidingMenu();
			fragment_manager = getSupportFragmentManager();
			mContent = new FragmentZsabb();
			setContentView(R.layout.activity_main);
			// 设置主界面
			fragment_manager
					.beginTransaction()
					.replace(R.id.activity_main_linearlayout_container,
							mContent).commit();
			// 设置菜单界面
			setBehindContentView(R.layout.fragment_menu);
			fragment_menu = new FragmentMenu();
			fragment_manager
					.beginTransaction()
					.replace(R.id.fragment_menu_linearlayout_container,
							fragment_menu).commit();
			// 设置滑动菜单的属性值
			sliding_menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			sliding_menu
					.setShadowDrawable(R.drawable.shape_sliding_menu_shadow);
			sliding_menu.setShadowWidth(30);
			sliding_menu.setBehindOffset(90);
			sliding_menu.setFadeDegree(0.35f);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void start_local_services() {
		try {
			intent_message_receiver = new Intent();
			intent_message_receiver.setClass(this, service_xmpp_receiver.class);
			this.startService(intent_message_receiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		try {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				moveTaskToBack(true);
//				exitFrameDialog();
			}
			if (keyCode == KeyEvent.KEYCODE_MENU) {
				getSlidingMenu().toggle();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return super.onKeyDown(keyCode, event);

	}

	public static FragmentEwen fragmentEwen;

	public static void switch_fragment(Fragment fragment) {
		try {
			fragment_manager
					.beginTransaction()
					.replace(R.id.activity_main_linearlayout_container,
							fragment).commit();
			Log.d("line150", fragment_manager.getBackStackEntryCount() + "==");
			sliding_menu.toggle();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public static void switch_fragment(Fragment fragment, boolean flag) {
		try {
			fragment_manager
					.beginTransaction()
					.replace(R.id.activity_main_linearlayout_container,
							fragment).commit();
			if (flag) {
				sliding_menu.toggle();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void show_or_hide_deagment() {
		try {
			sliding_menu.toggle();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == message_what_values.widget_top_action_bar_button_menu_click) {
				ActivityMain.show_or_hide_deagment();
			}
		};
	};
	
	/**
	 * 程序退出
	 */
	public void exitFrameDialog(){
		AlertDialog.Builder builder = new Builder(this);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						    dialog.dismiss();
						    exitOperator();
					}
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
	}
	
	public void exitOperator(){
//		Intent i = new Intent();  
//		i.setClass(this, AppService.class);  
//		this.stopService(i); 				// 停止后台服务
	  	finish();
		System.exit(0);
		//杀进程 完全退出
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
}
