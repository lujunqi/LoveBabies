package com.xqj.lovebabies.fragments;

import java.util.List;
import java.util.Vector;

import org.apache.http.HttpStatus;

import cn.trinea.android.common.util.PreferencesUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityAddGrowthSelector;
import com.xqj.lovebabies.activitys.ActivityMain;
import com.xqj.lovebabies.activitys.ActivityViewGrowthInfo;
import com.xqj.lovebabies.adapters.adapter_fragment_album_listview;
import com.xqj.lovebabies.adapters.adapter_fragment_album_popwindow;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_density_transform;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.dbs_album_baby_growth;
import com.xqj.lovebabies.databases.dbs_baby_my_baby;
import com.xqj.lovebabies.databases.table_album_baby_growth;
import com.xqj.lovebabies.databases.table_album_gridview_photo_path;
import com.xqj.lovebabies.databases.table_album_my_baby;
import com.xqj.lovebabies.structures.interface_app_get_all_my_baby_req;
import com.xqj.lovebabies.structures.interface_app_get_all_my_baby_resp;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_by_page_req;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_by_page_resp;
import com.xqj.lovebabies.structures.interface_app_get_my_baby_req;
import com.xqj.lovebabies.structures.interface_app_get_my_baby_resp;
import com.xqj.lovebabies.threads.thread_album_get_all_my_baby_list;
import com.xqj.lovebabies.threads.thread_album_get_baby_growth_by_page_list;
import com.xqj.lovebabies.threads.thread_album_get_my_baby_list;
import com.xqj.lovebabies.widgets.UIListView;
import com.xqj.lovebabies.widgets.UIListView.OnRefreshListener;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class FragmentAlbum extends Fragment {
	// 读取进度
	private Dialog dialog;
//	private boolean bl_show_dialog = true;// 能否显示进度
	
	// PopupWindow对象
	private PopupWindow album_pop_window = null;
	private View popupWindowPage = null;
	private LinearLayout album_pop_window_layout;
	// 自定义Adapter
	private adapter_fragment_album_popwindow pop_window_adapter = null;
	private final int SINGLE_POP_ITEM_HEIGHT = 38;
	private int pop_window_height = 0;// popWindow的高度
	private int pop_window_width = 220;// popWindow的宽度
	private int pop_window_margin_left = 0;// popWindow 相对左边缘的距离
	private ListView listview_pop_window = null;
	
	private View main_view = null;
	
	private adapter_fragment_album_listview album_adapter = null;
	
	private interface_app_get_all_my_baby_req get_my_baby_req = null;
	private interface_app_get_all_my_baby_resp get_my_baby_resp = null;

	private List<table_album_my_baby> my_baby_list = null;
	private Vector<table_album_my_baby> popwindow_baby_list = new Vector<table_album_my_baby>();

//	private boolean bl_pop_window_init = false;// 判断 popWindow 是否已经初始化
	
	private UIListView listview_album;
	private View bottom_view;
	private interface_app_get_baby_growth_by_page_req get_baby_growth_by_page_req = null;
	private interface_app_get_baby_growth_by_page_resp get_baby_growth_by_page_resp = null;
	private List<table_album_baby_growth> baby_growth_list = null;
	
	private String current_baby_id = "0";
	private int page_number = 1;// 宝贝成长记录分页查询 页码
	
	private LayoutInflater inflater;
	
	private dbs_baby_my_baby dbs_my_baby;
	private dbs_album_baby_growth dbs_baby_growth;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pop_window_width = utils_density_transform.dip2px(getActivity(), 250);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.inflater = inflater;
		try{
			main_view = inflater.inflate(R.layout.fragment_album, container, false);
			init_action_bar();
			init_main_view();
			// 初始化popWindow
			f_init_pop_window_wedget();
			// 先查本地宝贝
			boolean query_local_baby = f_action_get_my_local_baby_list();
			if(utils_common_tools.get_network_status(getActivity())){// 网络连接正常
				// 再联网查询最新宝贝
				f_action_get_my_baby_list();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return main_view;
	}
	
	private void init_action_bar() {
		try {
			
			ActivityMain.main_action_bar.getCmd_textview_title_name().setText("");
			
			ActivityMain.main_action_bar.getCmd_imagebutton_more().setVisibility(View.VISIBLE);
			ActivityMain.main_action_bar.getCmd_imagebutton_more().setImageResource(R.drawable.add_baby_growth_selector);
			ActivityMain.main_action_bar.getCmd_imagebutton_more().setOnClickListener(addGrowthBtnOnClickListener);		
			
			ActivityMain.main_action_bar.getCmd_imageview_title_icon().setVisibility(View.VISIBLE);
//			ActivityMain.main_action_bar.getCmd_imageview_title_icon().setImageResource(R.drawable.default_logo);			
			
			ActivityMain.main_action_bar.getCmd_imageview_title_action().setVisibility(View.VISIBLE);
			ActivityMain.main_action_bar.getCmd_imageview_title_action().setImageResource(R.drawable.growth_01);
		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 初始化主页面
	 */
	private void init_main_view() {
		Vector<table_album_baby_growth> list = new Vector<table_album_baby_growth>();
		listview_album = (UIListView)main_view.findViewById(R.id.fragment_album_listview);
		listview_album.setCacheColorHint(Color.TRANSPARENT);
		listview_album.setAlwaysDrawnWithCacheEnabled(true); 

		bottom_view = (View)inflater.inflate(R.layout.bottom_view_more, null);
		bottom_view.setVisibility(View.INVISIBLE);
		listview_album.addFooterView(bottom_view);
		album_adapter = new adapter_fragment_album_listview(list, getActivity());
		listview_album.setAdapter(album_adapter);
		listview_album.setOnItemClickListener(album_growth_item_onitem_click_listener);
		listview_album.setonRefreshListener(new OnRefreshListener() {// 下拉刷新
			@Override
			public void onRefresh() {
				page_number=1;
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							// 网络获取成长记录
							f_action_get_baby_growth_by_page(current_baby_id, page_number, 1);							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}
					protected void onPostExecute(Void result) {
						album_adapter.notifyDataSetChanged();
						listview_album.onRefreshComplete();
					}
				}.execute(null);
				bottom_view.setVisibility(View.VISIBLE);
			}
		});
		bottom_view.setOnClickListener(new OnClickListener() {// 查看更多
			@Override
			public void onClick(View arg0) {
				page_number++;
				f_action_get_baby_growth_by_page(current_baby_id, page_number, 0);
			}
		});
	}
	
	/**
	 * 处理从互联网拉取的数据
	 */
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// --
			if (msg.what == message_what_values.activity_album_my_baby_get_data_success) {
				get_my_baby_resp = (interface_app_get_all_my_baby_resp)msg.obj;
				if(get_my_baby_resp!=null){
					f_list_my_baby(get_my_baby_resp.getList());
				}
			}else if (msg.what == message_what_values.activity_album_baby_growth_by_page_get_data_success) {
				f_list_baby_growth(msg);
//				f_close_connect_dialog();
			}else if(msg.what == message_what_values.activity_album_get_data_timeout){
				Toast.makeText(getActivity(), "查询失败，网络出问题啦", Toast.LENGTH_LONG).show();
//				f_close_connect_dialog();
			}
		};
	};
	
//	/**
//	 * 处理添加宝宝对话框用户点击结果
//	 */
//	private Handler add_baby_handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			if (msg.what == AlbumAddBabyAlerDialog.POSITIVE_KEY){// 添加宝宝
//				
//			}else if (msg.what == AlbumAddBabyAlerDialog.NEGATIVE_KEY){//输入邀请码
//				// 弹出输入界面
//				Intent intent = new Intent(getActivity(), ActivityAddBabyByEcode.class);
//				intent.putExtra("baby_id", current_baby_id);
//				startActivity(intent);
//			}	
//		}
//	};
//	
	/**
	 * 从服务端分页查询宝贝成长记录
	 */
	private void f_action_get_baby_growth_by_page(String baby_id, int page_num, int bl_get_new){
		try {
			// 网络上返回指定页的数据
			String user_id = PreferencesUtils.getString(getActivity(), "user_id");
			get_baby_growth_by_page_req = new interface_app_get_baby_growth_by_page_req();
			get_baby_growth_by_page_req.setUser_id(user_id);
			get_baby_growth_by_page_req.setBaby_id(baby_id);
			get_baby_growth_by_page_req.setPage_index(String.valueOf(page_num));
			new thread_album_get_baby_growth_by_page_list(getActivity(), handler, get_baby_growth_by_page_req, bl_get_new).start();
//			if(bl_show_dialog){
//				f_show_connect_dialog();
//			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 查询本地宝贝成长记录
	 */
	private boolean f_action_local_get_baby_growth_list(){
		try{
			dbs_baby_growth = new dbs_album_baby_growth(getActivity());
			baby_growth_list = dbs_baby_growth.do_select_data("select * from(select * from t_album_baby_growth where baby_id='"+current_baby_id+"' order by record_time desc) limit 0,8");
			if(baby_growth_list!=null && baby_growth_list.size()>0){
				album_adapter.removeAll();
				for(int i=0; i<baby_growth_list.size(); i++){
					table_album_baby_growth temp = baby_growth_list.get(i);
					System.out.println(temp.getBaby_id()+
							"--------------Local----------------"+temp.getRecord_id()+"--"+
							temp.getWord_record());
					if(temp.getPic_name()!=null && temp.getPic_name().length()>0){
						String[] pic_array = temp.getPic_name().split(",");						
						Vector<table_album_gridview_photo_path> pic_list = new Vector<table_album_gridview_photo_path>();
						for(String pic_path : pic_array){
							table_album_gridview_photo_path photo_path = new table_album_gridview_photo_path();
							photo_path.setImage_path(pic_path);
							pic_list.add(photo_path);
						}
						temp.setPic_list(pic_list);
					}
					album_adapter.addItemTail(temp);
				}
				return true;
			}else{//网络查询
				page_number = 1;
				f_action_get_baby_growth_by_page(current_baby_id, page_number, 1);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 显示宝贝成长相册
	 */
	private void f_list_baby_growth(Message msg){
		get_baby_growth_by_page_resp = (interface_app_get_baby_growth_by_page_resp)msg.obj;
		baby_growth_list = get_baby_growth_by_page_resp.getList();
		
		if(get_baby_growth_by_page_resp.getResult_code() == HttpStatus.SC_OK){
			if(msg.arg1 == 1){// 如果是查询最新数据,清理数据重新加载，否则直接从后面添加数据
				album_adapter.removeAll();
			}else{
				if(baby_growth_list==null || baby_growth_list.size()==0){
					Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
					bottom_view.setVisibility(View.GONE);
				}
			}
			if(baby_growth_list!=null && baby_growth_list.size()>0){
				for(int i=0; i<baby_growth_list.size(); i++){
					table_album_baby_growth temp = baby_growth_list.get(i);
					System.out.println(temp.getBaby_id()+
							"--------------NetWork----------------"+temp.getRecord_id()+"--"+
							temp.getWord_record());
					if(temp.getPic_name()!=null && temp.getPic_name().length()>0){
						String[] pic_array = temp.getPic_name().split(",");						
						Vector<table_album_gridview_photo_path> pic_list = new Vector<table_album_gridview_photo_path>();
						for(String pic_path : pic_array){
							table_album_gridview_photo_path photo_path = new table_album_gridview_photo_path();
							photo_path.setImage_path(pic_path);
							pic_list.add(photo_path);
						}
						temp.setPic_list(pic_list);
					}
					album_adapter.addItemTail(temp);
				}
			}
		}else{
			Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * 从本地查询我的宝贝
	 */
	private boolean f_action_get_my_local_baby_list(){
		try{
			dbs_my_baby = new dbs_baby_my_baby(getActivity());
			my_baby_list = dbs_my_baby.do_select_data("select * from t_baby_my_baby order by baby_id desc");
			if(my_baby_list!=null && my_baby_list.size()>0){
				for(int k=0; k<my_baby_list.size(); k++){
					table_album_my_baby baby_temp = my_baby_list.get(k);
					System.out.println(baby_temp.getBaby_id()+
							"--------------Local------------------"+
							baby_temp.getBaby_name());
					
					popwindow_baby_list.add(baby_temp);
				}
				System.out.println("Local Baby-->"+popwindow_baby_list.size());
				// 修改TopBar，显示宝宝信息
				System.out.println("my_baby_list.get(0).getBaby_pic()---->"+my_baby_list.get(0).getBaby_pic());
				String babyName = my_baby_list.get(0).getBaby_name();
				String babyPic = my_baby_list.get(0).getBaby_pic();
				current_baby_id = my_baby_list.get(0).getBaby_id();
				System.out.println("Local current_baby_id-->"+my_baby_list.get(0).getBaby_id());
				try{
					f_change_top_bar(babyName, babyPic);
					// 设置popWindow的宽高
					f_set_pop_window_height();
					// 查询第一个宝贝的相册
					page_number = 1;
					f_action_local_get_baby_growth_list();// 先查询本地成长记录
//					f_action_get_baby_growth_by_page(current_baby_id , page_number, 1);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 从服务端查询我的宝贝
	 */
	private void f_action_get_my_baby_list(){
		System.out.println("--------------------从服务端查询我的宝贝---------------------");
		try {
			// 网络上返回指定页的数据
			String user_id = PreferencesUtils.getString(getActivity(), "user_id");
			get_my_baby_req = new interface_app_get_all_my_baby_req();
			get_my_baby_req.setUser_id(user_id);
			new thread_album_get_all_my_baby_list(getActivity(), handler, get_my_baby_req).start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 显示从服务端查询的我的宝贝
	 */
	private void f_list_my_baby(List<table_album_my_baby> list){
		my_baby_list = get_my_baby_resp.getList();
		if(my_baby_list!=null && my_baby_list.size()>0){
			popwindow_baby_list.removeAllElements();
			for(int k=0; k<my_baby_list.size(); k++){
				table_album_my_baby baby_temp = my_baby_list.get(k);
				System.out.println(baby_temp.getBaby_id()+
						"--------------------------------"+
						baby_temp.getBaby_name());
				
				popwindow_baby_list.add(baby_temp);
			}
//			my_baby_name_list.add("添加宝宝");//添加宝宝
//			my_baby_name_list.add("邀请亲友");//邀请亲友
//			my_baby_name_list.add("输入邀请码");//输入邀请码
			
			
			// 修改TopBar，显示宝宝信息
			System.out.println("my_baby_list.get(0).getBaby_pic()---->"+my_baby_list.get(0).getBaby_pic());
			String babyName = my_baby_list.get(0).getBaby_name();
			String babyPic = my_baby_list.get(0).getBaby_pic();
			String pre_baby_id = current_baby_id;
			current_baby_id = my_baby_list.get(0).getBaby_id();
			System.out.println("network--current-baby-id-->"+current_baby_id);
			try{
				f_change_top_bar(babyName, babyPic);
				
				// 设置popWindow的宽高
				f_set_pop_window_height();
				
//				f_popup_windw_showing();
				// 查询第一个宝贝的相册
				page_number = 1;
				
				if(!pre_baby_id.equals(current_baby_id)){// 如果当前baby_id与原baby_id不一致，重新查询成长记录
					System.out.println("f_action_local_get_baby_growth_list 当前baby_id"+current_baby_id+"与原baby_id"+pre_baby_id+"不一致");
					f_action_local_get_baby_growth_list();
				}
//				f_action_get_baby_growth_by_page(current_baby_id , page_number, 1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	// 设置popWindow的宽高
	private void f_set_pop_window_height(){
		pop_window_height = (my_baby_list.size()+3)*SINGLE_POP_ITEM_HEIGHT + 2;
		pop_window_height = utils_density_transform.dip2px(getActivity(), pop_window_height);
		// 设置popWindow的宽高
		LayoutParams lp = 
				(LayoutParams) album_pop_window_layout.getLayoutParams();  
		lp.height=pop_window_height;  
		lp.width=pop_window_width;  
		album_pop_window_layout.setLayoutParams(lp);  
		album_pop_window.setContentView(popupWindowPage);  
		album_pop_window.setHeight(LayoutParams.WRAP_CONTENT);  
		album_pop_window.setWidth(LayoutParams.WRAP_CONTENT);  
		album_pop_window.setFocusable(true);  
		album_pop_window.setOutsideTouchable(true);  
	}
	
	/**
	 * 更新TopBar
	 * @param babyName
	 * @param babyPic
	 */
	private void f_change_top_bar(String babyName, String babyPic){
		try{
			ActivityMain.main_action_bar.getCmd_textview_title_name().setText(babyName);
			if(babyPic!=null && babyPic.length()>0){
				String imgurl = network_interface_paths.get_project_root + babyPic;
//				DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
////				builder.showImageOnLoading(R.drawable.default_logo);
//				builder.showImageForEmptyUri(R.drawable.default_logo);
//				builder.cacheInMemory(false);
//				builder.cacheOnDisk(true);
//				builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
////				builder.displayer(new RoundedBitmapDisplayer(20));
//				DisplayImageOptions options = builder.build();
//				ImageLoader.getInstance().displayImage(imgurl, ActivityMain.main_action_bar.getCmd_imageview_title_icon(), options);
				utils_common_tools.f_display_Image(getActivity(), 
						ActivityMain.main_action_bar.getCmd_imageview_title_icon(), imgurl,R.drawable.default_logo,
						R.drawable.default_logo, ImageScaleType.IN_SAMPLE_INT);
			}else{
				ActivityMain.main_action_bar.getCmd_imageview_title_icon().setImageResource(R.drawable.default_logo);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * ***********************  进度 等待 部分  **********************************
	 */
//	/**
//	 * 显示进度条
//	 */
//	private void f_show_connect_dialog(){
//		bl_show_dialog = false;
//		dialog = progressDialog("请稍等", "正在读取数据中..");
//		dialog.show();
//	}
//	/**
//	 * 构造进度条
//	 */
//	private Dialog progressDialog(String title, String msg) {
//		ProgressDialog dialog = new ProgressDialog(getActivity());
//		dialog.setMessage(msg);
//		return dialog;
//	}
//	/**
//	 * 隐藏进度条
//	 */
//	private void f_close_connect_dialog(){
//		if(dialog!=null){
//			dialog.dismiss();
//		}
//		bl_show_dialog = true;
//	}

	
	
	/************************** PopWindow部分 **************************************/
	
	/** * 初始化PopWindow界面控件 */
	private void f_init_pop_window_wedget() {
		// 初始化Handler,用来处理消息
//		int width = navCenterLay.getWidth();
		// 设置点击下拉箭头图片事件，点击弹出PopupWindow浮动下拉框
		ActivityMain.main_action_bar.getCmd_textview_title_name()
			.setOnClickListener(topBarOnClickListener);
		ActivityMain.main_action_bar.getCmd_imageview_title_icon()
			.setOnClickListener(topBarOnClickListener);
		ActivityMain.main_action_bar.getCmd_imageview_title_action()
			.setOnClickListener(topBarOnClickListener);
		// 初始化PopupWindow
		f_init_popup_window();
	}
	
	/**
	 * 添加成长记录按钮监听
	 */
	private OnClickListener addGrowthBtnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// 弹出“相机”、“写一写”、“上传照片”、“身高体重”
			if (my_baby_list != null && my_baby_list.size() > 0 
					&& current_baby_id!=null && current_baby_id.length()>0) {
				Intent intent = new Intent(getActivity(), ActivityAddGrowthSelector.class);
				intent.putExtra("baby_id", current_baby_id);
				startActivity(intent);
			} else {
				Toast.makeText(getActivity(), "你还没有添加宝宝。", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	/**
	 * TopBar点击监听器,控制popWIndow的隐藏和显示
	 * 
	 */
	private View.OnClickListener topBarOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//showToast(MainActivity.screen_width+"*"+MainActivity.screen_height);
			if (!album_pop_window.isShowing()) { // 显示PopupWindow窗口
				f_popup_windw_showing();
			}else{
				album_pop_window.dismiss();
			}
			
		}
	};
	
	/**
	 * listview成长记录点击事件
	 */
	private OnItemClickListener album_growth_item_onitem_click_listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			table_album_baby_growth growth_info = (table_album_baby_growth)album_adapter.getItem(position-1);
			table_album_my_baby baby = pop_window_adapter.getItemById(growth_info.getBaby_id());
			if(baby!=null){
				growth_info.setPermissions(baby.getPermissons());
			}
			Intent intent = new Intent(getActivity(), ActivityViewGrowthInfo.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("growth_info", growth_info);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
	};
	
	
	/**
	 * PopWindow 点击事件
	 */
	private OnItemClickListener pop_window_listview_onttem_click_listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			// 查询指定宝宝的相册
			if(my_baby_list!=null && my_baby_list.size()>0){
				String baby_id = my_baby_list.get(position).getBaby_id();
				if(baby_id==null || baby_id.length()==0
						|| baby_id.equals(current_baby_id)){
					// 不查询
				}else{
					current_baby_id = baby_id;
					// 更改当前宝宝名字以及头像
					f_change_top_bar(my_baby_list.get(position).getBaby_name(), 
							my_baby_list.get(position).getBaby_pic());
					album_adapter.removeAll();// 清除掉原宝宝的成长记录
					page_number = 1;
					f_action_local_get_baby_growth_list();//查询本地成长记录
//					f_action_get_baby_growth_by_page(current_baby_id , page_number, 1);
				}
			}else{
				Toast.makeText(getActivity(), "您还没有添加宝宝", Toast.LENGTH_LONG).show();
			}
			album_pop_window.dismiss();
		}
	};
	
	  /*
     * 创建PopupWindow
     */ 
    private void f_init_popup_window() { 
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity()); 
        popupWindowPage = layoutInflater.inflate(R.layout.fragment_album_pop_window, null, false); 
        album_pop_window_layout = (LinearLayout)popupWindowPage.findViewById(R.id.album_pop_window_layout);
        listview_pop_window = (ListView) popupWindowPage.findViewById(R.id.fragment_album_pop_window_listview);
		// 设置自定义Adapter
		
		pop_window_adapter = new adapter_fragment_album_popwindow(popwindow_baby_list , getActivity());
//		optionsAdapter.addItems(datas);
		listview_pop_window.setAdapter(pop_window_adapter);
		listview_pop_window.setOnItemClickListener(pop_window_listview_onttem_click_listener);
        
		// 创建一个PopupWindow 
        // 参数1：contentView 指定PopupWindow的内容 
        // 参数2：width 指定PopupWindow的width 
        // 参数3：height 指定PopupWindow的height 
//        album_pop_window = new PopupWindow(popupWindowPage, pop_window_width, 380); 
        album_pop_window = new PopupWindow(getActivity());
        album_pop_window.setContentView(popupWindowPage);
        
        album_pop_window.setOutsideTouchable(true);  
        album_pop_window.setAnimationStyle(android.R.style.Animation_Dialog);  
        album_pop_window.setTouchable(true);  
        album_pop_window.setFocusable(true); 
        album_pop_window.setBackgroundDrawable(new BitmapDrawable());
        album_pop_window.update();  
 
        popupWindowPage.setFocusableInTouchMode(true);  
        popupWindowPage.setOnKeyListener(new OnKeyListener() {  
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				 // TODO Auto-generated method stub  
                if ((keyCode == KeyEvent.KEYCODE_MENU)&&(album_pop_window.isShowing())) {  
                	album_pop_window.dismiss();// 这里写明模拟menu的PopupWindow退出就行  
                    return true;  
                }  
                return false;
			}  
        }); 
        
        // 获取屏幕和PopupWindow的width和height 
        int screen_width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        pop_window_margin_left = (screen_width  / 2 - pop_window_width / 2);
    } 
	
	/** * 显示PopupWindow窗口 * * @param popupwindow */
	public void f_popup_windw_showing() {
		if(pop_window_adapter!=null && pop_window_adapter.getCount() == 0){
			pop_window_adapter.removeAll();
			f_init_pop_window_data();
		}
		album_pop_window.showAsDropDown(ActivityMain.main_action_bar.getCmd_imagebutton_menu(), 
				pop_window_margin_left,
				utils_density_transform.dip2px(getActivity(), 10));// X、Y方向各偏移 
	}
	
	/**
	 * 初始化PopWindow的列表数据
	 */
	private void f_init_pop_window_data(){
		if(popwindow_baby_list!=null){
			for(int i=0; i<popwindow_baby_list.size(); i++){
				pop_window_adapter.addItem(popwindow_baby_list.get(i));
			}
		}
	}
	
	/**
	 * 获取生成初始数据
	 * @return
	 */
//	private Vector<table_album_baby_growth> getInitData(){
//		Vector<table_album_baby_growth> list = new Vector<table_album_baby_growth>();
//		table_album_baby_growth item = new table_album_baby_growth();
//		item.setBaby_id("1");
//		item.setRecord_id("1");
//		item.setRecord_time("2014-05-12 12:39:42");
//		item.setAge_true("1岁2个月");
//		item.setHeight("80");
//		item.setWeight("20");
//		item.setLocations("湖南省长沙市雨花区韶山北路");
//		item.setWord_record("又下雨了");
//		
//		Vector<table_album_gridview_photo_path> pic_list = new Vector<table_album_gridview_photo_path>();
//		
//		table_album_gridview_photo_path photo_path = new table_album_gridview_photo_path();
//		photo_path.setResource_id(R.drawable.baby_pic_5);
//		pic_list.add(photo_path);
//		
//		photo_path = new table_album_gridview_photo_path();
//		photo_path.setResource_id(R.drawable.baby_pic_4);
//		pic_list.add(photo_path);
//		
//		photo_path = new table_album_gridview_photo_path();
//		photo_path.setResource_id(R.drawable.baby_pic_3);
//		pic_list.add(photo_path);
//		
//		photo_path = new table_album_gridview_photo_path();
//		photo_path.setResource_id(R.drawable.baby_pic_2);
//		pic_list.add(photo_path);
//		
//		photo_path = new table_album_gridview_photo_path();
//		photo_path.setResource_id(R.drawable.baby_pic_5);
//		pic_list.add(photo_path);
//		
//		item.setPic_list(pic_list);
//		list.add(item);
//		
//		item = new table_album_baby_growth();
//		item.setBaby_id("2");
//		item.setRecord_id("2");
//		item.setRecord_time("2014-06-02 11:19:24");
//		item.setAge_true("8个月");
//		item.setHeight("90");
//		item.setWeight("30");
//		item.setLocations("湖南省长沙市雨花区新建西路");
//		item.setWord_record("呵呵呵呵呵呵");
//		
//		pic_list = new Vector<table_album_gridview_photo_path>();
//		
//		photo_path = new table_album_gridview_photo_path();
//		photo_path.setResource_id(R.drawable.baby_pic_3);
//		pic_list.add(photo_path);
//		
//		photo_path = new table_album_gridview_photo_path();
//		photo_path.setResource_id(R.drawable.baby_pic_4);
//		pic_list.add(photo_path);
//		
//		photo_path = new table_album_gridview_photo_path();
//		photo_path.setResource_id(R.drawable.baby_pic_5);
//		pic_list.add(photo_path);
//		
//		item.setPic_list(pic_list);
//		list.add(item);
//		
//		item = new table_album_baby_growth();
//		item.setBaby_id("3");
//		item.setRecord_id("3");
//		item.setRecord_time("2014-07-23 08:12:33");
//		item.setAge_true("3岁5个月18天");
//		item.setHeight("120");
//		item.setWeight("40");
//		item.setLocations("湖南省长沙市岳麓区含浦镇");
//		item.setWord_record("等车ing");
//		
//		pic_list = new Vector<table_album_gridview_photo_path>();
//		
//		photo_path = new table_album_gridview_photo_path();
//		photo_path.setResource_id(R.drawable.baby_pic_1);
//		pic_list.add(photo_path);
//		
//		photo_path = new table_album_gridview_photo_path();
//		photo_path.setResource_id(R.drawable.baby_pic_2);
//		pic_list.add(photo_path);
//		
//		photo_path = new table_album_gridview_photo_path();
//		photo_path.setResource_id(R.drawable.baby_pic_3);
//		pic_list.add(photo_path);
//		
//		photo_path = new table_album_gridview_photo_path();
//		photo_path.setResource_id(R.drawable.baby_pic_4);
//		pic_list.add(photo_path);
//		
//		photo_path = new table_album_gridview_photo_path();
//		photo_path.setResource_id(R.drawable.baby_pic_5);
//		pic_list.add(photo_path);
//		
//		item.setPic_list(pic_list);
//		list.add(item);
//		
//		return list;
//	}

}
