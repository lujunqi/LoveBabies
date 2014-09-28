package com.xqj.lovebabies.activitys;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_activity_my_center_my_point_rules_listview;
import com.xqj.lovebabies.adapters.adapter_activity_my_center_my_points_listview;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.dbs_album_baby_growth;
import com.xqj.lovebabies.databases.dbs_my_center_my_points;
import com.xqj.lovebabies.databases.table_album_baby_growth;
import com.xqj.lovebabies.databases.table_album_gridview_photo_path;
import com.xqj.lovebabies.databases.table_my_center_my_points;
import com.xqj.lovebabies.databases.table_my_center_record_rules;
import com.xqj.lovebabies.structures.interface_app_get_my_points_req;
import com.xqj.lovebabies.structures.interface_app_get_my_points_resp;
import com.xqj.lovebabies.structures.interface_app_get_record_rules_req;
import com.xqj.lovebabies.structures.interface_app_get_record_rules_resp;
import com.xqj.lovebabies.structures.interface_app_get_total_point_req;
import com.xqj.lovebabies.structures.interface_app_get_total_point_resp;
import com.xqj.lovebabies.threads.thread_my_center_get_my_points_list;
import com.xqj.lovebabies.threads.thread_my_center_get_record_rules_list;
import com.xqj.lovebabies.threads.thread_my_center_get_total_points_list;
import com.xqj.lovebabies.widgets.UIListView;
import com.xqj.lovebabies.widgets.UIListView.OnRefreshListener;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMyCenterPoints extends Activity {
	public static final int CHANGE_TOP_TEXTVIEW = 110001;
	public static final int CHANGE_HISTORY_TABLE_LAYOUT = 110002;
	public static final int CHANGE_BUY_CHECK_IMAGE = 110003;
	// topBar
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	//主界面
	private TextView top_point_history_textview;
	private TextView top_point_buy_textview;
	private TextView top_point_rules_textview;
	private ViewPager points_viewpager;
	private List<View> view_list = new ArrayList<View>();
	private LayoutInflater inflater = null;
	private MyCenterPointsPaperAdapter pagerAdapter = null;
	
	// 分界面
	private View history_view;
	private View buy_view;
	private View rules_view;
//	private TableLayout history_table_layout;
	
	// 积分记录查询
	private adapter_activity_my_center_my_points_listview my_points_listview_adapter = null;
	private UIListView my_points_listview = null;
	private View bottom_view;
	private TextView say_hello_textview = null;
	private TextView total_point_textview = null;
	private dbs_my_center_my_points db_my_center_my_points;
	private List<table_my_center_my_points> points_list;
	
	// 积分规则查询
	private adapter_activity_my_center_my_point_rules_listview my_point_rules_listview_adapter = null;
	private ListView my_points_rules_listview = null;
	
	// 积分购买 组件
	private TextView buy_account_textview;
	private Button buy_type_huafei_btn;	// 话费付费
	private Button buy_type_zhifubao_btn;// 支付宝付费
	private Button buy_type_yinlian_btn;// 银联付费
	private ImageView buy_check_count_imageview_10;// 10元
	private ImageView buy_check_count_imageview_20;// 20元
	private ImageView buy_check_count_imageview_30;// 30元
	private LinearLayout buy_check_count_layout_10;
	private LinearLayout buy_check_count_layout_20;
	private LinearLayout buy_check_count_layout_30;
	
	private List<ImageView> buy_check_count_imageview_list = new ArrayList<ImageView>();
	private List<Integer> buy_check_count_list = new ArrayList<Integer>();
	private int buy_check_count_current_selected = 0;
	
	private int page_index = 1;
	
	private String user_id;
	private String user_nick_name="";
	private String user_name = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_points);
		
		user_id = PreferencesUtils.getString(this, "user_id");
		user_nick_name = PreferencesUtils.getString(this, "user_nick_name");
		user_name = PreferencesUtils.getString(this, "user_name");
		
		init_top_bar();
		init_main_page();
	}
	
	/**
	 * 初始化头部
	 */
	private void init_top_bar(){
		head_btn_right = (ImageView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		
		head_title.setText("我的积分");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityMyCenterPoints.this.finish();
			}
		});
	}
	
	// 初始化主界面
	private void init_main_page(){
		top_point_history_textview = (TextView)findViewById(R.id.my_center_points_history_textview);
		top_point_buy_textview = (TextView)findViewById(R.id.my_center_points_buy_textview);
		top_point_rules_textview = (TextView)findViewById(R.id.my_center_points_rules_textview);
		points_viewpager = (ViewPager)findViewById(R.id.my_center_points_papger);
		init_view_pager();
		top_point_history_textview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				points_viewpager.setCurrentItem(0);
				f_send_message_to_uihandler(CHANGE_TOP_TEXTVIEW, 0);
			}
		});
		top_point_buy_textview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				points_viewpager.setCurrentItem(1);
				f_send_message_to_uihandler(CHANGE_TOP_TEXTVIEW, 1);
			}
		});
		top_point_rules_textview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				points_viewpager.setCurrentItem(2);
				f_send_message_to_uihandler(CHANGE_TOP_TEXTVIEW, 2);
			}
		});
	}
	
	// 给 change_ui_handler 发送指令
	private void f_send_message_to_uihandler(int msgWhat, int msgArg1){
		Message message = new Message();
		message.what = msgWhat;
		message.arg1 = msgArg1;
		change_ui_handler.sendMessage(message);
	}
	
	/**
	 * 更改UI的显示
	 */
	private Handler change_ui_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			System.out.println("msg.what="+msg.what);
			System.out.println("msg.arg1="+msg.arg1);
			if(msg.what == CHANGE_TOP_TEXTVIEW){
				if(msg.arg1 == 1){// 选中第二个textview，购买积分
					f_change_textview_style(top_point_buy_textview, R.drawable.shape_my_points_green_round_button, R.color.white);
					f_change_textview_style(top_point_history_textview, R.color.orange, R.color.green_textcolor);
					f_change_textview_style(top_point_rules_textview, R.color.orange, R.color.green_textcolor);
				}else if(msg.arg1 == 2){// 选中第三个textview，积分规则
					f_change_textview_style(top_point_rules_textview, R.drawable.shape_my_points_green_round_button, R.color.white);
					f_change_textview_style(top_point_buy_textview, R.color.orange, R.color.green_textcolor);
					f_change_textview_style(top_point_history_textview, R.color.orange, R.color.green_textcolor);
				}else{// 选中第一个textview，积分记录
					f_change_textview_style(top_point_history_textview, R.drawable.shape_my_points_green_round_button, R.color.white);
					f_change_textview_style(top_point_buy_textview, R.color.orange, R.color.green_textcolor);
					f_change_textview_style(top_point_rules_textview, R.color.orange, R.color.green_textcolor);
				}
			}
//			else if(msg.what == CHANGE_HISTORY_TABLE_LAYOUT){// 更新积分记录页面 tablelayout
//				table_my_center_my_points item = new table_my_center_my_points();
//				item.setConsume_style("收藏有声读物：孙悟空大闹天宫");
//				item.setIntegral_count("+5");
//				item.setTime("10:33 2013-12-10");
//				item.setRemark("~~HOHO！好好看");
//				my_points_listview_adapter.addItem(item);
//				
//				item = new table_my_center_my_points();
//				item.setConsume_style("购买积分");
//				item.setIntegral_count("+100");
//				item.setTime("09:12 2013-09-01");
//				my_points_listview_adapter.addItem(item);
//				
//				item = new table_my_center_my_points();
//				item.setConsume_style("购买积分");
//				item.setIntegral_count("+100");
//				item.setTime("09:12 2013-09-01");
//				my_points_listview_adapter.addItem(item);
//				
//				item = new table_my_center_my_points();
//				item.setConsume_style("收藏有声读物：孙悟空大闹天宫");
//				item.setIntegral_count("+5");
//				item.setTime("10:33 2013-12-10");
//				item.setRemark("~~HOHO！好好看");
//				my_points_listview_adapter.addItem(item);
//				
//			}
			else if(msg.what == CHANGE_BUY_CHECK_IMAGE){// 购买积分页面：更改付款金额
				buy_check_count_current_selected = msg.arg1;
				for(int i=0; i<buy_check_count_imageview_list.size(); i++){
					if(buy_check_count_current_selected == i){// 选中此选项
						buy_check_count_imageview_list.get(i).setImageResource(R.drawable.my_center_pointts_buy_check_on);
					}else{
						buy_check_count_imageview_list.get(i).setImageResource(R.drawable.my_center_pointts_buy_check_off);
					}
				}
			}
		}
		
	};
	
	/**
	 **********************View Pager 部分***************************
	 */
	// 更改textview样式
	private void f_change_textview_style(TextView textview, int background_resource, int text_color){
		textview.setBackgroundResource(background_resource);
		textview.setTextColor(this.getResources().getColor(text_color));
	}
	// 初始化 view pager
	private void init_view_pager(){
		inflater = LayoutInflater.from(this);
		init_history_view();
		init_buy_view();
		init_rules_view();
		
		pagerAdapter = new MyCenterPointsPaperAdapter();
		points_viewpager.setAdapter(pagerAdapter);
		points_viewpager.setCurrentItem(0);
		f_send_message_to_uihandler(CHANGE_TOP_TEXTVIEW, 0);
		
		points_viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int index) {
				Message message = new Message();
				message.what = CHANGE_TOP_TEXTVIEW;
				message.arg1 = index;
				change_ui_handler.sendMessage(message);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
	}
	
	class MyCenterPointsPaperAdapter extends PagerAdapter{
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
            return view_list.get(position);  
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
	}
	/**
	 * ***********************  初始化积分记录    ***************************
	 */
	private void init_history_view(){
		history_view = inflater.inflate(R.layout.activity_my_center_points_history, null);
		say_hello_textview = (TextView)history_view.findViewById(R.id.my_center_points_sayhello_textview);
		total_point_textview = (TextView)history_view.findViewById(R.id.my_center_points_totalpoint_textview);
		say_hello_textview.setText("您好，"+user_nick_name);
		my_points_listview = (UIListView)history_view.findViewById(R.id.my_center_points_listview);
		bottom_view = (View)inflater.inflate(R.layout.bottom_view_more, null);
		bottom_view.setVisibility(View.INVISIBLE);
		my_points_listview.addFooterView(bottom_view);
		my_points_listview_adapter = new adapter_activity_my_center_my_points_listview(this);
		my_points_listview.setAdapter(my_points_listview_adapter);
		view_list.add(history_view);
		
		my_points_listview.setonRefreshListener(new OnRefreshListener() {// 下拉刷新
			@Override
			public void onRefresh() {
				page_index=1;
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							f_get_my_points();
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}
					protected void onPostExecute(Void result) {
						my_points_listview_adapter.notifyDataSetChanged();
						my_points_listview.onRefreshComplete();
					}
				}.execute(null);
				bottom_view.setVisibility(View.VISIBLE);
			}
		});
		bottom_view.setOnClickListener(new OnClickListener() {// 查看更多
			@Override
			public void onClick(View arg0) {
				page_index++;
				f_get_my_points();
			}
		});
		
//		history_table_layout = (TableLayout)history_view.findViewById(R.id.my_center_points_history_table_layout);
//		f_send_message_to_uihandler(CHANGE_HISTORY_TABLE_LAYOUT, 0);
		page_index = 1;
		f_get_local_my_points();//查询本地积分记录
		f_get_total_point();//查询总计分数
	}
	/**
	 * ***********************  初始化购买积分    ***************************
	 */
	private void init_buy_view(){
		buy_view = inflater.inflate(R.layout.activity_my_center_points_buy, null);
		view_list.add(buy_view);
		buy_account_textview = (TextView)buy_view.findViewById(R.id.my_center_pointts_buy_account_textview);
		buy_account_textview.setText(user_name);
		buy_type_huafei_btn = (Button)buy_view.findViewById(R.id.my_center_points_buy_type_huafei_btn);
		buy_type_zhifubao_btn = (Button)buy_view.findViewById(R.id.my_center_points_buy_type_zhifubao_btn);
		buy_type_yinlian_btn = (Button)buy_view.findViewById(R.id.my_center_points_buy_type_yinlian_btn);
		buy_type_huafei_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(ActivityMyCenterPoints.this, 
//						"话费支付："+buy_check_count_list.get(buy_check_count_current_selected)+"元", Toast.LENGTH_SHORT).show();
				Toast.makeText(ActivityMyCenterPoints.this, 
						"支付通道暂未开通，敬请期待", Toast.LENGTH_SHORT).show();
			}
		});
		buy_type_zhifubao_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(ActivityMyCenterPoints.this, 
//						"支付宝支付："+buy_check_count_list.get(buy_check_count_current_selected)+"元", Toast.LENGTH_SHORT).show();
				Toast.makeText(ActivityMyCenterPoints.this, 
						"支付通道暂未开通，敬请期待", Toast.LENGTH_SHORT).show();
			}
		});
		buy_type_yinlian_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(ActivityMyCenterPoints.this, 
//						"银联支付："+buy_check_count_list.get(buy_check_count_current_selected)+"元", Toast.LENGTH_SHORT).show();
				Toast.makeText(ActivityMyCenterPoints.this, 
						"支付通道暂未开通，敬请期待", Toast.LENGTH_SHORT).show();
			}
		});
		
		buy_check_count_imageview_10 = (ImageView)buy_view.findViewById(R.id.my_center_pointts_buy_check_imageview_1);
		buy_check_count_imageview_20 = (ImageView)buy_view.findViewById(R.id.my_center_pointts_buy_check_imageview_2);
		buy_check_count_imageview_30 = (ImageView)buy_view.findViewById(R.id.my_center_pointts_buy_check_imageview_3);
		buy_check_count_imageview_list.add(buy_check_count_imageview_10);
		buy_check_count_imageview_list.add(buy_check_count_imageview_20);
		buy_check_count_imageview_list.add(buy_check_count_imageview_30);
		buy_check_count_layout_10 = (LinearLayout)buy_view.findViewById(R.id.my_center_pointts_buy_check_layout_1);
		buy_check_count_layout_20 = (LinearLayout)buy_view.findViewById(R.id.my_center_pointts_buy_check_layout_2);
		buy_check_count_layout_30 = (LinearLayout)buy_view.findViewById(R.id.my_center_pointts_buy_check_layout_3);
		buy_check_count_layout_10.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				f_send_message_to_uihandler(CHANGE_BUY_CHECK_IMAGE, 0);
			}
		});
		buy_check_count_layout_20.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				f_send_message_to_uihandler(CHANGE_BUY_CHECK_IMAGE, 1);
			}
		});
		buy_check_count_layout_30.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				f_send_message_to_uihandler(CHANGE_BUY_CHECK_IMAGE, 2);
			}
		});
		f_send_message_to_uihandler(CHANGE_BUY_CHECK_IMAGE, 0);// 默认第一个选项
		
		buy_check_count_list.add(10);
		buy_check_count_list.add(20);
		buy_check_count_list.add(30);
	}
	/**
	 * ***********************  初始化积分规则    ***************************
	 */
	private void init_rules_view(){
		rules_view = inflater.inflate(R.layout.activity_my_center_points_rules, null);
		my_points_rules_listview = (ListView)rules_view.findViewById(R.id.my_center_points_rules_listview);
		my_point_rules_listview_adapter = new adapter_activity_my_center_my_point_rules_listview(this);
		my_points_rules_listview.setAdapter(my_point_rules_listview_adapter);
		view_list.add(rules_view);
		
//		table_my_center_record_rules rule = new table_my_center_record_rules();
//		rule.setCount_limit("\\");
//		rule.setIntegral_count("+5");
//		rule.setIntegral_reason("登录");
//		rule.setRemark("good");
//		my_point_rules_listview_adapter.addItem(rule);
//		rule = new table_my_center_record_rules();
//		rule.setCount_limit("\\");
//		rule.setIntegral_count("+5");
//		rule.setIntegral_reason("登录");
//		rule.setRemark("good");
//		my_point_rules_listview_adapter.addItem(rule);
//		rule = new table_my_center_record_rules();
//		rule.setCount_limit("\\");
//		rule.setIntegral_count("+5");
//		rule.setIntegral_reason("登录");
//		rule.setRemark("good");
//		my_point_rules_listview_adapter.addItem(rule);
		
		f_get_record_rules();
	}
	
	
	/**
	 * *****************************   网络交互部分   ***************************************
	 */
	private Handler network_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_get_my_points_success){
				interface_app_get_my_points_resp resp = (interface_app_get_my_points_resp)msg.obj;
				if(resp!=null){
					List<table_my_center_my_points> list = resp.getList();
					String this_page = resp.getIndex();
					if(this_page==null || this_page.length()==0 
							|| this_page.equals("0")
							|| this_page.equals("1")){// 第一页查询
						my_points_listview_adapter.removeAll();
					}else{
						if(list==null || list.size()==0){
							Toast.makeText(ActivityMyCenterPoints.this, "没有更多数据", Toast.LENGTH_SHORT).show();
							bottom_view.setVisibility(View.GONE);
						}
					}
					if(list!=null && list.size()>0){
						for(int i=0;i<list.size();i++){
							my_points_listview_adapter.addItem(list.get(i));
						}
					}
				}
			}else if(msg.what == message_what_values.activity_my_center_get_record_rules_success){
				interface_app_get_record_rules_resp resp = (interface_app_get_record_rules_resp)msg.obj;
				if(resp!=null){
					List<table_my_center_record_rules> list = resp.getList();
					if(list!=null && list.size()>0){
						my_point_rules_listview_adapter.removeAll();
						for(int i=0;i<list.size();i++){
							my_point_rules_listview_adapter.addItem(list.get(i));
						}
					}
				}
				
			}else if(msg.what == message_what_values.activity_my_center_get_total_points_success){
				interface_app_get_total_point_resp resp = (interface_app_get_total_point_resp)msg.obj;
				if(resp!=null){
					String total_points = resp.getTotal_integral();
					total_point_textview.setText("您目前的积分总分为："+total_points+"分");
				}
				
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(ActivityMyCenterPoints.this, "查询失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	//本地查询我的积分
	private boolean f_get_local_my_points(){
		try{
			db_my_center_my_points = new dbs_my_center_my_points(this);
			points_list = db_my_center_my_points.do_select_data("select * from(select * from t_my_center_my_points where user_id='"+user_id+"' order by time desc) limit 0,11");
			if(points_list!=null && points_list.size()>0){
				for(int i=0; i<points_list.size(); i++){
					table_my_center_my_points temp = points_list.get(i);
					System.out.println(temp.getUser_id()+
							"--------------Local----------------"+temp.getConsume_style());
					my_points_listview_adapter.addItem(temp);
				}
				
			}else{//网络查询积分
				f_get_my_points();
			}
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	//查询我的积分
	private void f_get_my_points(){
		interface_app_get_my_points_req req = new interface_app_get_my_points_req();
		req.setUser_id(user_id);
		req.setIndex(String.valueOf(page_index));
		new thread_my_center_get_my_points_list(this, network_handler, req).start();
	}
	// 查询积分规则
	private void f_get_record_rules(){
		interface_app_get_record_rules_req req = new interface_app_get_record_rules_req();
		new thread_my_center_get_record_rules_list(this, network_handler, req).start();
	}
	
	// 查询积分总分
	private void f_get_total_point(){
		interface_app_get_total_point_req req = new interface_app_get_total_point_req();
		req.setUser_id(user_id);
		new thread_my_center_get_total_points_list(this, network_handler, req).start();
		
	}
}
