package com.xqj.lovebabies.activitys;

import java.util.ArrayList;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_activity_description_viewpager;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.listeners.listener_activity_description_on_click;
import com.xqj.lovebabies.listeners.listener_activity_description_on_page_change;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

//第一次启动的功能说明
public class ActivityDescription extends Activity {
	private ViewPager cmd_viewpager_container = null;
	private ImageButton cmd_imagebutton_enter = null;
	private ArrayList<View> list_view = null;
	private ImageView cmd_imageview_container = null;
	private Bitmap item_bitmap_imageview_bg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_description);
		init();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			
			item_bitmap_imageview_bg.recycle();
			cmd_imageview_container.destroyDrawingCache();
			list_view.clear();
			System.gc();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void init() {
		try {

			// 是第一次运行,显示viewpager

			list_view = new ArrayList<View>();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;// 图片压缩倍数
			// --
			cmd_imageview_container = new ImageView(this);
			item_bitmap_imageview_bg = BitmapFactory.decodeResource(getResources(), R.drawable.activity_description_viewpager_bg1, options);
			cmd_imageview_container.setImageBitmap(item_bitmap_imageview_bg);
			list_view.add(cmd_imageview_container);
			// --
			cmd_imageview_container = new ImageView(this);
			item_bitmap_imageview_bg = BitmapFactory.decodeResource(getResources(), R.drawable.activity_description_viewpager_bg2, options);
			cmd_imageview_container.setImageBitmap(item_bitmap_imageview_bg);
			list_view.add(cmd_imageview_container);
			// --
			cmd_imageview_container = new ImageView(this);
			item_bitmap_imageview_bg = BitmapFactory.decodeResource(getResources(), R.drawable.activity_description_viewpager_bg3, options);
			cmd_imageview_container.setImageBitmap(item_bitmap_imageview_bg);
			list_view.add(cmd_imageview_container);
			// --
			cmd_imageview_container = new ImageView(this);
			item_bitmap_imageview_bg = BitmapFactory.decodeResource(getResources(), R.drawable.activity_description_viewpager_bg4, options);
			cmd_imageview_container.setImageBitmap(item_bitmap_imageview_bg);
			list_view.add(cmd_imageview_container);
			// --
			cmd_viewpager_container = (ViewPager) findViewById(R.id.activity_description_viewpage_product_intro);
			cmd_viewpager_container.setVisibility(View.VISIBLE);
			cmd_viewpager_container.setAdapter(new adapter_activity_description_viewpager(list_view));
			cmd_viewpager_container.setOnPageChangeListener(new listener_activity_description_on_page_change(handler));
			// --
			cmd_imagebutton_enter = (ImageButton) findViewById(R.id.activity_description_imagebutton_enter);
			cmd_imagebutton_enter.setOnClickListener(new listener_activity_description_on_click(handler));
			cmd_imagebutton_enter.setBackgroundResource(R.drawable.activity_description_imagebutton_enter);
			// --

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == message_what_values.activity_description_imagebutton_enter_click) {
				// 点击体验按钮
				f_action_to_activity_login();
			}
			if (msg.what == message_what_values.activity_description_viewpager_container_page_change_hide_enter_button) {
				// 隐藏体验按钮
				f_action_hide_enter_button();
			}
			if (msg.what == message_what_values.activity_description_viewpager_container_page_change_show_enter_button) {
				// 显示体验按钮
				f_action_show_enter_button();
			}

		};
	};

	private void f_action_show_enter_button() {
		try {
			cmd_imagebutton_enter.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_hide_enter_button() {
		try {
			cmd_imagebutton_enter.setVisibility(View.GONE);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_to_activity_login() {
		try {
			PreferencesUtils.putBoolean(this, "is_first_launch", false);
			// --
			Intent intent = new Intent();
			intent.setClass(this, ActivityLogin.class);
			startActivity(intent);
			finish();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
