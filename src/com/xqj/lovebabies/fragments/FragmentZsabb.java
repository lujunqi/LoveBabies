package com.xqj.lovebabies.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.view.DropDownListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityBabyStudy;
import com.xqj.lovebabies.activitys.ActivityHealthDetail;
import com.xqj.lovebabies.activitys.ActivityHealthSearch;
import com.xqj.lovebabies.activitys.ActivityMain;
import com.xqj.lovebabies.adapters.adapter_fragment_health_listview;
import com.xqj.lovebabies.adapters.adapter_fragment_zsabb_album_gridview;
import com.xqj.lovebabies.adapters.adapter_fragment_zsabb_health_listview;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.dbs_health_information;
import com.xqj.lovebabies.databases.dbs_health_information_top;
import com.xqj.lovebabies.databases.table_album_gridview_photo_path;
import com.xqj.lovebabies.databases.table_health_information;
import com.xqj.lovebabies.databases.table_health_information_top_pic_info;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_img_req;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_img_resp;
import com.xqj.lovebabies.structures.interface_app_get_health_information_req;
import com.xqj.lovebabies.structures.interface_app_get_health_information_resp;
import com.xqj.lovebabies.threads.thread_album_get_baby_growth_img;
import com.xqj.lovebabies.threads.thread_health_get_information_by_page_list;
import com.xqj.lovebabies.widgets.HealthListView;
import com.xqj.lovebabies.widgets.HealthListView.OnRefreshListener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentZsabb extends Fragment {
	private Integer[] growth_default_img_array = new Integer[]{
			R.drawable.zsabb_album_pic_1,R.drawable.zsabb_album_pic_2,
			R.drawable.zsabb_album_pic_3,R.drawable.zsabb_album_pic_4,
			R.drawable.zsabb_album_pic_5,R.drawable.zsabb_album_pic_6
	};
	// UI 组件
	private View main_view;
	private ListView info_list_view;
	private adapter_fragment_zsabb_health_listview health_listview_adapter = null;
	
	// 健康资讯
	private ViewPager top_view_pager = null;
	private HealthTopPaperAdapter view_page_adapter = null;
	private LinearLayout dot_num_layout = null;
	private Button dot_selected_button;
	private TextView top_page_title_textview = null;
	private List<View> top_pic_view_list = new ArrayList<View>();
	private List<table_health_information_top_pic_info> top_list;
	private int origin_page_index = 0;
	private dbs_health_information_top db_health_information_top;
	private dbs_health_information db_health_information;
	
	// 日期与天气
	private TextView date_textview;
	// 宝宝相册
	private GridView album_gridview;
	private adapter_fragment_zsabb_album_gridview adapter_album_gridview;
	//点读学习
	private RelativeLayout study_layout;
	
	private String user_id;
	
	private int new_data = 1;// 1 查询最新数据    0 查询历史记录
	private int page_number = 1;
	
	private LayoutInflater inflater;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.inflater = inflater;
		try{
			main_view = inflater.inflate(R.layout.fragment_zsabb, container, false);
			
			top_view_pager = (ViewPager)main_view.findViewById(R.id.health_top_pics_viewpager);
			dot_num_layout = (LinearLayout)main_view.findViewById(R.id.dot_num_layout);
			top_page_title_textview = (TextView)main_view.findViewById(R.id.top_page_title);
			
			init_action_bar();// 初始化头部actionBar、
			init_main_page();// 初始化主界面
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		user_id = PreferencesUtils.getString(getActivity(), "user_id");

		
		
		// 健康资讯
//		f_get_health_information_by_page("info", String.valueOf(page_number));// 获取普通资讯
//		f_get_health_information_by_page("top", String.valueOf(page_number));// 获取头条资讯
		
		f_get_local_health_information("info");// 获取本地普通资讯
		System.out.println("f_get_local_health_information(info);");
		if(!utils_common_tools.get_network_status(getActivity())){//如果无网络
			f_get_local_health_information("top");// 获取本地头条
			System.out.println("f_get_local_health_information(top);");
		}else{
			System.out.println("f_get_health_information_by_page(top);");
			f_get_health_information_by_page("top", String.valueOf(page_number));// 获取头条资讯
		}
		
		
		// 宝宝成长记录
		if(utils_common_tools.get_network_status(getActivity())){//有网络
			f_get_baby_growth_img();//网络查询宝宝成长记录图片
		}else{
			f_init_baby_growth_img();// 显示默认图片
		}
		
		return main_view;
	}
	
	/**
	 * 初始化头部actionBar、
	 */
	private void init_action_bar() {
		try {
			
			ActivityMain.main_action_bar.getCmd_textview_title_name().setText("掌上爱宝贝");
			
			ActivityMain.main_action_bar.getCmd_imagebutton_more().setVisibility(View.INVISIBLE);
			
			ActivityMain.main_action_bar.getCmd_imageview_title_icon().setVisibility(View.INVISIBLE);
			
			ActivityMain.main_action_bar.getCmd_imageview_title_action().setVisibility(View.INVISIBLE);
		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 初始化主页面
	 */
	private void init_main_page() {
		info_list_view = (ListView)main_view.findViewById(R.id.fragment_zsabb_health_listview);
		health_listview_adapter = new adapter_fragment_zsabb_health_listview(getActivity());
		info_list_view.setAdapter(health_listview_adapter);
		info_list_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				table_health_information info = (table_health_information)health_listview_adapter.getItem(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityHealthDetail.class);
				intent.putExtra("content_id", info.getContent_id());
				getActivity().startActivity(intent);
			}
		});
		// 日期显示
		date_textview = (TextView)main_view.findViewById(R.id.fragment_zsabb_date_textview);
		long timestamp = System.currentTimeMillis();
		String time = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(new Date(timestamp));
		date_textview.setText(time);
		
		// 宝宝相册
		album_gridview = (GridView)main_view.findViewById(R.id.fragment_zsabb_album_gridview);
		adapter_album_gridview = new adapter_fragment_zsabb_album_gridview(getActivity());
		album_gridview.setAdapter(adapter_album_gridview);
		album_gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// 跳转到宝宝相册
				ActivityMain.switch_fragment(new FragmentAlbum(),false);
			}
		});
		
		//点读学习
		study_layout = (RelativeLayout)main_view.findViewById(R.id.fragment_zsabb_study_layout);
		study_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityBabyStudy.class);
				startActivity(intent);
			}
		});
	}
	/**
	 * 初始化列表头部滑动图片
	 */
	private void init_top_list(){
		view_page_adapter = new HealthTopPaperAdapter();
		top_view_pager.setAdapter(view_page_adapter);
		
		top_view_pager.setCurrentItem(origin_page_index);
		 table_health_information_top_pic_info orig_info = top_list.get(origin_page_index);
		top_page_title_textview.setText(orig_info.getTitle());
		
		top_view_pager.setOnPageChangeListener(new OnPageChangeListener() {  
            @Override  
            public void onPageSelected(int position) { 
            	System.out.println("onPageSelected-------"+position);
            	origin_page_index = position;
            	int b_index = position%top_pic_view_list.size();
                if(dot_selected_button != null){  
                	dot_selected_button.setBackgroundResource(R.drawable.icon_dot_normal);  
                }
                Button currentBt = (Button)dot_num_layout.getChildAt(b_index);
                currentBt.setBackgroundResource(R.drawable.icon_dot_selected);  
                dot_selected_button = currentBt;  
                table_health_information_top_pic_info info = top_list.get(b_index);
                top_page_title_textview.setText(info.getTitle());
            }
            @Override  
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }  
            @Override  
            public void onPageScrollStateChanged(int arg0) {
            }
        }); 
	}
	class HealthTopPaperAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
		}
		@Override
		public Object instantiateItem(View container, final int position) {
            try{
            	((ViewPager) container).addView(top_pic_view_list.get(position%top_pic_view_list.size()),0);
                top_pic_view_list.get(position).setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					table_health_information_top_pic_info info = top_list.get(position%top_pic_view_list.size());
    					Intent intent = new Intent();
    					intent.setClass(getActivity(), ActivityHealthDetail.class);
    					intent.putExtra("content_id", info.getTop_id());
    					getActivity().startActivity(intent);
    				}
    			});
            }catch(Exception e){
            	//
            }
            return top_pic_view_list.get(position%top_pic_view_list.size());  
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	/**
	 * 网络交互部分
	 */
	private Handler network_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.fragment_health_get_information_list_success){
				interface_app_get_health_information_resp resp = (interface_app_get_health_information_resp)msg.obj;
				if(resp!=null && resp.getAction()!=null){
					if(resp.getAction().equals("top")){//头条
						top_list = resp.getTop_list();
						if(top_list!=null && top_list.size()>0){
							for(int i=0;i<top_list.size();i++){
								table_health_information_top_pic_info info = top_list.get(i);
								LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_health_top_pic_layout, null);
								ImageView imageview = (ImageView)layout.findViewById(R.id.health_top_pic_model_imageview);
								f_display_Image(imageview, info.getPic_name());
								top_pic_view_list.add(layout);
							}
							init_top_list();//初始化头条资讯TopView
						}else{
							top_view_pager.setVisibility(View.GONE);
							dot_num_layout.setVisibility(View.GONE);
							top_page_title_textview.setVisibility(View.GONE);
						}
						for (int i = 0; i < top_pic_view_list.size(); i++) { 
				            Button bt = new Button(getActivity());  
				            android.widget.RadioGroup.LayoutParams button_params = 
				            		new android.widget.RadioGroup.LayoutParams(10, 10);
				            button_params.setMargins(0, 0, 5, 0);
				            bt.setLayoutParams(button_params);  
				            bt.setBackgroundResource(R.drawable.icon_dot_normal);
							if(i == origin_page_index){
								bt.setBackgroundResource(R.drawable.icon_dot_selected);
								dot_selected_button = bt;
							}
							dot_num_layout.addView(bt);  
				        }
					}else if(resp.getAction().equals("info")){//普通资讯
						List<table_health_information> list = resp.getList();
						String this_page = resp.getPage_number();
						if(this_page==null || this_page.length()==0 
								|| this_page.equals("0")
								|| this_page.equals("1")){// 第一页查询
							health_listview_adapter.removeAll();
						}else{//查看更多
							if(list==null || list.size()==0){
								Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
							}
						}
						if(list!=null && list.size()>0){
							for(int i=0;i<list.size();i++){
								if(health_listview_adapter.getCount()<3){// 只添加三条
									health_listview_adapter.addItemTail(list.get(i));
								}
							}
						}
					}
				}
			}else if(msg.what == message_what_values.activity_album_baby_growth_img_get_data_success){
				interface_app_get_baby_growth_img_resp resp = (interface_app_get_baby_growth_img_resp)msg.obj;
				if(resp!=null){
					List<String> img_list = resp.getList();
					int count = 0;
					if(img_list!=null && img_list.size()>0){
						adapter_album_gridview.removeAll();
						for(int i=0;i<img_list.size();i++){
							String img_path = img_list.get(i);
							if(img_path!=null && img_path.length()>0){
								String[] img_array = img_path.split(",");
								for(String img : img_array){
									if(count>=5){
										// 只添加头5张图片
									}else{
										table_album_gridview_photo_path photo_path = new table_album_gridview_photo_path();
										photo_path.setImage_path(img);
										adapter_album_gridview.addItem(photo_path);
										count++;
									}
								}
							}
						}
						for(;count<6;count++){
							table_album_gridview_photo_path photo_path = new table_album_gridview_photo_path();
							photo_path.setResource_id(growth_default_img_array[count]);
							adapter_album_gridview.addItem(photo_path);
						}
					}else{
						f_init_baby_growth_img();
					}
				}else{
					Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == message_what_values.activity_album_get_data_timeout){
				Toast.makeText(getActivity(), "查询失败,网络错误", Toast.LENGTH_SHORT).show();
			}else if(msg.what == message_what_values.fragment_health_get_data_failed){
				Toast.makeText(getActivity(), "查询失败,网络错误", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	
	/**
	 * 显示图片
	 * @param imageView
	 * @param pic_path
	 */
	private void f_display_Image(ImageView imageView, String pic_path){
		String imgurl = network_interface_paths.get_project_root+pic_path;
//		System.out.println("GrowthPhoto-->"+imgurl);
//		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//		builder.showImageOnLoading(R.drawable.default_image_position);
//		builder.showImageForEmptyUri(R.drawable.default_image_position);
//		builder.cacheInMemory(false);
//		builder.cacheOnDisk(true);
//		builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//		DisplayImageOptions options = builder.build();
//		ImageLoader.getInstance().displayImage(imgurl, imageView, options);
		utils_common_tools.f_display_Image(getActivity(), 
				imageView, imgurl,R.drawable.default_image_position,
				R.drawable.default_image_position, ImageScaleType.IN_SAMPLE_INT);
	}
	
	/**
	 * 分页查询健康育儿资讯
	 * @param action
	 * @param showPage
	 */
	private void f_get_health_information_by_page(String action, String showPage){
		interface_app_get_health_information_req req = new interface_app_get_health_information_req();
		req.setAction(action);
		req.setShow_page(showPage);
		new thread_health_get_information_by_page_list(getActivity(), network_handler, req, new_data).start();
	}
	
	/**
	 * 查询本地健康资讯
	 */
	private boolean f_get_local_health_information(String action){
		if(action!=null && action.equals("top")){
			// 本地头条健康资讯,只在无网络状态下查询本地头条，
			//头条只查询一次（只配置一次ViewPager）
			db_health_information_top = new dbs_health_information_top(getActivity());
			top_list = db_health_information_top.do_select_data("select * from (select * from t_health_information_top order by top_id desc) limit 0,5");
			if(top_list!=null && top_list.size()>0){
				for(int i=0;i<top_list.size();i++){
					table_health_information_top_pic_info info = top_list.get(i);
					LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_health_top_pic_layout, null);
					ImageView imageview = (ImageView)layout.findViewById(R.id.health_top_pic_model_imageview);
					f_display_Image(imageview, info.getPic_name());
					top_pic_view_list.add(layout);
				}
				init_top_list();//初始化头条资讯TopView
			}else{
				top_view_pager.setVisibility(View.GONE);
				dot_num_layout.setVisibility(View.GONE);
				top_page_title_textview.setVisibility(View.GONE);
			}
			for (int i = 0; i < top_pic_view_list.size(); i++) { 
	            Button bt = new Button(getActivity());  
	            android.widget.RadioGroup.LayoutParams button_params = 
	            		new android.widget.RadioGroup.LayoutParams(10, 10);
	            button_params.setMargins(0, 0, 5, 0);
	            bt.setLayoutParams(button_params);  
	            bt.setBackgroundResource(R.drawable.icon_dot_normal);
				if(i == origin_page_index){
					bt.setBackgroundResource(R.drawable.icon_dot_selected);
					dot_selected_button = bt;
				}
				dot_num_layout.addView(bt);  
	        }
		}else{
			db_health_information = new dbs_health_information(getActivity());
			List<table_health_information> health_list = db_health_information.do_select_data("select * from(select * from t_health_information order by content_id desc) limit 0,3");
			if(health_list!=null && health_list.size()>0){
				health_listview_adapter.removeAll();
				for(int i=0;i<health_list.size();i++){
					health_listview_adapter.addItemTail(health_list.get(i));
				}
			}else{// 网络获取
				f_get_health_information_by_page("info", String.valueOf(page_number));
			}
		}
		return false;
	}
	
	/**
	 * 查询宝宝成长记录图片
	 */
	private boolean f_get_baby_growth_img(){
		interface_app_get_baby_growth_img_req req = new interface_app_get_baby_growth_img_req();
		req.setUser_id(user_id);
		new thread_album_get_baby_growth_img(getActivity(), network_handler, req).start();
		return true;
	}
	
	/**
	 * 初始化宝宝成长记录本地图片
	 */
	private void f_init_baby_growth_img(){
		adapter_album_gridview.removeAll();
		table_album_gridview_photo_path photo_path;
		for(int resource_id : growth_default_img_array){
			photo_path = new table_album_gridview_photo_path();
			photo_path.setResource_id(resource_id);
			adapter_album_gridview.addItem(photo_path);
		}
	}
}
