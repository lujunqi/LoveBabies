package com.xqj.lovebabies.activitys;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.widgets.LazyScrollView;
import com.xqj.lovebabies.widgets.LazyScrollView.OnScrollListener;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityTestLazyScroll extends Activity {

	private LazyScrollView lazy_scrollview = null;
	private LinearLayout news_layout_01,news_layout_02;//两列
	private TextView loading_news_textview = null;//加载中ing
	
	private int image_width;//图片显示宽度
	private Integer[] image_list = new Integer[]{
			R.drawable.baby_pic_1,R.drawable.baby_pic_2,
			R.drawable.baby_pic_3,R.drawable.baby_pic_4,
			R.drawable.baby_pic_5,R.drawable.ic_launcher,
			R.drawable.zsabb_album_pic_1,R.drawable.zsabb_album_pic_2,
			R.drawable.zsabb_album_pic_3,R.drawable.zsabb_album_pic_4,
			R.drawable.zsabb_album_pic_5
	};
	
	private int page_number = 0;
	private int page_count = 8;
	private int column_index = 0;
	
	private LayoutInflater inflater = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.z_fragment_interaction_news);
		inflater = LayoutInflater.from(this);
		
		lazy_scrollview = (LazyScrollView)findViewById(R.id.fragment_news_scrollview);
		lazy_scrollview.initView();
		lazy_scrollview.setOnScrollListener(lazy_scroller_listener);
		
		news_layout_01 = (LinearLayout)findViewById(R.id.fragment_news_layout_01);
		news_layout_02 = (LinearLayout)findViewById(R.id.fragment_news_layout_02);
		loading_news_textview = (TextView)findViewById(R.id.fragment_news_loading_textview);
		// 获取显示图片宽度
		image_width = (getWindowManager().getDefaultDisplay().getWidth() - 14) / 2;
		
		addImage();
	}
	
	/***
	 * 加载更多
	 */
	private void addImage() {
		for (int x = page_number * page_count; x < (page_number + 1) * page_count; x++) {
			int img_index  = x%image_list.length;
			System.out.println("img_index-->"+img_index);
			addBitMapToImage(image_list[img_index], column_index, x);
			column_index++;
			if (column_index >= 2)
				column_index = 0;
		}
		page_number++;
	}
	
	public void addBitMapToImage(int resId, int j, int i) {
		LinearLayout item_layout = (LinearLayout)inflater.inflate(R.layout.z_fragment_interaction_news_listview_item, null);
		ImageView news_pic = (ImageView)item_layout.findViewById(R.id.fragment_news_pic_imageview);
		TextView news_title = (TextView)item_layout.findViewById(R.id.fragment_news_title_textview);
		news_title.setText("幼儿园运动会——"+i);
		news_pic.setImageResource(resId);
		int image_height = get_compressed_bitmap_height_from_res(getResources(), resId, image_width);
		LayoutParams layoutParams = news_pic.getLayoutParams();
		layoutParams.height = image_height;
		news_pic.setLayoutParams(layoutParams);
		news_pic.setImageResource(resId);
		
		if (j == 0) {
			news_layout_01.addView(item_layout);
		} else if (j == 1) {
			news_layout_02.addView(item_layout);
		} 
		item_layout.setTag(String.valueOf(i));
		item_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ActivityTestLazyScroll.this,
						"您点击了第" + v.getTag() + "个Item", Toast.LENGTH_SHORT)
						.show();
			}
		});
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
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == 1){
				loading_news_textview.setVisibility(View.VISIBLE);
			}else{
				loading_news_textview.setVisibility(View.GONE);
			}
		}
		
	};
	
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
			handler.sendMessage(handler.obtainMessage(1));
			addImage();
			handler.sendMessage(handler.obtainMessage(2));
		}
	};

}
