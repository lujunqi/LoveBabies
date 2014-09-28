package com.xqj.lovebabies.activitys;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.trinea.android.common.util.PreferencesUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_my_center_favorite;
import com.xqj.lovebabies.structures.interface_app_collect_health_information_req;
import com.xqj.lovebabies.structures.interface_app_collect_health_information_resp;
import com.xqj.lovebabies.structures.interface_app_delete_my_favorite_req;
import com.xqj.lovebabies.structures.interface_app_get_health_information_detail_req;
import com.xqj.lovebabies.structures.interface_app_get_health_information_detail_resp;
import com.xqj.lovebabies.threads.thread_health_collect_information;
import com.xqj.lovebabies.threads.thread_health_get_information_detail;
import com.xqj.lovebabies.threads.thread_my_center_delete_my_favorite;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityHealthDetail extends Activity {
	private ImageView head_left_imageview = null;
	private ImageView head_share_imageview = null;
	private ImageView head_favorite_imageview = null;
	private TextView health_info_title_textview = null;
	private TextView health_info_time_textview = null;
	private TextView health_info_content_textview = null;
	private ImageView health_info_pic_imageview = null;
	
	private String user_id;
	private String content_id;
	private String imgurl;
	private boolean bl_if_collected = false;
	private String title = "";
	private String content = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_health_detail);
		
		user_id = PreferencesUtils.getString(this, "user_id");
		content_id = getIntent().getStringExtra("content_id");
		
		head_left_imageview = (ImageView)findViewById(R.id.head_left_imageview);
		head_share_imageview = (ImageView)findViewById(R.id.head_right_share_imageview);
		head_favorite_imageview = (ImageView)findViewById(R.id.head_right_favorite_imageview);
		head_left_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ActivityHealthDetail.this.finish();
			}
		});
		head_share_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {// 弹出分享页面
				showOnekeyshare(title, content);
			}
		});
		head_favorite_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {//收藏资讯
				
				if(bl_if_collected){
					Toast.makeText(ActivityHealthDetail.this, "已经收藏", Toast.LENGTH_SHORT).show();
				}else{
					f_collect_health_info();
				}
			}
		});
		
		health_info_title_textview = (TextView)findViewById(R.id.health_info_title_textview);
		health_info_time_textview = (TextView)findViewById(R.id.health_info_time_textview);
		health_info_content_textview = (TextView)findViewById(R.id.health_info_content_textview);
		health_info_pic_imageview = (ImageView)findViewById(R.id.health_info_pic_imageview);
		
		f_get_health_info_detail();
		
	}
	
	/***
	 * ***********************  网络交互部分   ************************
	 */
	private Handler network_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.fragment_health_get_information_detail_success){
				interface_app_get_health_information_detail_resp resp = (interface_app_get_health_information_detail_resp)msg.obj;
				if(resp!=null){
					title = resp.getTitle();
					content = resp.getContent();
					health_info_title_textview.setText(resp.getTitle());
					String time_string = resp.getPublish_time()+"  来自"+resp.getSource();
					health_info_time_textview.setText(time_string);
					health_info_content_textview.setText(resp.getContent());
					// 显示图片
					if(resp.getPic_name()!=null && resp.getPic_name().length()>0){
						imgurl = network_interface_paths.get_project_root+ resp.getPic_name();
//						System.out.println("HealthIcon-->"+imgurl);
//						utils_picture_caches.getInstance().init(ActivityHealthDetail.this);// 初始化图片缓存
//						DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//						builder.showImageOnLoading(R.drawable.ic_launcher);
//						builder.showImageForEmptyUri(R.drawable.ic_launcher);
//						builder.cacheInMemory(false);
//						builder.cacheOnDisk(true);
//						builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//						DisplayImageOptions options = builder.build();
//						ImageLoader.getInstance().displayImage(imgurl, health_info_pic_imageview, options);
						utils_common_tools.f_display_Image(ActivityHealthDetail.this, 
								health_info_pic_imageview, imgurl,R.drawable.ic_launcher,
								R.drawable.ic_launcher, ImageScaleType.IN_SAMPLE_INT);
					}
					//判断是否已收藏
					if(resp.getIs_collect()!=null && resp.getIs_collect().equals("1")){//已收藏
						bl_if_collected = true;
						head_favorite_imageview.setImageResource(R.drawable.health_favorite_selected);
					}else{
						bl_if_collected = false;
						head_favorite_imageview.setImageResource(R.drawable.health_favorite_normal);
					}
				}
			}else if(msg.what == message_what_values.fragment_health_collect_information_success){// 收藏资讯成功
				interface_app_collect_health_information_resp resp = 
						(interface_app_collect_health_information_resp)msg.obj;
				if(resp!=null){// && resp.getReturn_code().equals("1")
					if(bl_if_collected){//原来是已收藏,现在切换成未收藏
						bl_if_collected = false;
						head_favorite_imageview.setImageResource(R.drawable.health_favorite_normal);
					}else{
						bl_if_collected = true;
						head_favorite_imageview.setImageResource(R.drawable.health_favorite_selected);
					}
				}else{
					
				}
			}else if(msg.what == message_what_values.fragment_health_get_data_failed){
				Toast.makeText(ActivityHealthDetail.this, "查询失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	// 查询资讯详情
	private void f_get_health_info_detail(){
		interface_app_get_health_information_detail_req req = new interface_app_get_health_information_detail_req();
		req.setId(content_id);
		req.setUser_id(user_id);
		new thread_health_get_information_detail(this, network_handler, req).start();;
	}
	
	// 收藏收藏健康资讯
	private void f_collect_health_info(){
		interface_app_collect_health_information_req req = new interface_app_collect_health_information_req();
		req.setContent_id(content_id);
		req.setUser_id(user_id);
		new thread_health_collect_information(this, network_handler, req).start();
	}
	
	// 分享
	private void showOnekeyshare(final String title,final String content) {
		final OnekeyShare oks = new OnekeyShare();
		String url=global_contants.share_health_info_url+"?ACTION=InfoDetailed&id="+content_id;
		oks.setNotification(R.drawable.ic_launcher, "掌上爱宝贝");
		oks.setTitleUrl(url);
		oks.setTitle(title);
		oks.setText(content+url);
		oks.setSite(url);
		oks.setImageUrl(imgurl);
		// 参考代码配置章节，设置分享参数
		// 构造一个图标
//		Bitmap logo = BitmapFactory.decodeResource(getResources(),
//				R.drawable.ic_launcher);
//		// 定义图标的标签
//		String label = getResources().getString(R.string.app_name);
//		// 图标点击后会通过Toast提示消息
//		OnClickListener listener = new OnClickListener() {
//			public void onClick(View v) {
//				Toast.makeText(ActivityHealthDetail.this, content, Toast.LENGTH_SHORT)
//						.show();
//				oks.finish();
//			}
//		};
//		oks.setCustomerLogo(logo, label, listener);
		oks.setCallback(new PlatformActionListener() {
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				Log.i("Message", arg0+" onError="+arg1);
			}
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				Log.i("Message", arg0+" onComplete="+arg1);
			}
			@Override
			public void onCancel(Platform arg0, int arg1) {
				Log.i("Message", arg0+" onCancel="+arg1);
			}
		});
		oks.show(this);
	}

}
