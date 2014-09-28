package com.xqj.lovebabies.activitys;

import java.util.ArrayList;
import java.util.List;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityViewGrowthPhoto.AlbumViewGroupPaperAdapter;
import com.xqj.lovebabies.widgets.NoticeEditAddView;
import com.xqj.lovebabies.widgets.NoticeEditHistoryView;
import com.xqj.lovebabies.widgets.NoticeEditInterceptView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityInteractionNoticeEditor extends Activity{
	private final int SET_CURRENT_PAGE = 10001;
	
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	private TextView left_bar_textview;
	private TextView mid_bar_textview;
	private TextView right_bar_textview;
	private ViewPager notice_editor_viewpager;
	
	private List<View> view_list = new ArrayList<View>();
	private NoticeEditPaperAdapter pager_adapter;
	private NoticeEditAddView add_view;
	private NoticeEditInterceptView intercept_view;
	private NoticeEditHistoryView history_view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_interaction_notice_editor);
		
		init_top_bar();
		init_main_page();
	}
	
	/**
	 * 初始化主界面
	 */
	private void init_main_page(){
		//初始化选择按钮
		left_bar_textview = (TextView)findViewById(R.id.activity_interaction_notice_editor_left_textview);
		mid_bar_textview = (TextView)findViewById(R.id.activity_interaction_notice_editor_mid_textview);
		right_bar_textview = (TextView)findViewById(R.id.activity_interaction_notice_editor_right_textview);
		left_bar_textview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				change_ui_handler.sendMessage(change_ui_handler.obtainMessage(SET_CURRENT_PAGE, 0, 0));
			}
		});
		mid_bar_textview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				change_ui_handler.sendMessage(change_ui_handler.obtainMessage(SET_CURRENT_PAGE, 1, 0));
			}
		});
		right_bar_textview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				change_ui_handler.sendMessage(change_ui_handler.obtainMessage(SET_CURRENT_PAGE, 2, 0));
			}
		});
		
		//初始化viewPager
		notice_editor_viewpager = (ViewPager)findViewById(R.id.activity_interaction_notice_editor_viewpager_container);
		add_view = new NoticeEditAddView(this);
		intercept_view = new NoticeEditInterceptView(this);
		history_view = new NoticeEditHistoryView(this);
		view_list.add(add_view.getView());
		view_list.add(intercept_view.getView());
		view_list.add(history_view.getView());
		
		pager_adapter = new NoticeEditPaperAdapter();
		notice_editor_viewpager.setAdapter(pager_adapter);
		notice_editor_viewpager.setOnPageChangeListener(new OnPageChangeListener() {  
            @Override  
            public void onPageSelected(int position) {  
            	System.out.println("set_current_page("+position+")");
            	set_current_page(position);
            }
            @Override  
            public void onPageScrolled(int arg0, float arg1, int arg2) {}  
            @Override  
            public void onPageScrollStateChanged(int arg0) {}
        });  
		
		
		//显示第一个页面
		change_ui_handler.sendMessage(change_ui_handler.obtainMessage(SET_CURRENT_PAGE, 0, 0));
	}
	
	private void set_current_page(int index){
		if(index == 0){
			left_bar_textview.setBackgroundResource(R.drawable.interaction_notice_left_selected);
			mid_bar_textview.setBackgroundResource(R.drawable.interaction_notice_middle_normal);
			right_bar_textview.setBackgroundResource(R.drawable.interaction_notice_right_normal);
			
			left_bar_textview.setTextColor(getResources().getColor(R.color.white));
			mid_bar_textview.setTextColor(getResources().getColor(R.color.green_textcolor));
			right_bar_textview.setTextColor(getResources().getColor(R.color.green_textcolor));
		}else if(index == 1){
			left_bar_textview.setBackgroundResource(R.drawable.interaction_notice_left_normal);
			mid_bar_textview.setBackgroundResource(R.drawable.interaction_notice_middle_selected);
			right_bar_textview.setBackgroundResource(R.drawable.interaction_notice_right_normal);
			
			left_bar_textview.setTextColor(getResources().getColor(R.color.green_textcolor));
			mid_bar_textview.setTextColor(getResources().getColor(R.color.white));
			right_bar_textview.setTextColor(getResources().getColor(R.color.green_textcolor));
		}else if(index == 2){
			left_bar_textview.setBackgroundResource(R.drawable.interaction_notice_left_normal);
			mid_bar_textview.setBackgroundResource(R.drawable.interaction_notice_middle_normal);
			right_bar_textview.setBackgroundResource(R.drawable.interaction_notice_right_selected);
			
			left_bar_textview.setTextColor(getResources().getColor(R.color.green_textcolor));
			mid_bar_textview.setTextColor(getResources().getColor(R.color.green_textcolor));
			right_bar_textview.setTextColor(getResources().getColor(R.color.white));
		}
	}

	/**
	 * 初始化头部
	 */
	private void init_top_bar(){
		head_btn_right = (ImageView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		head_title.setText("发送公告");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(ActivityPersonalInfo.RESULT_CANCEL);
				ActivityInteractionNoticeEditor.this.finish();
			}
		});
	}

	/**
	 * 修改界面UI的handler
	 */
	private Handler change_ui_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == SET_CURRENT_PAGE){//切换页面
				set_current_page(msg.arg1);
				notice_editor_viewpager.setCurrentItem(msg.arg1);
			}
		}
	};
	
	/**
	 * 初始化ViewPager
	 */
	class NoticeEditPaperAdapter extends PagerAdapter{
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
					System.out.println("viewpager view onclick................");
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(add_view!=null){
			add_view.onActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
