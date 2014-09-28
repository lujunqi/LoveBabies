package com.xqj.lovebabies.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.trinea.android.common.util.PreferencesUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_interaction_news;
import com.xqj.lovebabies.structures.interface_app_get_news_req;
import com.xqj.lovebabies.structures.interface_app_get_news_resp;
import com.xqj.lovebabies.threads.thread_interaction_get_news_list;
import com.xqj.lovebabies.widgets.LazyScrollView;
import com.xqj.lovebabies.widgets.LazyScrollView.OnScrollListener;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentInteractionSchoolNews extends Fragment {
	
	private View news_container = null;
	private LazyScrollView lazy_scrollview = null;
	private LinearLayout news_layout_01,news_layout_02;//两列
	private TextView loading_news_textview = null;//加载中ing
	
	private int image_width = 0;
	private int page_number = 0;
	private int column_index = 0;
	
	private interface_app_get_news_req get_news_req;
	private List<table_interaction_news> news_list = new ArrayList<table_interaction_news>();
	
	private LayoutInflater inflater;
	
	private Map<String, View> view_map = new HashMap<String, View>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			this.inflater = inflater;
			news_container = inflater.inflate(R.layout.z_fragment_interaction_news, null);
			lazy_scrollview = (LazyScrollView)news_container.findViewById(R.id.fragment_news_scrollview);
			lazy_scrollview.initView();
			lazy_scrollview.setOnScrollListener(lazy_scroller_listener);
			
			news_layout_01 = (LinearLayout)news_container.findViewById(R.id.fragment_news_layout_01);
			news_layout_02 = (LinearLayout)news_container.findViewById(R.id.fragment_news_layout_02);
			loading_news_textview = (TextView)news_container.findViewById(R.id.fragment_news_loading_textview);
			// 获取显示图片宽度
			image_width = (getActivity().getWindowManager().getDefaultDisplay().getWidth() - 14) / 2;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return news_container;
	}
	
	@Override
	public void onResume() {
		page_number = 1;
		f_action_get_news_list();
		super.onResume();
	}
	
	// 从网上或者缓存获取动态列表
	private void f_action_get_news_list() {
		try {
			page_number = page_number == 0 ? 1 : page_number;
			System.out.println("从网上或者缓存获取动态列表"+page_number);
			get_news_req = new interface_app_get_news_req();
			get_news_req.setUser_id(PreferencesUtils.getString(getActivity(), "user_id"));
			get_news_req.setPage_number(page_number);
			// --
			new thread_interaction_get_news_list(network_handler, get_news_req).start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 与网络交互的handler
	 */
	private Handler network_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.fragment_interaction_news_get_news_list_success){//查询最新动态成功
				interface_app_get_news_resp resp = (interface_app_get_news_resp)msg.obj;
				if(resp!=null){
					if(resp.getPage_number()==1){//查看最新动态
//						news_layout_01.removeAllViews();
//						news_layout_02.removeAllViews();
					}else{//查看更多
						
					}
					List<table_interaction_news> news = resp.getNews();
					if(news!=null && news.size()>0){
						int column_index = 0;
						for(table_interaction_news news_info : news){
							addBitMapToImage(news_info, column_index, news_list.size());
							column_index++;
							if (column_index >= 2)
								column_index = 0;
							news_list.add(news_info);
						}
						page_number++;
					}else{
						Toast.makeText(getActivity(), "没有更多动态", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == message_what_values.fragment_interaction_news_get_news_list_failure){
				Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
		
	public void addBitMapToImage(table_interaction_news t_interaction_news, int j, int i) {
		LinearLayout item_layout = null;
		if(view_map.containsKey(t_interaction_news.getNews_id())){
			item_layout = (LinearLayout)view_map.get(t_interaction_news.getNews_id());
			System.out.println("view_map.containsKey-->"+t_interaction_news.getNews_id());
			if (j == 0) {
				news_layout_01.removeView(item_layout);
				news_layout_01.addView(item_layout);
			} else if (j == 1) {
				news_layout_02.removeView(item_layout);
				news_layout_02.addView(item_layout);
			}
		}else{
			item_layout = (LinearLayout)inflater.inflate(R.layout.z_fragment_interaction_news_listview_item, null);
			ImageDownLoadAsyncTask imageTask = new ImageDownLoadAsyncTask(t_interaction_news, image_width);
			imageTask.setItem_layout(item_layout);
			imageTask.execute();
			item_layout.setTag(String.valueOf(i));
			item_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(),
							"您点击了第" + v.getTag() + "个Item", Toast.LENGTH_SHORT)
							.show();
				}
			});
			if (j == 0) {
				news_layout_01.addView(item_layout);
			} else if (j == 1) {
				news_layout_02.addView(item_layout);
			}
			view_map.put(t_interaction_news.getNews_id(), item_layout);
			System.out.println("view_map.addKey-->"+t_interaction_news.getNews_id());
		}
		 
	}
	
	/**
	 * 获取按宽度等比压缩的图像的高度
	 * @return
	 */
	public int get_compressed_bitmap_by_height(Bitmap bitmap, int target_width){
		try {
			int height = bitmap.getHeight();// 获取图片的高度.
			int width = bitmap.getWidth();// 获取图片的宽度
			int target_height = (height * target_width) / width;
			return target_height;
		} catch (Exception e) {
			return 0;
		} finally {

		}
	}
	/**
	 * 获取按宽度等比压缩的图像的高度
	 * @return
	 */
	public int get_compressed_bitmap_height_from_res(Resources res, int rid, int target_width){
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeResource(res, rid);
			int height = bitmap.getHeight();// 获取图片的高度.
			int width = bitmap.getWidth();// 获取图片的宽度
			int target_height = (height * target_width) / width;
			return target_height;
		} catch (Exception e) {
			return 0;
		} finally {
			bitmap.recycle();
			bitmap = null;
		}
	}
	
	private LazyScrollView.OnScrollListener lazy_scroller_listener = new OnScrollListener() {
		@Override
		public void onTop() {
			// TODO Auto-generated method stub
			Log.d("LazyScrollView", "top");
		}
		
		@Override
		public void onScroll() {
			// TODO Auto-generated method stub
			Log.d("LazyScrollView", "top");
		}
		
		@Override
		public void onBottom() {
			// TODO Auto-generated method stub
			f_action_get_news_list();
		}
	};
	
	private class ImageDownLoadAsyncTask extends AsyncTask<Void, Void, Bitmap> {
		private table_interaction_news t_interaction_news;
		private LinearLayout item_layout;
		private int image_width;
		private ImageView news_pic;
		
		public ImageDownLoadAsyncTask(table_interaction_news t_interaction_news, int image_width){
			this.t_interaction_news = t_interaction_news;
			this.image_width = image_width;
		}
		
		public void setItem_layout(LinearLayout layout){
			this.item_layout = layout;
			news_pic = (ImageView)item_layout.findViewById(R.id.fragment_news_pic_imageview);
		}
		
		private Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 1){
					if(msg.obj!=null){
						Bitmap drawable = (Bitmap)msg.obj;
						LayoutParams layoutParams = news_pic.getLayoutParams();
						int height = drawable.getHeight();// 获取图片的高度.
						int width = drawable.getWidth();// 获取图片的宽度
						layoutParams.height = (height * image_width) / width;
						news_pic.setLayoutParams(layoutParams);
						news_pic.setImageBitmap(drawable);
					}else{
						news_pic.setImageResource(R.drawable.default_image_position);
					}
				}
			}
		};
		@Override
		protected Bitmap doInBackground(Void... arg0) {
			if(t_interaction_news.getPicture_path()!=null
					&& t_interaction_news.getPicture_path().length()>0){
				String imageuri = "";
				imageuri += network_interface_paths.get_project_root;
				imageuri += t_interaction_news.getPicture_path();
				System.out.println("imageurl-->"+imageuri);
				utils_picture_caches.getInstance().init(getActivity());// 初始化图片缓存
				ImageLoader.getInstance().loadImage(imageuri, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						System.out.println("onLoadingStarted");
					}
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						System.out.println("onLoadingFailed");
					}
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap drawable) {
						System.out.println("onLoadingComplete:"+drawable);
						Message msg = new Message();
						msg.what = 1;
						msg.obj = drawable;
						handler.sendMessage(msg);
					}
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						System.out.println("onLoadingCancelled");
					}
				});
				return null;
			}else{
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Bitmap drawable) {
			// TODO Auto-generated method stub
			super.onPostExecute(drawable);
			TextView news_title = (TextView)item_layout.findViewById(R.id.fragment_news_title_textview);
			TextView news_org = (TextView)item_layout.findViewById(R.id.fragment_news_org_textview);
			TextView news_time = (TextView)item_layout.findViewById(R.id.fragment_news_date_textview);
			TextView news_content = (TextView)item_layout.findViewById(R.id.fragment_news_content_textview);
			
			TextView zan_count = (TextView)item_layout.findViewById(R.id.fragment_news_zan_count_textview);
			TextView comment_count = (TextView)item_layout.findViewById(R.id.fragment_news_comment_count_textview);
			TextView share_count = (TextView)item_layout.findViewById(R.id.fragment_news_share_count_textview);
			TextView collect_count = (TextView)item_layout.findViewById(R.id.fragment_news_collect_count_textview);
			
			news_title.setText(t_interaction_news.getNews_title());
			news_org.setText(t_interaction_news.getNews_org_name());
			news_time.setText(t_interaction_news.getPublish_time());
			String content_str = "";
			if(t_interaction_news.getNews_content()!=null 
					&& t_interaction_news.getNews_content().length()>0){
				if(t_interaction_news.getNews_content().length()>23){
					content_str = t_interaction_news.getNews_content().substring(0, 20) + "...";
				}else{
					content_str = t_interaction_news.getNews_content();
				}
			}
			news_content.setText(content_str);
			zan_count.setText(t_interaction_news.getPraise_count());
			comment_count.setText(t_interaction_news.getComment_count());
			share_count.setText(t_interaction_news.getShare_count());
			collect_count.setText(t_interaction_news.getCollect_count());
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
}
