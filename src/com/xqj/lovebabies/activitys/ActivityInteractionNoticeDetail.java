package com.xqj.lovebabies.activitys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.StringUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_activity_interaction_notice_comment_listview;
import com.xqj.lovebabies.adapters.adapter_activity_interaction_notice_detail_gridview_picture;
import com.xqj.lovebabies.adapters.adapter_fragment_album_listview;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_density_transform;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_album_baby_growth;
import com.xqj.lovebabies.databases.table_interaction_notice;
import com.xqj.lovebabies.databases.table_interaction_notice_comment;
import com.xqj.lovebabies.databases.table_interaction_notice_praise;
import com.xqj.lovebabies.listeners.listener_activity_interaction_notice_detail_comment_item_button_delete_on_click;
import com.xqj.lovebabies.listeners.listener_activity_interaction_notice_detail_on_click;
import com.xqj.lovebabies.structures.*;
import com.xqj.lovebabies.threads.thread_interaction_create_notice_comment;
import com.xqj.lovebabies.threads.thread_interaction_delete_notice_comment;
import com.xqj.lovebabies.threads.thread_interaction_get_notice_comment_list;
import com.xqj.lovebabies.threads.thread_interaction_get_notice_detail;
import com.xqj.lovebabies.threads.thread_interaction_get_notice_praise_list;
import com.xqj.lovebabies.threads.thread_interaction_set_notice_praise;
import com.xqj.lovebabies.threads.thread_interaction_unset_notice_praise;
import com.xqj.lovebabies.widgets.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityInteractionNoticeDetail extends Activity {
	public static final int SHOW_ZAN = 1001;
	public static final int DELETE_NOTICE_COMMENT = 1004;// 删除公告评论
	
	
	private ImageView head_left_imageview;
	private TextView head_title_textview;
	
	private ListView comment_listview;
	
	private String notice_id = null;
	
	private LayoutInflater inflater;
	
	private View content_view;
	private ImageView user_icon_imageview;
	private TextView user_name_textview;
	private TextView date_textview;
	private TextView org_textview;
	private TextView type_textview;
	private TextView content_textview;
	private GridView pics_gridview;
	private TextView pics_count_textview;
	private TextView zan_count_textview;
	private TextView commetn_count_textview;
	private LinearLayout zan_layout;
	private TextView zan_textview;
	private ImageView zan_imageview;
	private LinearLayout comment_layout;
	private TextView bottom_zan_list_textview;
	/**
	 * 评论成长记录
	 */
	private android.app.AlertDialog add_comment_dialog = null;
	private EditText comment_content_edittext = null;
	private Button save_button = null;
	private Button reset_button = null;
	private boolean bl_comment_able = true;
	
	private table_interaction_notice t_interaction_notice;
	
	private adapter_activity_interaction_notice_comment_listview comment_listview_adapter;

	private boolean bl_zan = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_interaction_notice_info);
		inflater = LayoutInflater.from(this);
		// 获取notice_id
		Bundle mBundle = getIntent().getExtras();
		t_interaction_notice = (table_interaction_notice)mBundle.get("notice");
		if(t_interaction_notice!=null){
			notice_id = t_interaction_notice.getNotice_id();
		}
		
		init_top_bar();
		init_content_ui();
		
		comment_listview = (ListView)findViewById(R.id.interaction_notice_comments_listview);
		comment_listview_adapter = new adapter_activity_interaction_notice_comment_listview(this, change_ui_handler, "0");
		comment_listview.addHeaderView(content_view);
		comment_listview.setAdapter(comment_listview_adapter);
		
		// 查询comment_list
		f_get_notice_comment();
		// 查询点赞列表
		f_get_notice_praise();
	}
	
	//初始化头部
	private void init_top_bar(){
		head_left_imageview = (ImageView)findViewById(R.id.interaction_notice_left_imageview);
		head_title_textview = (TextView)findViewById(R.id.interaction_notice_title_textview);
		head_left_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ActivityInteractionNoticeDetail.this.finish();
			}
		});
	}
	
	private void init_content_ui(){
		content_view =  inflater.inflate(R.layout.activity_interaction_notice_info_content, null);
	
		user_icon_imageview = (ImageView)content_view.findViewById(R.id.fragment_interaction_notice_item_imageview_sender_icon);
		user_name_textview = (TextView)content_view.findViewById(R.id.fragment_interaction_notice_item_textview_sender_name);
		date_textview = (TextView)content_view.findViewById(R.id.fragment_interaction_notice_item_textview_notice_publish_time);
		org_textview = (TextView)content_view.findViewById(R.id.fragment_interaction_notice_item_textview_org_name);
		type_textview = (TextView)content_view.findViewById(R.id.fragment_interaction_notice_item_textview_notice_type);
		content_textview = (TextView)content_view.findViewById(R.id.fragment_interaction_notice_item_content_textview);
		pics_gridview = (GridView)content_view.findViewById(R.id.fragment_interaction_notice_item_gridview_pictures);
		pics_count_textview = (TextView)content_view.findViewById(R.id.activity_interaction_notice_detail_textview_notice_picture_count);
		zan_count_textview = (TextView)content_view.findViewById(R.id.fragment_interaction_textview_zan_count);
		commetn_count_textview = (TextView)content_view.findViewById(R.id.fragment_interaction_textview_comment_count);
		zan_layout = (LinearLayout)content_view.findViewById(R.id.fragment_interaction_layout_zan);
		zan_textview = (TextView)content_view.findViewById(R.id.fragment_interaction_textview_zan);
		zan_imageview = (ImageView)content_view.findViewById(R.id.fragment_interaction_imageview_zan);
		comment_layout = (LinearLayout)content_view.findViewById(R.id.fragment_interaction_layout_comment);
		bottom_zan_list_textview = (TextView)content_view.findViewById(R.id.interaction_notice_bottom_zan_textveiw);
		if(t_interaction_notice!=null){
			user_name_textview.setText(t_interaction_notice.getNotice_sender_name());
			date_textview.setText(t_interaction_notice.getNotice_publish_time());
			org_textview.setText("来自："+t_interaction_notice.getNotice_org_name());
			type_textview.setText(t_interaction_notice.getNotice_type_name());
			content_textview.setText(t_interaction_notice.getNotice_content());
			zan_count_textview.setText(t_interaction_notice.getNotice_praise_count());
			commetn_count_textview.setText(t_interaction_notice.getNotice_comment_count());
			// 用户头像
			try {
				utils_picture_caches.getInstance().init(ActivityInteractionNoticeDetail.this);// 初始化图片缓存
				String sender_icon = network_interface_paths.get_project_root + t_interaction_notice.getNotice_sender_icon();
//				DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//				builder.showImageOnLoading(R.drawable.ic_launcher);
//				builder.showImageForEmptyUri(R.drawable.ic_launcher);
//				builder.cacheInMemory(false);
//				builder.cacheOnDisk(true);
//				builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//				DisplayImageOptions options = builder.build();
//				ImageLoader.getInstance().displayImage(sender_icon, user_icon_imageview, options);
				utils_common_tools.f_display_Image(ActivityInteractionNoticeDetail.this, 
						user_icon_imageview, sender_icon,R.drawable.ic_launcher,
						R.drawable.ic_launcher, ImageScaleType.IN_SAMPLE_INT);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			// 照片墙
			String images_path = t_interaction_notice.getNotice_picture_breviary();
			String[] image_path_array = images_path.split(",");
			image_path_array = image_path_array == null ? new String[0] : image_path_array;
			try {
				pics_gridview.setAdapter(new adapter_activity_interaction_notice_detail_gridview_picture(this, image_path_array));
				pics_gridview.setOnItemClickListener(gridviewItemClickListener);
				// --
				if (image_path_array.length <= 0) {
					pics_gridview.setVisibility(View.GONE);
					pics_count_textview.setVisibility(View.GONE);
				} else{
					ViewGroup.LayoutParams lparams = pics_gridview.getLayoutParams();
					int height = utils_density_transform.dip2px(this, getGridViewHeight(image_path_array.length));
					lparams.height = height;
//					lparams.width = (80*3)+(10*4);
					pics_gridview.setVisibility(View.VISIBLE);
					pics_gridview.setLayoutParams(lparams);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			pics_count_textview.setText("共"+image_path_array.length+"张 >");
			
			bottom_zan_list_textview.setText("");
			
			zan_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Message msg = new Message();
					msg.what = SHOW_ZAN;
					change_ui_handler.sendMessage(msg);
				}
			});
			
			comment_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// 弹出输入框，提交评论
					showAddCommentDialog();
				}
			});
		}
	}
	
	private Handler change_ui_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == SHOW_ZAN){
				if(bl_zan){// 取消点赞
					zan_imageview.setImageResource(R.drawable.activity_interaction_notice_detail_praise_0);
					zan_textview.setText("点赞");
					bl_zan = false;
					f_cancel_praise();
				}else{// 点赞
					zan_imageview.setImageResource(R.drawable.activity_interaction_notice_detail_praise_1);
					zan_textview.setText("取消");
					bl_zan = true;
					f_add_praise();
				}
			}
		}
	};
	
	/**
	 * 获取相册GridView的高度
	 * @param num
	 * @return
	 */
	public static int getGridViewHeight(int num){
		if(num>0 && num<=3){
			return 80+(10*2);
		}else if(num>3 && num<=6){
			return (80*2)+(10*3);
		}else if(num>6){
			return (80*3)+(10*4);
		}else{
			return 0;			
		}
	}
	
	private OnItemClickListener gridviewItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parentView, View arg1, int photoIndex,
				long arg3) {
			try{			
				if(t_interaction_notice!=null
						&&t_interaction_notice.getNotice_picture_breviary()!=null
						&&t_interaction_notice.getNotice_picture_breviary().length()>0){
					List<String> pic_path_list = new ArrayList<String>();
					String image_path = null;
					image_path = t_interaction_notice.getNotice_picture_breviary();
					image_path = image_path == null ? "" : image_path;
					String[] image_path_array = image_path.split(",");
					image_path_array = image_path_array == null ? new String[0] : image_path_array;
					for(String image : image_path_array){
						pic_path_list.add(image);
					}
					
					Intent intent = new Intent();
					intent.setClass(ActivityInteractionNoticeDetail.this, ActivityViewNoticePhoto.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("pic_list", (Serializable)pic_path_list);
					intent.putExtras(bundle);
					intent.putExtra("photo_index", photoIndex);
					ActivityInteractionNoticeDetail.this.startActivity(intent);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	};
	
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
				if(content_str!=null && content_str.length()>0){
					f_action_submit_notice_comment_content(content_str);
				}else{
					Toast.makeText(ActivityInteractionNoticeDetail.this, "说点儿什么吧...", Toast.LENGTH_LONG).show();
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
	
	/**
	 * *********   网络交互部分      ***********
	 */
	private Handler network_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_interaction_notice_detail_get_praise_list_success){
				interface_app_get_notice_praise_list_resp resp = (interface_app_get_notice_praise_list_resp)msg.obj;
				if(resp!=null){
					List<table_interaction_notice_praise> list = resp.getPraises();
					String praise_users = "";
					if(list!=null && list.size()>0){
						for(table_interaction_notice_praise praise : list){
							praise_users += praise.getPraise_user_nike_name() + ",";
						}
						if(praise_users.length()>0){
							praise_users = praise_users.substring(0, praise_users.length()-1);
						}
					}
					bottom_zan_list_textview.setText(praise_users);
				}else{
					Toast.makeText(ActivityInteractionNoticeDetail.this, "点赞列表加载失败", Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == message_what_values.activity_interaction_notice_detail_get_praise_list_failture){
				Toast.makeText(ActivityInteractionNoticeDetail.this, "点赞列表加载失败", Toast.LENGTH_SHORT).show();
			}else if(msg.what == message_what_values.activity_interaction_notice_detail_get_comment_list_success){
				interface_app_get_notice_comment_list_resp resp = (interface_app_get_notice_comment_list_resp)msg.obj;
				if(resp!=null){
					List<table_interaction_notice_comment> list = resp.getComments();
					if(list!=null && list.size()>0){
						comment_listview_adapter.removeAll();
						for(int i=list.size()-1;i>=0;i--){
							comment_listview_adapter.addItem(list.get(i));
						}
					}
				}else{
					Toast.makeText(ActivityInteractionNoticeDetail.this, "评论列表加载失败", Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == message_what_values.activity_interaction_notice_detail_get_comment_list_failture){
				Toast.makeText(ActivityInteractionNoticeDetail.this, "评论列表加载失败", Toast.LENGTH_SHORT).show();
			}else if(msg.what == message_what_values.activity_interaction_notice_detail_unset_praise_success){
				f_get_notice_praise();
				Toast.makeText(ActivityInteractionNoticeDetail.this, "取消点赞成功", Toast.LENGTH_SHORT).show();
			}else if(msg.what == message_what_values.activity_interaction_notice_detail_unset_praise_failture){
				Toast.makeText(ActivityInteractionNoticeDetail.this, "取消点赞失败", Toast.LENGTH_SHORT).show();
			}else if(msg.what == message_what_values.activity_interaction_notice_detail_set_praise_success){
				f_get_notice_praise();
				Toast.makeText(ActivityInteractionNoticeDetail.this, "点赞成功", Toast.LENGTH_SHORT).show();
			}else if(msg.what == message_what_values.activity_interaction_notice_detail_set_praise_failture){
				Toast.makeText(ActivityInteractionNoticeDetail.this, "点赞失败", Toast.LENGTH_SHORT).show();
			}// 添加成长记录评论
			else if(msg.what == message_what_values.activity_interaction_notice_detail_submit_comment_success){
				Toast.makeText(ActivityInteractionNoticeDetail.this, "评论成功", Toast.LENGTH_SHORT).show();;
				add_comment_dialog.dismiss();
				f_get_notice_comment();
			}else if(msg.what == message_what_values.activity_interaction_notice_detail_submit_comment_failure){
				Toast.makeText(ActivityInteractionNoticeDetail.this, "评论失败", Toast.LENGTH_SHORT).show();;
			}
		}
	};
	
	// 提交评论
	public void f_action_submit_notice_comment_content(String content_str) {
		try {
			if (!StringUtils.isBlank(content_str)) {
				interface_app_create_notice_comment_req create_notice_comment_req = new interface_app_create_notice_comment_req();
				create_notice_comment_req.setComment_content(content_str);
				create_notice_comment_req.setComment_notice_id(notice_id);
				create_notice_comment_req.setComment_sender(PreferencesUtils.getString(this, "user_id"));
				new thread_interaction_create_notice_comment(network_handler, create_notice_comment_req).start();
			} else {
				Toast.makeText(this, "发布的评论不能为空!", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	// 取消点赞
	private void f_cancel_praise(){
		interface_app_unset_notice_praise_req unset_notice_praise_req = new interface_app_unset_notice_praise_req();
		unset_notice_praise_req.setNotice_id(notice_id);
		unset_notice_praise_req.setUser_id(PreferencesUtils.getString(this, "user_id"));
		new thread_interaction_unset_notice_praise(network_handler, unset_notice_praise_req).start();
	}
	// 点赞
	private void f_add_praise(){
		interface_app_set_notice_praise_req set_notice_praise_req = new interface_app_set_notice_praise_req();
		set_notice_praise_req.setNotice_id(notice_id);
		set_notice_praise_req.setUser_id(PreferencesUtils.getString(this, "user_id"));
		new thread_interaction_set_notice_praise(network_handler, set_notice_praise_req).start();
	}
	
	// 获取点赞列表
	private void f_get_notice_praise() {
		try {
			
			interface_app_get_notice_praise_list_req get_notice_praise_list_req = new interface_app_get_notice_praise_list_req();
			get_notice_praise_list_req.setNotice_id(notice_id);
			get_notice_praise_list_req.setUser_id(PreferencesUtils.getString(this, "user_id"));
			new thread_interaction_get_notice_praise_list(network_handler, get_notice_praise_list_req).start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 获取公告评论列表
	public void f_get_notice_comment() {
		try {
			interface_app_get_notice_comment_list_req get_notice_comment_list_req = new interface_app_get_notice_comment_list_req();
			get_notice_comment_list_req.setNotice_id(notice_id);
			get_notice_comment_list_req.setUser_id(PreferencesUtils.getString(this, "user_id"));
			new thread_interaction_get_notice_comment_list(network_handler, get_notice_comment_list_req).start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	
}
