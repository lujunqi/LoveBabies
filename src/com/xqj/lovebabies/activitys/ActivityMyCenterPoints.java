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
	
	//������
	private TextView top_point_history_textview;
	private TextView top_point_buy_textview;
	private TextView top_point_rules_textview;
	private ViewPager points_viewpager;
	private List<View> view_list = new ArrayList<View>();
	private LayoutInflater inflater = null;
	private MyCenterPointsPaperAdapter pagerAdapter = null;
	
	// �ֽ���
	private View history_view;
	private View buy_view;
	private View rules_view;
//	private TableLayout history_table_layout;
	
	// ���ּ�¼��ѯ
	private adapter_activity_my_center_my_points_listview my_points_listview_adapter = null;
	private UIListView my_points_listview = null;
	private View bottom_view;
	private TextView say_hello_textview = null;
	private TextView total_point_textview = null;
	private dbs_my_center_my_points db_my_center_my_points;
	private List<table_my_center_my_points> points_list;
	
	// ���ֹ����ѯ
	private adapter_activity_my_center_my_point_rules_listview my_point_rules_listview_adapter = null;
	private ListView my_points_rules_listview = null;
	
	// ���ֹ��� ���
	private TextView buy_account_textview;
	private Button buy_type_huafei_btn;	// ���Ѹ���
	private Button buy_type_zhifubao_btn;// ֧��������
	private Button buy_type_yinlian_btn;// ��������
	private ImageView buy_check_count_imageview_10;// 10Ԫ
	private ImageView buy_check_count_imageview_20;// 20Ԫ
	private ImageView buy_check_count_imageview_30;// 30Ԫ
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
	 * ��ʼ��ͷ��
	 */
	private void init_top_bar(){
		head_btn_right = (ImageView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		
		head_title.setText("�ҵĻ���");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityMyCenterPoints.this.finish();
			}
		});
	}
	
	// ��ʼ��������
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
	
	// �� change_ui_handler ����ָ��
	private void f_send_message_to_uihandler(int msgWhat, int msgArg1){
		Message message = new Message();
		message.what = msgWhat;
		message.arg1 = msgArg1;
		change_ui_handler.sendMessage(message);
	}
	
	/**
	 * ����UI����ʾ
	 */
	private Handler change_ui_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			System.out.println("msg.what="+msg.what);
			System.out.println("msg.arg1="+msg.arg1);
			if(msg.what == CHANGE_TOP_TEXTVIEW){
				if(msg.arg1 == 1){// ѡ�еڶ���textview���������
					f_change_textview_style(top_point_buy_textview, R.drawable.shape_my_points_green_round_button, R.color.white);
					f_change_textview_style(top_point_history_textview, R.color.orange, R.color.green_textcolor);
					f_change_textview_style(top_point_rules_textview, R.color.orange, R.color.green_textcolor);
				}else if(msg.arg1 == 2){// ѡ�е�����textview�����ֹ���
					f_change_textview_style(top_point_rules_textview, R.drawable.shape_my_points_green_round_button, R.color.white);
					f_change_textview_style(top_point_buy_textview, R.color.orange, R.color.green_textcolor);
					f_change_textview_style(top_point_history_textview, R.color.orange, R.color.green_textcolor);
				}else{// ѡ�е�һ��textview�����ּ�¼
					f_change_textview_style(top_point_history_textview, R.drawable.shape_my_points_green_round_button, R.color.white);
					f_change_textview_style(top_point_buy_textview, R.color.orange, R.color.green_textcolor);
					f_change_textview_style(top_point_rules_textview, R.color.orange, R.color.green_textcolor);
				}
			}
//			else if(msg.what == CHANGE_HISTORY_TABLE_LAYOUT){// ���»��ּ�¼ҳ�� tablelayout
//				table_my_center_my_points item = new table_my_center_my_points();
//				item.setConsume_style("�ղ������������մ����칬");
//				item.setIntegral_count("+5");
//				item.setTime("10:33 2013-12-10");
//				item.setRemark("~~HOHO���úÿ�");
//				my_points_listview_adapter.addItem(item);
//				
//				item = new table_my_center_my_points();
//				item.setConsume_style("�������");
//				item.setIntegral_count("+100");
//				item.setTime("09:12 2013-09-01");
//				my_points_listview_adapter.addItem(item);
//				
//				item = new table_my_center_my_points();
//				item.setConsume_style("�������");
//				item.setIntegral_count("+100");
//				item.setTime("09:12 2013-09-01");
//				my_points_listview_adapter.addItem(item);
//				
//				item = new table_my_center_my_points();
//				item.setConsume_style("�ղ������������մ����칬");
//				item.setIntegral_count("+5");
//				item.setTime("10:33 2013-12-10");
//				item.setRemark("~~HOHO���úÿ�");
//				my_points_listview_adapter.addItem(item);
//				
//			}
			else if(msg.what == CHANGE_BUY_CHECK_IMAGE){// �������ҳ�棺���ĸ�����
				buy_check_count_current_selected = msg.arg1;
				for(int i=0; i<buy_check_count_imageview_list.size(); i++){
					if(buy_check_count_current_selected == i){// ѡ�д�ѡ��
						buy_check_count_imageview_list.get(i).setImageResource(R.drawable.my_center_pointts_buy_check_on);
					}else{
						buy_check_count_imageview_list.get(i).setImageResource(R.drawable.my_center_pointts_buy_check_off);
					}
				}
			}
		}
		
	};
	
	/**
	 **********************View Pager ����***************************
	 */
	// ����textview��ʽ
	private void f_change_textview_style(TextView textview, int background_resource, int text_color){
		textview.setBackgroundResource(background_resource);
		textview.setTextColor(this.getResources().getColor(text_color));
	}
	// ��ʼ�� view pager
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
	 * ***********************  ��ʼ�����ּ�¼    ***************************
	 */
	private void init_history_view(){
		history_view = inflater.inflate(R.layout.activity_my_center_points_history, null);
		say_hello_textview = (TextView)history_view.findViewById(R.id.my_center_points_sayhello_textview);
		total_point_textview = (TextView)history_view.findViewById(R.id.my_center_points_totalpoint_textview);
		say_hello_textview.setText("���ã�"+user_nick_name);
		my_points_listview = (UIListView)history_view.findViewById(R.id.my_center_points_listview);
		bottom_view = (View)inflater.inflate(R.layout.bottom_view_more, null);
		bottom_view.setVisibility(View.INVISIBLE);
		my_points_listview.addFooterView(bottom_view);
		my_points_listview_adapter = new adapter_activity_my_center_my_points_listview(this);
		my_points_listview.setAdapter(my_points_listview_adapter);
		view_list.add(history_view);
		
		my_points_listview.setonRefreshListener(new OnRefreshListener() {// ����ˢ��
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
		bottom_view.setOnClickListener(new OnClickListener() {// �鿴����
			@Override
			public void onClick(View arg0) {
				page_index++;
				f_get_my_points();
			}
		});
		
//		history_table_layout = (TableLayout)history_view.findViewById(R.id.my_center_points_history_table_layout);
//		f_send_message_to_uihandler(CHANGE_HISTORY_TABLE_LAYOUT, 0);
		page_index = 1;
		f_get_local_my_points();//��ѯ���ػ��ּ�¼
		f_get_total_point();//��ѯ�ܼƷ���
	}
	/**
	 * ***********************  ��ʼ���������    ***************************
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
//						"����֧����"+buy_check_count_list.get(buy_check_count_current_selected)+"Ԫ", Toast.LENGTH_SHORT).show();
				Toast.makeText(ActivityMyCenterPoints.this, 
						"֧��ͨ����δ��ͨ�������ڴ�", Toast.LENGTH_SHORT).show();
			}
		});
		buy_type_zhifubao_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(ActivityMyCenterPoints.this, 
//						"֧����֧����"+buy_check_count_list.get(buy_check_count_current_selected)+"Ԫ", Toast.LENGTH_SHORT).show();
				Toast.makeText(ActivityMyCenterPoints.this, 
						"֧��ͨ����δ��ͨ�������ڴ�", Toast.LENGTH_SHORT).show();
			}
		});
		buy_type_yinlian_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(ActivityMyCenterPoints.this, 
//						"����֧����"+buy_check_count_list.get(buy_check_count_current_selected)+"Ԫ", Toast.LENGTH_SHORT).show();
				Toast.makeText(ActivityMyCenterPoints.this, 
						"֧��ͨ����δ��ͨ�������ڴ�", Toast.LENGTH_SHORT).show();
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
		f_send_message_to_uihandler(CHANGE_BUY_CHECK_IMAGE, 0);// Ĭ�ϵ�һ��ѡ��
		
		buy_check_count_list.add(10);
		buy_check_count_list.add(20);
		buy_check_count_list.add(30);
	}
	/**
	 * ***********************  ��ʼ�����ֹ���    ***************************
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
//		rule.setIntegral_reason("��¼");
//		rule.setRemark("good");
//		my_point_rules_listview_adapter.addItem(rule);
//		rule = new table_my_center_record_rules();
//		rule.setCount_limit("\\");
//		rule.setIntegral_count("+5");
//		rule.setIntegral_reason("��¼");
//		rule.setRemark("good");
//		my_point_rules_listview_adapter.addItem(rule);
//		rule = new table_my_center_record_rules();
//		rule.setCount_limit("\\");
//		rule.setIntegral_count("+5");
//		rule.setIntegral_reason("��¼");
//		rule.setRemark("good");
//		my_point_rules_listview_adapter.addItem(rule);
		
		f_get_record_rules();
	}
	
	
	/**
	 * *****************************   ���罻������   ***************************************
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
							|| this_page.equals("1")){// ��һҳ��ѯ
						my_points_listview_adapter.removeAll();
					}else{
						if(list==null || list.size()==0){
							Toast.makeText(ActivityMyCenterPoints.this, "û�и�������", Toast.LENGTH_SHORT).show();
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
					total_point_textview.setText("��Ŀǰ�Ļ����ܷ�Ϊ��"+total_points+"��");
				}
				
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(ActivityMyCenterPoints.this, "��ѯʧ��", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	//���ز�ѯ�ҵĻ���
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
				
			}else{//�����ѯ����
				f_get_my_points();
			}
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	//��ѯ�ҵĻ���
	private void f_get_my_points(){
		interface_app_get_my_points_req req = new interface_app_get_my_points_req();
		req.setUser_id(user_id);
		req.setIndex(String.valueOf(page_index));
		new thread_my_center_get_my_points_list(this, network_handler, req).start();
	}
	// ��ѯ���ֹ���
	private void f_get_record_rules(){
		interface_app_get_record_rules_req req = new interface_app_get_record_rules_req();
		new thread_my_center_get_record_rules_list(this, network_handler, req).start();
	}
	
	// ��ѯ�����ܷ�
	private void f_get_total_point(){
		interface_app_get_total_point_req req = new interface_app_get_total_point_req();
		req.setUser_id(user_id);
		new thread_my_center_get_total_points_list(this, network_handler, req).start();
		
	}
}
