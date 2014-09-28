package com.xqj.lovebabies.activitys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import cn.trinea.android.common.util.PreferencesUtils;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_album_growth_comment_listview;
import com.xqj.lovebabies.adapters.adapter_fragment_album_gridview;
import com.xqj.lovebabies.adapters.adapter_fragment_album_listview;
import com.xqj.lovebabies.adapters.adapter_fragment_album_popwindow;
import com.xqj.lovebabies.commons.utils_common_share;
import com.xqj.lovebabies.commons.utils_density_transform;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.dbs_album_baby_growth;
import com.xqj.lovebabies.databases.table_album_baby_growth;
import com.xqj.lovebabies.databases.table_album_gridview_photo_path;
import com.xqj.lovebabies.databases.table_album_growth_comment;
import com.xqj.lovebabies.databases.table_album_growth_praise;
import com.xqj.lovebabies.map.baidu_location;
import com.xqj.lovebabies.structures.interface_app_add_baby_growth_comment_req;
import com.xqj.lovebabies.structures.interface_app_cancel_praise_baby_growth_req;
import com.xqj.lovebabies.structures.interface_app_delete_baby_growth_comment_req;
import com.xqj.lovebabies.structures.interface_app_delete_baby_growth_comment_resp;
import com.xqj.lovebabies.structures.interface_app_delete_baby_growth_req;
import com.xqj.lovebabies.structures.interface_app_delete_baby_growth_resp;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_comment_req;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_comment_resp;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_praise_req;
import com.xqj.lovebabies.structures.interface_app_get_baby_growth_praise_resp;
import com.xqj.lovebabies.structures.interface_app_praise_baby_growth_req;
import com.xqj.lovebabies.threads.thread_album_add_baby_growth_comment;
import com.xqj.lovebabies.threads.thread_album_cancel_praise_baby_growth;
import com.xqj.lovebabies.threads.thread_album_delete_baby_growth;
import com.xqj.lovebabies.threads.thread_album_delete_baby_growth_comment;
import com.xqj.lovebabies.threads.thread_album_get_growth_comment_list;
import com.xqj.lovebabies.threads.thread_album_get_growth_praise_list;
import com.xqj.lovebabies.threads.thread_album_praise_baby_growth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityViewGrowthInfo extends Activity {
	public static final int SHOW_ZAN = 1001;
//	public static final int RESET_COMMENT_LIST_HEIGH = 1002;// 重置评论列表高度
	public static final int DELETE_GROWTH_INFO = 1003;// 删除成长记录
	public static final int DELETE_GROWTH_COMMENT = 1004;// 删除成长记录
	private final int STOP_LOCATION_CLIENT = 9001;// 关闭地图搜索
	// PopupWindow对象
	private PopupWindow growth_info_pop_window = null;
	
	/**
	 * 百度地图应用
	 */
	private baidu_location location_application = null;
	private LocationClient location_client = null;
	private boolean bl_is_first_loc = true;
	private TextView location_info_textview = null;
	
	/*********************************************************************/
	private View contentView = null;
	private ListView comment_listview = null;
	private adapter_album_growth_comment_listview comment_listview_adapter = null;

	private GridView growth_photos_gridview = null;
	private adapter_fragment_album_gridview album_gridview_adapter = null;
	
//	private ScrollView view_growth_scrollview = null;
	
	private ImageView growth_info_left_imageview = null;
	private ImageView growth_info_right_imageview = null;
	
	private RelativeLayout view_growth_top_bar_layout = null;
	private TextView fragment_album_textview_title = null; // growth info 文字内容
	private TextView fragment_album_textview_age = null;	// 宝宝年纪
	private TextView fragment_album_textview_date = null;	// 记录时间
	private LinearLayout fragment_album_layout_zan = null;//点赞
	private ImageView fragment_album_imageview_zan = null;// 点赞图片
	private TextView fragment_album_textview_zan = null;// 点赞文字
	private LinearLayout fragment_album_layout_comment = null;//评论
	
	private int screen_width = 0;// 屏幕宽度
	
	
	/**
	 * 与服务端交互
	 */
	private boolean bl_zan = false;
	// 删除成长记录
	private interface_app_delete_baby_growth_req delete_growth_request = null;
	// 添加成长记录评论
	private interface_app_add_baby_growth_comment_req add_growth_comment_request = null;
	// 查询评论列表
	private interface_app_get_baby_growth_comment_req get_growth_comment_list_request = null;
	// 查询点赞列表
	private interface_app_get_baby_growth_praise_req get_growth_praise_list_request = null;
	// 删除评论
	private interface_app_delete_baby_growth_comment_req delete_growth_comment_request = null;
	// 取消点赞
	private interface_app_cancel_praise_baby_growth_req cancel_praise_growth_request = null;
	// 点赞成长记录
	private interface_app_praise_baby_growth_req praise_growth_request = null;
	// 评论列表
	private List<table_album_growth_comment> comment_list = null;
	// 点赞列表
	private List<table_album_growth_praise> praise_list = null;
	// 是否已点赞
	private String if_praised = null;// 初始值，表示未成功查询点赞列表
	
	/**
	 * 点赞列表
	 */
	private TextView album_growth_comment_zan_textveiw = null;
	private ImageView growth_comment_zan_imageview = null;
	/**
	 * 评论成长记录
	 */
	private android.app.AlertDialog add_comment_dialog = null;
	private EditText comment_content_edittext = null;
	private Button save_button = null;
	private Button reset_button = null;
	private boolean bl_comment_able = true;
	
	/**
	 * ************************* growth info ***************************
	 */
	private table_album_baby_growth growth_info = null;
	private String record_id = null;	// 成长记录ID
	
	private boolean bl_delete_growth_able = true;// 点击删除成长记录是否响应
	private LayoutInflater inflater = null;
	
	private String permissons = "0";//用户对宝宝的操作权限
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_album_growth_info);
		inflater = LayoutInflater.from(this);
		
		// 百度地图
		location_info_textview = new TextView(this);
		location_application = new baidu_location(getApplication());
		
		
		Bundle mBundle = getIntent().getExtras();
		growth_info = (table_album_baby_growth)mBundle.get("growth_info");
		if(growth_info!=null){
			record_id = growth_info.getRecord_id();
			permissons = growth_info.getPermissions();
		}
		System.out.println("permissons-->"+permissons);
		
		initTopBar();
		initContentUI();
				
		comment_listview = (ListView)findViewById(R.id.album_growth_comments_listview);
		comment_listview_adapter = new adapter_album_growth_comment_listview(this, widge_show_change_handler, permissons);
		comment_listview.addHeaderView(contentView);
		comment_listview.setAdapter(comment_listview_adapter);
		
		init_comment_listview();
//		setListViewHeightBasedOnChildren(comment_listview);
		
		f_init_popup_window();// 初始化 popWindow
		
		getAddress();// 初始化百度地图定位
		location_client.start();// 定位地图
	}
	
	
	
	/****
	 * ********************************   初始化界面   **********************************
	 */
	/**
	 * 初始化头部UI
	 */
	private void initTopBar(){
		growth_info_left_imageview = (ImageView)findViewById(R.id.growth_info_left_imageview);
		growth_info_right_imageview = (ImageView)findViewById(R.id.growth_info_right_imageview);
		growth_info_left_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityViewGrowthInfo.this.finish();
			}
		});
		growth_info_right_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (growth_info_pop_window.isShowing()) { 
					System.out.println("growth_info_pop_window.dismiss()");
                	growth_info_pop_window.dismiss();
                }else{
                	System.out.println("f_popup_windw_showing");
                	f_popup_windw_showing();
                }
			}
		});
		
		view_growth_top_bar_layout = (RelativeLayout) findViewById(R.id.view_growth_top_bar_layout);

	}
	
	/**
	 * 初始化内容xml
	 */
	private void initContentUI(){
		contentView =  inflater.inflate(R.layout.fragment_album_growth_info_content, null);
		fragment_album_textview_date = (TextView) contentView.findViewById(R.id.fragment_album_textview_date);
		fragment_album_textview_age = (TextView) contentView.findViewById(R.id.fragment_album_textview_age);
		fragment_album_textview_title = (TextView) contentView.findViewById(R.id.fragment_album_textview_title);
		if(growth_info!=null){
			fragment_album_textview_date.setText(growth_info.getRecord_time());
			fragment_album_textview_age.setText(growth_info.getAge_true());
			fragment_album_textview_title.setText(growth_info.getWord_record());
		}
		
		fragment_album_layout_zan = (LinearLayout)contentView.findViewById(R.id.fragment_album_layout_zan);
		fragment_album_imageview_zan = (ImageView)contentView.findViewById(R.id.fragment_album_imageview_zan);
		fragment_album_textview_zan = (TextView)contentView.findViewById(R.id.fragment_album_textview_zan);
		fragment_album_layout_comment = (LinearLayout)contentView.findViewById(R.id.fragment_album_layout_comment);
		fragment_album_layout_zan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = SHOW_ZAN;
				widge_show_change_handler.sendMessage(msg);
			}
		});
		fragment_album_layout_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 弹出输入框，提交评论
				showAddCommentDialog();
			}
		});
		growth_photos_gridview = (GridView)contentView.findViewById(R.id.fragment_album_gridview_photos);
		Vector<table_album_gridview_photo_path> pic_list = growth_info.getPic_list();
		if(pic_list != null){
			int pic_num = pic_list.size();
			ViewGroup.LayoutParams params = growth_photos_gridview.getLayoutParams();
			int gridHeight = utils_density_transform.dip2px(this, adapter_fragment_album_listview.getGridViewHeight(pic_num));
			Log.i("gridViewHeight", "pic_num="+pic_num+"-----------------gridHeight="+gridHeight);
			params.height = gridHeight;
			growth_photos_gridview.setLayoutParams(params);
			album_gridview_adapter = new adapter_fragment_album_gridview(pic_list, this);
			growth_photos_gridview.setAdapter(album_gridview_adapter);
			growth_photos_gridview.setOnItemClickListener(gridviewItemClickListener);
		}else{
			growth_photos_gridview.setVisibility(View.INVISIBLE);
		}
		
		album_growth_comment_zan_textveiw = (TextView)contentView.findViewById(R.id.album_growth_comment_zan_textveiw);
		growth_comment_zan_imageview = (ImageView)contentView.findViewById(R.id.growth_comment_zan_imageview);
		
		String zan_user_str = "";
		if(praise_list!=null && praise_list.size()>0){
			for(int i=0; i<praise_list.size()-1; i++){
				zan_user_str += praise_list.get(i).getUser_nick_name()+",";
			}
			zan_user_str += praise_list.get(praise_list.size()-1).getUser_nick_name();
		}
		album_growth_comment_zan_textveiw.setText(zan_user_str);
	}
	
	/**
	 * 处理界面的交互的 handler
	 */
	private Handler widge_show_change_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String location_info = "";
			if(location_info_textview!=null){
				location_info = location_info_textview.getText().toString();
				location_info = (location_info==null || location_info.equals("null")) ? "" : location_info;
			}
					
			if(msg.what == SHOW_ZAN){
				if(bl_zan){// 取消点赞
					fragment_album_imageview_zan.setImageResource(R.drawable.activity_interaction_notice_detail_praise_0);
					fragment_album_textview_zan.setText("点赞");
					bl_zan = false;
					f_cancel_praise();
				}else{// 点赞
					fragment_album_imageview_zan.setImageResource(R.drawable.activity_interaction_notice_detail_praise_1);
					fragment_album_textview_zan.setText("取消");
					bl_zan = true;
					f_add_praise(location_info, "");
				}
			}else if(msg.what == DELETE_GROWTH_COMMENT){// 请求删除评论
				table_album_growth_comment comment = (table_album_growth_comment)comment_listview_adapter.getItem(msg.arg1);
				f_delete_comment(comment.getComm_id());
			}
			// 删除成长记录评论，返回结果
			else if(msg.what == message_what_values.activity_delete_baby_growth_comment_success){
				interface_app_delete_baby_growth_comment_resp resp = (interface_app_delete_baby_growth_comment_resp)msg.obj;
				if(resp.getResultCode().equals(resp.SUCCESS)){
					Toast.makeText(ActivityViewGrowthInfo.this, "删除成功", Toast.LENGTH_SHORT).show();
					f_get_comment_list();
				}else if(resp.getResultCode().equals(resp.FAILED_NO_PERMISSION)){
					Toast.makeText(ActivityViewGrowthInfo.this, "删除失败，无权限", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(ActivityViewGrowthInfo.this, "删除失败", Toast.LENGTH_SHORT).show();
				}
			}
			// 删除成长记录
			else if(msg.what == message_what_values.activity_delete_baby_growth_success){// 删除成长记录
				interface_app_delete_baby_growth_resp resp = (interface_app_delete_baby_growth_resp)msg.obj;
				if(resp.getResultCode().equals(resp.SUCCESS)){
					Toast.makeText(ActivityViewGrowthInfo.this, "删除成功", Toast.LENGTH_SHORT).show();
					dbs_album_baby_growth dbs = new dbs_album_baby_growth(ActivityViewGrowthInfo.this);
					deleteGrowthData(resp.getGrowth_id(), dbs);//删除数据库缓存
					ActivityViewGrowthInfo.this.finish();
				}else{
					Toast.makeText(ActivityViewGrowthInfo.this, "删除失败", Toast.LENGTH_SHORT).show();
					bl_delete_growth_able = true;
				}
			}else if(msg.what == message_what_values.activity_delete_baby_growth_failed){
				Toast.makeText(ActivityViewGrowthInfo.this, "删除失败", Toast.LENGTH_SHORT).show();
				bl_delete_growth_able = true;
			}
			// 添加成长记录评论
			else if(msg.what == message_what_values.activity_add_baby_growth_comment_success){
				Toast.makeText(ActivityViewGrowthInfo.this, "评论成功", Toast.LENGTH_SHORT).show();;
				add_comment_dialog.dismiss();
				f_get_comment_list();
			}
			// 查询成长记录评论
			else if(msg.what == message_what_values.activity_get_baby_growth_comment_list_success){
				interface_app_get_baby_growth_comment_resp resp = (interface_app_get_baby_growth_comment_resp)msg.obj;
				comment_list = resp.getList();
				if(comment_list!=null && comment_list.size()>0){
					comment_listview_adapter.removeAll();
					if(if_praised!=null && if_praised.equals("1")){// 已赞
						bl_zan = true;// 已赞，再点就是取消
						fragment_album_textview_zan.setText("取消");
						fragment_album_imageview_zan.setImageResource(R.drawable.activity_interaction_notice_detail_praise_1);
					}else{
						bl_zan = false;// 点击，点赞
						fragment_album_textview_zan.setText("点赞");
						fragment_album_imageview_zan.setImageResource(R.drawable.activity_interaction_notice_detail_praise_0);		
					}
					if(praise_list!=null && praise_list.size()>0){
						
					}
					// 再添加评论列表
					for(int i=comment_list.size()-1; i>=0; i--){
						table_album_growth_comment comment_item = comment_list.get(i);
						comment_listview_adapter.addItem(comment_item);
					}
					// 重置listview的高度
//					setListViewHeightBasedOnChildren(comment_listview);
				}
			}
			// 查询成长记录点赞列表
			else if(msg.what == message_what_values.activity_get_baby_growth_praise_list_success){
				interface_app_get_baby_growth_praise_resp resp = (interface_app_get_baby_growth_praise_resp)msg.obj;
				praise_list = resp.getList();
				if_praised = resp.getIs_praised();
				if(if_praised!=null && if_praised.equals("1")){// 已赞
					bl_zan = true;// 已赞，再点就是取消
					fragment_album_textview_zan.setText("取消");
					fragment_album_imageview_zan.setImageResource(R.drawable.activity_interaction_notice_detail_praise_1);
				}else{
					bl_zan = false;// 点击，点赞
					fragment_album_textview_zan.setText("点赞");
					fragment_album_imageview_zan.setImageResource(R.drawable.activity_interaction_notice_detail_praise_0);		
				}
				String zan_user_str = "";
				if(praise_list!=null && praise_list.size()>0){
					for(int i=0; i<praise_list.size()-1; i++){
						zan_user_str += praise_list.get(i).getUser_nick_name()+",";
					}
					zan_user_str += praise_list.get(praise_list.size()-1).getUser_nick_name();
				}
				album_growth_comment_zan_textveiw.setText(zan_user_str);
			}
			// 点赞成功
			else if(msg.what == message_what_values.activity_praise_baby_growth_success){
				f_get_praise_list();
			}
			// 取消点赞成功
			else if(msg.what == message_what_values.activity_cancel_praise_baby_growth_success){
				f_get_praise_list();
			}
			else if(msg.what == message_what_values.activity_album_get_data_timeout){
				Toast.makeText(ActivityViewGrowthInfo.this, "请求失败,网络错误", Toast.LENGTH_SHORT).show();
			}
			
			
			else if(msg.what == STOP_LOCATION_CLIENT){// 关闭地图搜索
				location_client.stop();
			}
		}
		
	};
	
	/**
	 * 设置 listview 高度 scrollview 和 listview共存
	 * 
	 * @param listView
	 *            要设置高度的listview对象
	 */
//	public void setListViewHeightBasedOnChildren(ListView listView) {
//		ListAdapter listAdapter = listView.getAdapter();
//		if (listAdapter == null) {
//			return;
//		}
//		int totalHeight = 0;
//		for (int i = 0; i < listAdapter.getCount(); i++) {
//			listAdapter.getItem(i);
//			View listItem = listAdapter.getView(i, null, listView);
//			listItem.measure(0, 0);
//			totalHeight += listItem.getMeasuredHeight();
//			System.out.println("totalHeight+="+listItem.getMeasuredHeight()+"-->"+totalHeight);
//		}
//
//		ViewGroup.LayoutParams params = listView.getLayoutParams();
//		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//		System.out.println("params.height = "+totalHeight
//				+"+("+listView.getDividerHeight()+"*("+(listAdapter.getCount() - 1)
//				+"))-->"+params.height);
//		// 设置边距
////		 ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
//		listView.setLayoutParams(params);
//	}
	
	/**
	 * *****************************  与服务端交互  *****************************************
	 */
	
	/**
	 * 查询评论列表
	 */
	private void f_get_comment_list(){
		get_growth_comment_list_request = new interface_app_get_baby_growth_comment_req();
		String user_id = PreferencesUtils.getString(this, "user_id");
		get_growth_comment_list_request.setUser_id(user_id);
		get_growth_comment_list_request.setRecord_id(record_id);
		System.out.println("--------------------------   查询成长记录的评论列表  -------------------------");
		System.out.println("user_id-->"+user_id);
		System.out.println("record_id-->"+record_id);
		new thread_album_get_growth_comment_list(widge_show_change_handler, get_growth_comment_list_request).start();
	}
	
	/**
	 * 查询点赞列表
	 */
	private void f_get_praise_list(){
		get_growth_praise_list_request = new interface_app_get_baby_growth_praise_req();
		String user_id = PreferencesUtils.getString(this, "user_id");
		get_growth_praise_list_request.setUser_id(user_id);
		get_growth_praise_list_request.setRecord_id(record_id);
		System.out.println("--------------------------   查询成长记录的点赞列表  -------------------------");
		System.out.println("user_id-->"+user_id);
		System.out.println("record_id-->"+record_id);
		new thread_album_get_growth_praise_list(widge_show_change_handler, get_growth_praise_list_request).start();
	}
	
	/**
	 * 删除评论
	 */
	private void f_delete_comment(String comm_id){
		delete_growth_comment_request = new interface_app_delete_baby_growth_comment_req();
		String user_id = PreferencesUtils.getString(this, "user_id");
		delete_growth_comment_request.setUser_id(user_id);
		delete_growth_comment_request.setComment_id(comm_id);
		System.out.println("--------------------------   删除成长记录的评论  -------------------------");
		System.out.println("user_id-->"+user_id);
		System.out.println("comm_id-->"+comm_id);
		new thread_album_delete_baby_growth_comment(widge_show_change_handler, delete_growth_comment_request).start();
	}
	
	/**
	 * 评论成长记录
	 */
	private void f_add_comment(String comm_content, String comm_place, 
			String parent_comm_id, String comm_machion, String remark){
		add_growth_comment_request = new interface_app_add_baby_growth_comment_req();
		String user_id = PreferencesUtils.getString(this, "user_id");
		add_growth_comment_request.setUser_id(user_id);
		add_growth_comment_request.setRecord_id(record_id);
		add_growth_comment_request.setComm_content(comm_content);
		add_growth_comment_request.setComm_place(comm_place);
		add_growth_comment_request.setParent_comm_id(parent_comm_id);
		add_growth_comment_request.setComm_machion(comm_machion);
		add_growth_comment_request.setRemark(remark);
		System.out.println("--------------------------   添加成长记录的评论  -------------------------");
		System.out.println("user_id-->"+user_id);
		System.out.println("record_id-->"+record_id);
		System.out.println("comm_content-->"+comm_content);
		System.out.println("comm_place-->"+comm_place);
		System.out.println("parent_comm_id-->"+parent_comm_id);
		System.out.println("comm_machion-->"+comm_machion);
		System.out.println("remark-->"+remark);
		new thread_album_add_baby_growth_comment(widge_show_change_handler, add_growth_comment_request).start();
	}
	
	/**
	 * 给成长记录点赞
	 */
	private void f_add_praise(String location, String remark){
		praise_growth_request = new interface_app_praise_baby_growth_req();
		String user_id = PreferencesUtils.getString(this, "user_id");
		praise_growth_request.setUser_id(user_id);
		praise_growth_request.setRecord_id(record_id);
		praise_growth_request.setOper_mation(global_contants.oper_mation);
		praise_growth_request.setLocation(location);
		praise_growth_request.setRemark(remark);
		System.out.println("--------------------------   给成长记录点赞  -------------------------");
		System.out.println("user_id-->"+user_id);
		System.out.println("record_id-->"+record_id);
		System.out.println("oper_mation-->"+global_contants.oper_mation);
		System.out.println("location-->"+location);
		System.out.println("remark-->"+remark);
		new thread_album_praise_baby_growth(widge_show_change_handler, praise_growth_request).start();
	
	}
	
	/**
	 * 取消点赞
	 */
	private void f_cancel_praise(){
		cancel_praise_growth_request = new interface_app_cancel_praise_baby_growth_req();
		String user_id = PreferencesUtils.getString(this, "user_id");
		cancel_praise_growth_request.setUser_id(user_id);
		cancel_praise_growth_request.setRecord_id(record_id);
		System.out.println("--------------------------   取消成长记录点赞  -------------------------");
		System.out.println("user_id-->"+user_id);
		System.out.println("record_id-->"+record_id);
		new thread_album_cancel_praise_baby_growth(widge_show_change_handler, cancel_praise_growth_request).start();
	}
	
	/**
	 * 删除成长记录
	 */
	private void f_delete_growth_info(){
		delete_growth_request = new interface_app_delete_baby_growth_req();
		String user_id = PreferencesUtils.getString(this, "user_id");
		delete_growth_request.setUser_id(user_id);
		delete_growth_request.setRecord_id(record_id);
		System.out.println("--------------------------   删除成长记录  -------------------------");
		System.out.println("user_id-->"+user_id);
		System.out.println("record_id-->"+record_id);
		new thread_album_delete_baby_growth(widge_show_change_handler, delete_growth_request).start();
		bl_delete_growth_able = false;
	}
	
	/**
	 * ******************************** 评论列表 ****************************************
	 */
	/**
	 * 初始化评论列表
	 */
	private void init_comment_listview(){
		f_get_comment_list();
		f_get_praise_list();
	}

	/**
	 ***************************** 弹出对话框  ****************************
	 */
	/**
	 * 弹出 添加评论 的对话框
	 */
	private void showAddCommentDialog(){
		add_comment_dialog = new android.app.AlertDialog.Builder(this).create();
		add_comment_dialog.show();
		Window window = add_comment_dialog.getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		window.setContentView(R.layout.album_add_growth_comment_dialog);
		comment_content_edittext = (EditText)window.findViewById(R.id.growth_add_comment_content_edittext);		
		save_button = (Button)window.findViewById(R.id.growth_save_comment_button);	
		reset_button = (Button)window.findViewById(R.id.growth_reset_comment_button);	
		save_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 提交评论
				String content_str = comment_content_edittext.getText().toString();
				String location_str = location_info_textview.getText().toString();
				location_str = (location_str==null || location_str.equals("null")) ? "" : location_str;
				if(content_str!=null && content_str.length()>0){
					f_add_comment(content_str, location_str, "", global_contants.oper_mation, "");
				}else{
					Toast.makeText(ActivityViewGrowthInfo.this, "说点儿什么吧...", Toast.LENGTH_LONG).show();
				}
			}
		});
		reset_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				comment_content_edittext.setText("");
			}
		});
	}
	
	/*****************************  百度地图定位   ************************************/
	// 定位地址信息
		private void getAddress() {
			bl_is_first_loc = false;

			location_client = location_application.getmLocationClient();
			location_application.setmTv(location_info_textview);
			location_application.setHandler(widge_show_change_handler);

			setLocationOption();
			location_client.requestPoi();
		}

		// 设置定位相关参数
		private void setLocationOption() {
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true); // 打开gps
			option.setServiceName("com.baidu.location.service_v2.9");
			option.setPoiExtraInfo(true);
			option.setAddrType("all");
			option.setPriority(LocationClientOption.GpsFirst); // 不设置，默认是gps优先
			option.setPoiNumber(10);
			option.setScanSpan(5000); //设置发起定位请求的间隔时间为5000ms  
			option.disableCache(true);
			location_client.setLocOption(option);
			System.out.println("----------setLocationOption------------");
		}
	
	/**
	 * ****************************  PopWindow  ************************************************
	 */
	  /*
     * 创建PopupWindow
     */ 
    private void f_init_popup_window() { 
        LayoutInflater layoutInflater = LayoutInflater.from(this); 
        View popupWindowPage = layoutInflater.inflate(R.layout.view_growth_info_pop_window, null); 
		
		// 创建一个PopupWindow 
        // 参数1：contentView 指定PopupWindow的内容 
        // 参数2：width 指定PopupWindow的width 
        // 参数3：height 指定PopupWindow的height 
        int height = utils_density_transform.dip2px(this, 80);
        int width = utils_density_transform.dip2px(this, 120);
        growth_info_pop_window = new PopupWindow(popupWindowPage, width, height); 
 
        growth_info_pop_window.setOutsideTouchable(true);  
        growth_info_pop_window.setAnimationStyle(android.R.style.Animation_Dialog);  
        growth_info_pop_window.setTouchable(true);  
        growth_info_pop_window.setFocusable(true); 
        growth_info_pop_window.setBackgroundDrawable(new BitmapDrawable());//设置点击窗口外边窗口消失
        growth_info_pop_window.update();
 
        popupWindowPage.setFocusableInTouchMode(true);  
        popupWindowPage.setOnKeyListener(new OnKeyListener() {  
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				 // TODO Auto-generated method stub  
                if ((keyCode == KeyEvent.KEYCODE_MENU)&&(growth_info_pop_window.isShowing())) { 
                	growth_info_pop_window.dismiss();// 这里写明模拟menu的PopupWindow退出就行  
                    return true;  
                }  
                return false;
			}  
        }); 
        LinearLayout grow_info_delete_layout = (LinearLayout)popupWindowPage.findViewById(R.id.grow_info_delete_layout);
		LinearLayout grow_info_share_layout = (LinearLayout)popupWindowPage.findViewById(R.id.grow_info_share_layout);
		if(permissons!=null && permissons.equals("1")){
			grow_info_delete_layout.setVisibility(View.VISIBLE);
		}else{
			grow_info_delete_layout.setVisibility(View.GONE);
		}
		grow_info_delete_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				growth_info_pop_window.dismiss();
				if(bl_delete_growth_able){
					f_delete_growth_info();
				}else{
					Toast.makeText(ActivityViewGrowthInfo.this, "正在处理ing,请稍候...", Toast.LENGTH_LONG);
				}
				
			}
		});
		grow_info_share_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				growth_info_pop_window.dismiss();
				HashMap<String, String> hm = new HashMap<String, String>();
				String share_content = "";
				if(growth_info!=null){
					share_content = growth_info.getWord_record();
				}
				hm.put("content", share_content);
				Intent intent = utils_common_share.shareMethod(hm);// 分享内容
				ActivityViewGrowthInfo.this.startActivity(Intent.createChooser(intent, "分享"));
			}
		});
        
        // 获取屏幕和PopupWindow的width和height 
        screen_width = this.getWindowManager().getDefaultDisplay().getWidth();
        System.out.println("screen_width = "+screen_width);
    } 
	
	/** * 显示PopupWindow窗口 * * @param popupwindow */
	public void f_popup_windw_showing() {
		 int xOff = screen_width - utils_density_transform.dip2px(this, 60);
		 int yOff = utils_density_transform.dip2px(this, 10);
//		growth_info_pop_window.showAtLocation(view_growth_scrollview, 
//				Gravity.RIGHT | Gravity.TOP, 0,xOff);// X、Y方向各偏移 
		growth_info_pop_window.showAsDropDown(view_growth_top_bar_layout, 
				xOff,
				0);// X、Y方向各偏移 
		
	}
	
	/**
	 * *******************************END*****************************************
	 */
	
	private OnItemClickListener gridviewItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parentView, View arg1, int photoIndex,
				long arg3) {
			// TODO Auto-generated method stub
			try{
				if(growth_info!=null){
					Vector<table_album_gridview_photo_path> pic_list = growth_info.getPic_list();
					List<String> pic_path_list = new ArrayList<String>();
					
					if(pic_list!=null){
						for(int i=0;i<pic_list.size();i++){
							pic_path_list.add(pic_list.get(i).getImage_path());
						}
					}
					
					Intent intent = new Intent();
					intent.setClass(ActivityViewGrowthInfo.this, ActivityViewGrowthPhoto.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("pic_list", (Serializable)pic_path_list);
					intent.putExtras(bundle);
					intent.putExtra("photo_index", photoIndex);
					ActivityViewGrowthInfo.this.startActivity(intent);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	};
	
	/**
	 * 获取初始  虚拟数据
	 * @return
	 */
//	private Vector<table_album_gridview_photo_path> get_pic_list(){
//		Vector<table_album_gridview_photo_path> pic_list = new Vector<table_album_gridview_photo_path>();
//		table_album_gridview_photo_path temp = new table_album_gridview_photo_path();
//		temp.setResource_id(R.drawable.baby_pic_1);
//		pic_list.add(temp);
//		
//		temp = new table_album_gridview_photo_path();
//		temp.setResource_id(R.drawable.baby_pic_2);
//		pic_list.add(temp);
//		
//		temp = new table_album_gridview_photo_path();
//		temp.setResource_id(R.drawable.baby_pic_3);
//		pic_list.add(temp);
//		
//		temp = new table_album_gridview_photo_path();
//		temp.setResource_id(R.drawable.baby_pic_4);
//		pic_list.add(temp);
//		
//		temp = new table_album_gridview_photo_path();
//		temp.setResource_id(R.drawable.baby_pic_5);
//		pic_list.add(temp);
//		
//		return pic_list;
//	}
	/**
	 * 删除缓存数据库 记录
	 */
	private boolean deleteGrowthData(String n, dbs_album_baby_growth dbs) {
		try {
			List<table_album_baby_growth> list = dbs.do_select_data("delete from t_album_baby_growth where record_id='" + n + "'");
			list = list == null ? new ArrayList<table_album_baby_growth>() : list;
			if (list.get(0) != null) { return true; }
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}
