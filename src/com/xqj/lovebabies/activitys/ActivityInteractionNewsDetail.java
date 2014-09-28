package com.xqj.lovebabies.activitys;

import cn.trinea.android.common.util.StringUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_interaction_news;
import com.xqj.lovebabies.listeners.listener_activity_interaction_news_detail_on_click;
import com.xqj.lovebabies.structures.interface_app_get_news_req;
import com.xqj.lovebabies.threads.thread_interaction_get_news_detail;
import com.xqj.lovebabies.widgets.TopActionBar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityInteractionNewsDetail extends Activity {

	private ImageView head_left_imageview = null;
	private ImageView head_right_share_imageview = null;
	private ImageView head_right_collect_imageview = null;
	private ImageView head_right_zan_imageview = null;
	private TextView head_title_textview = null;
	
	private TextView cmd_textview_news_title = null;
	private TextView cmd_textview_news_creator = null;
	private TextView cmd_textview_news_create_time = null;
	private ImageView cmd_imageview_news_picture = null;
	private TextView cmd_textview_news_content = null;
	private FrameLayout news_video_layout = null;
	private ImageView news_video_imageview = null;
	private ImageButton news_video_button = null;
	
	private table_interaction_news t_interaction_news = null;
	private interface_app_get_news_req get_news_req = null;
	
	private String news_id = "";
	private String video_url = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_interaction_news_detail);
		try {
			news_id = getIntent().getStringExtra("news_id");
			init_head();
			init();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 初始化头部
	 */
	private void init_head(){
		head_left_imageview = (ImageView)findViewById(R.id.head_left_imageview);
		head_right_share_imageview = (ImageView)findViewById(R.id.head_right_share_imageview);
		head_right_collect_imageview = (ImageView)findViewById(R.id.head_right_collect_imageview);
		head_right_zan_imageview = (ImageView)findViewById(R.id.head_right_zan_imageview);
		head_title_textview = (TextView)findViewById(R.id.head_title_textview);
		head_left_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ActivityInteractionNewsDetail.this.finish();
			}
		});
		head_right_share_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			}
		});
		head_right_collect_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			}
		});
		head_right_zan_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			}
		});
	}

	private void init() {
		try {
			// --
			cmd_textview_news_title = (TextView) findViewById(R.id.activity_interaction_news_detail_textview_news_title);
			cmd_textview_news_creator = (TextView) findViewById(R.id.activity_interaction_news_detail_textview_news_creator);
			cmd_textview_news_create_time = (TextView) findViewById(R.id.activity_interaction_news_detail_textview_news_create_time);
			cmd_textview_news_content = (TextView) findViewById(R.id.activity_interaction_news_detail_textview_news_content);
			cmd_imageview_news_picture = (ImageView) findViewById(R.id.activity_interaction_news_detail_imageview_image_content);
			cmd_imageview_news_picture.setVisibility(View.GONE);
			// --
			news_video_layout = (FrameLayout)findViewById(R.id.activity_interaction_news_detail_video_layout);
			news_video_imageview = (ImageView)findViewById(R.id.activity_interaction_news_detail_video_imageview);
			news_video_button = (ImageButton)findViewById(R.id.activity_interaction_news_detail_video_btn);
			news_video_layout.setVisibility(View.GONE);
			news_video_button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					System.out.println("Onclick news_video_button...");
					play_video();
				}
			});
			news_video_imageview.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					System.out.println("Onclick news_video_imageview...");
					play_video();
				}
			});
			
			f_action_get_news();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//播放视频
	private void play_video(){
		System.out.println(video_url);
		if(video_url!=null && video_url.length()>0){
			Uri uri = Uri.parse(video_url);
	        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
		    intent.setType("video/*");
		    intent.setDataAndType(uri , "video/*");
		    startActivity(intent);
		}else{
			Toast.makeText(ActivityInteractionNewsDetail.this, "视频无法播放", Toast.LENGTH_SHORT).show();
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == message_what_values.activity_interaction_news_detail_get_news_detail_success) {
				f_action_get_news_success(msg);
			}
			if (msg.what == message_what_values.activity_interaction_news_detail_get_news_detail_failture) {
				f_action_get_news_failure();
			}
		};
	};

	private void f_action_get_news() {
		try {
			get_news_req = new interface_app_get_news_req();
			get_news_req.setNews_id(news_id);
			new thread_interaction_get_news_detail(handler, get_news_req).start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_get_news_success(Message message) {
		try {
			t_interaction_news = (table_interaction_news) message.obj;
			try {
				cmd_textview_news_title.setText(t_interaction_news.getNews_title());
			} catch (Exception e) {
			}
			try {
				cmd_textview_news_creator.setText("来自:" + t_interaction_news.getNews_org_name());
				cmd_textview_news_create_time.setText("时间:" + t_interaction_news.getPublish_time());
			} catch (Exception e) {
				// TODO: handle exception
			}
			// --
			try {
				cmd_textview_news_content.setText(t_interaction_news.getNews_content());
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			// --
			try {
				if(t_interaction_news.getVideo_path()!=null 
						&& t_interaction_news.getVideo_path().length()>0){//显示视频
					news_video_layout.setVisibility(View.VISIBLE);
					System.out.println("显示视频");
					video_url = network_interface_paths.get_project_root + t_interaction_news.getVideo_path();
					if(!StringUtils.isBlank(t_interaction_news.getPicture_path())){
						String image_path = "";
						image_path += network_interface_paths.get_project_root;
						image_path += t_interaction_news.getPicture_path();
//						utils_picture_caches.getInstance().init(ActivityInteractionNewsDetail.this);// 初始化图片缓存
//						DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//						builder.showImageOnLoading(R.drawable.default_image_position);
//						builder.showImageForEmptyUri(R.drawable.default_image_err);
//						builder.cacheInMemory(false);
//						builder.cacheOnDisk(true);
//						builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//						DisplayImageOptions options = builder.build();
//						ImageLoader.getInstance().displayImage(image_path, news_video_imageview, options);
						utils_common_tools.f_display_Image(ActivityInteractionNewsDetail.this, 
								news_video_imageview, image_path,R.drawable.default_image_position,
								R.drawable.default_image_err, ImageScaleType.IN_SAMPLE_INT);
					}
				}else if(!StringUtils.isBlank(t_interaction_news.getPicture_path())){//显示图片
					String image_path = "";
					image_path += network_interface_paths.get_project_root;
					image_path += t_interaction_news.getPicture_path();
					cmd_imageview_news_picture.setVisibility(View.VISIBLE);
//					utils_picture_caches.getInstance().init(ActivityInteractionNewsDetail.this);// 初始化图片缓存
//					DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//					builder.showImageOnLoading(R.drawable.default_image_position);
//					builder.showImageForEmptyUri(R.drawable.default_image_err);
//					builder.cacheInMemory(false);
//					builder.cacheOnDisk(true);
//					builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//					DisplayImageOptions options = builder.build();
//					ImageLoader.getInstance().displayImage(image_path, cmd_imageview_news_picture, options);
					utils_common_tools.f_display_Image(ActivityInteractionNewsDetail.this, 
							news_video_imageview, image_path,R.drawable.default_image_position,
							R.drawable.default_image_err, ImageScaleType.IN_SAMPLE_INT);
				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_get_news_failure() {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
	}


}
