package com.xqj.lovebabies.activitys;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_album_gridview_photo_path;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ActivityViewGrowthPhoto extends Activity {
	private List<String> pic_list;
	private List<View> view_list = new ArrayList<View>();
	private LayoutInflater inflater = null;
	private LinearLayout dot_number_layout;
	private Button dot_selected_button;
	private ViewPager viewpager = null;
	private AlbumViewGroupPaperAdapter pagerAdapter = null;
	private RelativeLayout growth_photo_layout = null;
	private int origin_page_index = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_album_view_growth_photos);
		
		Bundle bundle = getIntent().getExtras();
		origin_page_index = getIntent().getIntExtra("photo_index", 0);
		pic_list = (List<String>)bundle.getSerializable("pic_list");
		System.out.println("pic_list-->"+pic_list);
		System.out.println("pic_list.size()-->"+pic_list.size());
		if(pic_list!=null){
			for(int k=0;k<pic_list.size();k++){
				ImageView imageView = new ImageView(this);
				f_display_Image(imageView, pic_list.get(k));
				view_list.add(imageView);
			}
		}
		
		growth_photo_layout = (RelativeLayout)findViewById(R.id.growth_photos_layout);
		viewpager = (ViewPager)findViewById(R.id.album_view_photos_viewpager);
		pagerAdapter = new AlbumViewGroupPaperAdapter();
		viewpager.setAdapter(pagerAdapter);
		viewpager.setCurrentItem(origin_page_index);
		
		dot_number_layout = (LinearLayout)findViewById(R.id.dot_num_layout);
//		
//		Bitmap normal_dot_bitmap = BitmapFactory.decodeResource(getResources(), 
//				R.drawable.icon_dot_normal);
		
		for (int i = 0; i < view_list.size(); i++) {  
            Button bt = new Button(this);  
            android.widget.RadioGroup.LayoutParams button_params = 
            		new android.widget.RadioGroup.LayoutParams(2, 2);
            button_params.setMargins(0, 0, 5, 0);
            bt.setLayoutParams(button_params);  
            bt.setBackgroundResource(R.drawable.icon_dot_normal);
			if(i == origin_page_index){
				bt.setBackgroundResource(R.drawable.icon_dot_selected);
				dot_selected_button = bt;
			}
            dot_number_layout.addView(bt);  
        }
		if(pic_list!=null && pic_list.size()<2){// 如果少于两张图片，则不显示下面的小圆点
			dot_number_layout.setVisibility(View.INVISIBLE);
		}
		
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {  
            @Override  
            public void onPageSelected(int position) {  
                if(dot_selected_button != null){  
                	dot_selected_button.setBackgroundResource(R.drawable.icon_dot_normal);  
                }
                Button currentBt = (Button)dot_number_layout.getChildAt(position);
                currentBt.setBackgroundResource(R.drawable.icon_dot_selected);  
                dot_selected_button = currentBt;  
            }
            @Override  
            public void onPageScrolled(int arg0, float arg1, int arg2) {}  
            @Override  
            public void onPageScrollStateChanged(int arg0) {}
        });  
		
//		growth_photo_layout.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				System.out.println("growth_photo_layout onclick................");
//				ActivityViewGrowthPhoto.this.finish();
//			}
//		});
	}
	
	/**
	 * 显示图片
	 * @param imageView
	 * @param pic_path
	 */
	private void f_display_Image(ImageView imageView, String pic_path){
		String imgurl = network_interface_paths.get_project_root+ "img/1/" + pic_path;
		System.out.println("GrowthPhoto-->"+imgurl);
//		utils_picture_caches.getInstance().init(ActivityViewGrowthPhoto.this);// 初始化图片缓存
//		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//		builder.showImageOnLoading(R.drawable.default_image_position);
//		builder.showImageForEmptyUri(R.drawable.default_image_position);
//		builder.cacheInMemory(false);
//		builder.cacheOnDisk(true);
//		builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//		DisplayImageOptions options = builder.build();
//		ImageLoader.getInstance().displayImage(imgurl, imageView, options);
		utils_common_tools.f_display_Image(ActivityViewGrowthPhoto.this, 
				imageView, imgurl,R.drawable.default_image_position,
				R.drawable.default_image_position, ImageScaleType.IN_SAMPLE_INT);
	}

	class AlbumViewGroupPaperAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return view_list.size();
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			Log.i("INFO", "destroy item:"+position);  
            ((ViewPager) container).removeView(view_list.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			Log.i("INFO", "instantiate item:"+position);  
            ((ViewPager) container).addView(view_list.get(position),0); 
            view_list.get(position).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("growth_photo_layout onclick................");
					ActivityViewGrowthPhoto.this.finish();
				}
			});
            return view_list.get(position);  
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
	}
}
