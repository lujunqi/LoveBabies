package com.xqj.lovebabies.fragments;

import java.io.File;
import java.util.*;

import cn.trinea.android.common.util.ImageUtils;
import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityBabyStudy;
import com.xqj.lovebabies.activitys.ActivityLogin;
import com.xqj.lovebabies.activitys.ActivityMain;
import com.xqj.lovebabies.activitys.ActivityRegister;
import com.xqj.lovebabies.adapters.adapter_fragment_menu_listview;
import com.xqj.lovebabies.commons.utils_common_cache_clear_manager;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.dbs_album_baby_growth;
import com.xqj.lovebabies.databases.dbs_baby_my_baby;
import com.xqj.lovebabies.databases.dbs_interaction_contacts;
import com.xqj.lovebabies.databases.dbs_interaction_message;
import com.xqj.lovebabies.databases.dbs_interaction_news;
import com.xqj.lovebabies.databases.dbs_interaction_notice;
import com.xqj.lovebabies.fragments.FragmentMyCenter.OnHeadIconModifiedListener;
import com.xqj.lovebabies.listeners.listener_fragment_menu_item_on_click;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentMenu extends Fragment {
	private View layout_main_container = null;
	private Bitmap bitmap_background = null;
	private ListView cmd_listview_menu = null;
	private adapter_fragment_menu_listview menu_listview_adapter = null;
	private ArrayList<HashMap<String, Object>> list;
	private HashMap<String, Object> map = null;
	
	private String lovebaby_cache_path;
	public FragmentMenu() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout_main_container = inflater.inflate(R.layout.fragment_menu, null);
		BitmapFactory.Options options = new Options();
		options.inSampleSize = 5;
		bitmap_background = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.fragment_menu_bg);
		layout_main_container.setBackgroundDrawable(ImageUtils.bitmapToDrawable(bitmap_background));

		//
		return layout_main_container;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			System.out.println("这里初始化menu-------------------------");
			init_menu();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void init_menu() {
		try {
			// --
			String user_nick_name = PreferencesUtils.getString(getActivity(), "user_nick_name");
			System.out.println("FragmentMenu-->user_nick_name-->"+user_nick_name);
			list = new ArrayList<HashMap<String, Object>>();
			map = new HashMap<String, Object>();
			map.put("menu_logo", 0);
			map.put("menu_name", user_nick_name);
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("menu_logo", R.drawable.fragment_menu_logo_zsabb);
			map.put("menu_name", "掌上爱宝贝");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("menu_logo", R.drawable.fragment_menu_logo_jkye);
			map.put("menu_name", "健康育儿");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("menu_logo", R.drawable.fragment_menu_logo_ddxx);
			map.put("menu_name", "点读学习");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("menu_logo", R.drawable.fragment_menu_logo_ymtx);
			map.put("menu_name", "疫苗提醒");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("menu_logo", R.drawable.fragment_menu_logo_bbxc);
			map.put("menu_name", "宝宝相册");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("menu_logo", R.drawable.fragment_menu_logo_jyhd);
			map.put("menu_name", "家园互动");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("menu_logo", R.drawable.fragment_menu_logo_check);
			map.put("menu_name", "晨检日记");
			list.add(map);
			map = new HashMap<String, Object>();
			map.put("menu_logo", R.drawable.fragment_menu_logo_book);
			map.put("menu_name", "健康档案");
			list.add(map);
			
			
			map = new HashMap<String, Object>();
			map.put("menu_logo", R.drawable.fragment_menu_logo_tcdl);
			map.put("menu_name", "退出登录");
			list.add(map);

			// --
			menu_listview_adapter = new adapter_fragment_menu_listview(list, getActivity());
			cmd_listview_menu = (ListView) layout_main_container.findViewById(R.id.fragment_menu_listview_menu);
			cmd_listview_menu.setAdapter(menu_listview_adapter);
			cmd_listview_menu.setOnItemClickListener(new listener_fragment_menu_item_on_click(handler, list));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == message_what_values.fragment_menu_listview_on_item_click) {
				f_action_menu_item_click_handler(msg);
			}
		};
	};

	private void exit_login() {
		try {
			// 清除缓存
			lovebaby_cache_path = "";
			lovebaby_cache_path += PreferencesUtils.getString(getActivity(), "sys_path_sd_card");
			lovebaby_cache_path += PreferencesUtils.getString(getActivity(), "sys_path_app_folder");
			File file = new File(lovebaby_cache_path);
			utils_common_cache_clear_manager.deleteAllFiles(file);
			// 清除数据库
			utils_common_cache_clear_manager.deleteDBDatas(getActivity());
			
			// 清除PreferenceUtils
			PreferencesUtils.putString(getActivity(), "user_icon", "");
			PreferencesUtils.putString(getActivity(), "user_nick_name", "");
			PreferencesUtils.putString(getActivity(), "user_phone", "");
			PreferencesUtils.putString(getActivity(), "user_email", "");
			
			// 跳转
			Intent intent = new Intent();
			intent.setClass(getActivity(), ActivityLogin.class);
			getActivity().startActivity(intent);
			getActivity().finish();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_menu_item_click_handler(Message msg) {
		try {
			int item_logo_id = msg.arg1;
			switch (item_logo_id) {
			case R.drawable.fragment_menu_logo_bbxc:
				// 宝宝相册
				ActivityMain.switch_fragment(new FragmentAlbum());
				break;
			case R.drawable.fragment_menu_logo_jkye:
				// 健康育儿
				ActivityMain.switch_fragment(new FragmentHealth());
				break;
			case R.drawable.fragment_menu_logo_ddxx:
				// 点读学习
				System.out.println("--------------点读学习------------");
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityBabyStudy.class);
				startActivity(intent);
				break;
			case R.drawable.fragment_menu_logo_jyhd:
				// 家园互动
				ActivityMain.switch_fragment(new FragmentInteraction());
				break;
			case R.drawable.fragment_menu_logo_check:
				// 晨检日记
				ActivityMain.switch_fragment(new FragmentCheck());
				break;
			case R.drawable.fragment_menu_logo_book:
				// 健康档案
				ActivityMain.switch_fragment(new FragmentHealthDoc());
				break;	
				
			case R.drawable.fragment_menu_logo_tcdl:
				// 退出登录
				exit_login();
				break;
			case R.drawable.fragment_menu_logo_ymtx:
				// 疫苗提醒
				ActivityMain.switch_fragment(new FragmentYmtx());
				break;
			case R.drawable.fragment_menu_logo_zsabb:
				// 掌上爱宝贝
				ActivityMain.switch_fragment(new FragmentZsabb());
				break;
			
			case 0:
				// 个人中心
				OnHeadIconModifiedListener onHeadIconModifiedListener = new OnHeadIconModifiedListener() {
					@Override
					public void updateUserIcon(Bitmap bitmap) {
						menu_listview_adapter.getItem(0).put("user_icon", bitmap);
						System.out.println("list.get(0).put(bitmap)");
						menu_listview_adapter.notifyDataSetChanged();
					}
					@Override
					public void notifyDataChanged() {
						menu_listview_adapter.notifyDataSetChanged();
					}
				};
				FragmentMyCenter myCenterFragment = new FragmentMyCenter();
				myCenterFragment.setOnHeadIconModifiedListener(onHeadIconModifiedListener);
				ActivityMain.switch_fragment(myCenterFragment);
				break;
			default:
				break;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
