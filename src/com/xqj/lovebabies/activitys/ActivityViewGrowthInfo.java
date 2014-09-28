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
//	public static final int RESET_COMMENT_LIST_HEIGH = 1002;// ���������б�߶�
	public static final int DELETE_GROWTH_INFO = 1003;// ɾ���ɳ���¼
	public static final int DELETE_GROWTH_COMMENT = 1004;// ɾ���ɳ���¼
	private final int STOP_LOCATION_CLIENT = 9001;// �رյ�ͼ����
	// PopupWindow����
	private PopupWindow growth_info_pop_window = null;
	
	/**
	 * �ٶȵ�ͼӦ��
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
	private TextView fragment_album_textview_title = null; // growth info ��������
	private TextView fragment_album_textview_age = null;	// �������
	private TextView fragment_album_textview_date = null;	// ��¼ʱ��
	private LinearLayout fragment_album_layout_zan = null;//����
	private ImageView fragment_album_imageview_zan = null;// ����ͼƬ
	private TextView fragment_album_textview_zan = null;// ��������
	private LinearLayout fragment_album_layout_comment = null;//����
	
	private int screen_width = 0;// ��Ļ���
	
	
	/**
	 * �����˽���
	 */
	private boolean bl_zan = false;
	// ɾ���ɳ���¼
	private interface_app_delete_baby_growth_req delete_growth_request = null;
	// ��ӳɳ���¼����
	private interface_app_add_baby_growth_comment_req add_growth_comment_request = null;
	// ��ѯ�����б�
	private interface_app_get_baby_growth_comment_req get_growth_comment_list_request = null;
	// ��ѯ�����б�
	private interface_app_get_baby_growth_praise_req get_growth_praise_list_request = null;
	// ɾ������
	private interface_app_delete_baby_growth_comment_req delete_growth_comment_request = null;
	// ȡ������
	private interface_app_cancel_praise_baby_growth_req cancel_praise_growth_request = null;
	// ���޳ɳ���¼
	private interface_app_praise_baby_growth_req praise_growth_request = null;
	// �����б�
	private List<table_album_growth_comment> comment_list = null;
	// �����б�
	private List<table_album_growth_praise> praise_list = null;
	// �Ƿ��ѵ���
	private String if_praised = null;// ��ʼֵ����ʾδ�ɹ���ѯ�����б�
	
	/**
	 * �����б�
	 */
	private TextView album_growth_comment_zan_textveiw = null;
	private ImageView growth_comment_zan_imageview = null;
	/**
	 * ���۳ɳ���¼
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
	private String record_id = null;	// �ɳ���¼ID
	
	private boolean bl_delete_growth_able = true;// ���ɾ���ɳ���¼�Ƿ���Ӧ
	private LayoutInflater inflater = null;
	
	private String permissons = "0";//�û��Ա����Ĳ���Ȩ��
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_album_growth_info);
		inflater = LayoutInflater.from(this);
		
		// �ٶȵ�ͼ
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
		
		f_init_popup_window();// ��ʼ�� popWindow
		
		getAddress();// ��ʼ���ٶȵ�ͼ��λ
		location_client.start();// ��λ��ͼ
	}
	
	
	
	/****
	 * ********************************   ��ʼ������   **********************************
	 */
	/**
	 * ��ʼ��ͷ��UI
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
	 * ��ʼ������xml
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
				// ����������ύ����
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
	 * �������Ľ����� handler
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
				if(bl_zan){// ȡ������
					fragment_album_imageview_zan.setImageResource(R.drawable.activity_interaction_notice_detail_praise_0);
					fragment_album_textview_zan.setText("����");
					bl_zan = false;
					f_cancel_praise();
				}else{// ����
					fragment_album_imageview_zan.setImageResource(R.drawable.activity_interaction_notice_detail_praise_1);
					fragment_album_textview_zan.setText("ȡ��");
					bl_zan = true;
					f_add_praise(location_info, "");
				}
			}else if(msg.what == DELETE_GROWTH_COMMENT){// ����ɾ������
				table_album_growth_comment comment = (table_album_growth_comment)comment_listview_adapter.getItem(msg.arg1);
				f_delete_comment(comment.getComm_id());
			}
			// ɾ���ɳ���¼���ۣ����ؽ��
			else if(msg.what == message_what_values.activity_delete_baby_growth_comment_success){
				interface_app_delete_baby_growth_comment_resp resp = (interface_app_delete_baby_growth_comment_resp)msg.obj;
				if(resp.getResultCode().equals(resp.SUCCESS)){
					Toast.makeText(ActivityViewGrowthInfo.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
					f_get_comment_list();
				}else if(resp.getResultCode().equals(resp.FAILED_NO_PERMISSION)){
					Toast.makeText(ActivityViewGrowthInfo.this, "ɾ��ʧ�ܣ���Ȩ��", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(ActivityViewGrowthInfo.this, "ɾ��ʧ��", Toast.LENGTH_SHORT).show();
				}
			}
			// ɾ���ɳ���¼
			else if(msg.what == message_what_values.activity_delete_baby_growth_success){// ɾ���ɳ���¼
				interface_app_delete_baby_growth_resp resp = (interface_app_delete_baby_growth_resp)msg.obj;
				if(resp.getResultCode().equals(resp.SUCCESS)){
					Toast.makeText(ActivityViewGrowthInfo.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
					dbs_album_baby_growth dbs = new dbs_album_baby_growth(ActivityViewGrowthInfo.this);
					deleteGrowthData(resp.getGrowth_id(), dbs);//ɾ�����ݿ⻺��
					ActivityViewGrowthInfo.this.finish();
				}else{
					Toast.makeText(ActivityViewGrowthInfo.this, "ɾ��ʧ��", Toast.LENGTH_SHORT).show();
					bl_delete_growth_able = true;
				}
			}else if(msg.what == message_what_values.activity_delete_baby_growth_failed){
				Toast.makeText(ActivityViewGrowthInfo.this, "ɾ��ʧ��", Toast.LENGTH_SHORT).show();
				bl_delete_growth_able = true;
			}
			// ��ӳɳ���¼����
			else if(msg.what == message_what_values.activity_add_baby_growth_comment_success){
				Toast.makeText(ActivityViewGrowthInfo.this, "���۳ɹ�", Toast.LENGTH_SHORT).show();;
				add_comment_dialog.dismiss();
				f_get_comment_list();
			}
			// ��ѯ�ɳ���¼����
			else if(msg.what == message_what_values.activity_get_baby_growth_comment_list_success){
				interface_app_get_baby_growth_comment_resp resp = (interface_app_get_baby_growth_comment_resp)msg.obj;
				comment_list = resp.getList();
				if(comment_list!=null && comment_list.size()>0){
					comment_listview_adapter.removeAll();
					if(if_praised!=null && if_praised.equals("1")){// ����
						bl_zan = true;// ���ޣ��ٵ����ȡ��
						fragment_album_textview_zan.setText("ȡ��");
						fragment_album_imageview_zan.setImageResource(R.drawable.activity_interaction_notice_detail_praise_1);
					}else{
						bl_zan = false;// ���������
						fragment_album_textview_zan.setText("����");
						fragment_album_imageview_zan.setImageResource(R.drawable.activity_interaction_notice_detail_praise_0);		
					}
					if(praise_list!=null && praise_list.size()>0){
						
					}
					// ����������б�
					for(int i=comment_list.size()-1; i>=0; i--){
						table_album_growth_comment comment_item = comment_list.get(i);
						comment_listview_adapter.addItem(comment_item);
					}
					// ����listview�ĸ߶�
//					setListViewHeightBasedOnChildren(comment_listview);
				}
			}
			// ��ѯ�ɳ���¼�����б�
			else if(msg.what == message_what_values.activity_get_baby_growth_praise_list_success){
				interface_app_get_baby_growth_praise_resp resp = (interface_app_get_baby_growth_praise_resp)msg.obj;
				praise_list = resp.getList();
				if_praised = resp.getIs_praised();
				if(if_praised!=null && if_praised.equals("1")){// ����
					bl_zan = true;// ���ޣ��ٵ����ȡ��
					fragment_album_textview_zan.setText("ȡ��");
					fragment_album_imageview_zan.setImageResource(R.drawable.activity_interaction_notice_detail_praise_1);
				}else{
					bl_zan = false;// ���������
					fragment_album_textview_zan.setText("����");
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
			// ���޳ɹ�
			else if(msg.what == message_what_values.activity_praise_baby_growth_success){
				f_get_praise_list();
			}
			// ȡ�����޳ɹ�
			else if(msg.what == message_what_values.activity_cancel_praise_baby_growth_success){
				f_get_praise_list();
			}
			else if(msg.what == message_what_values.activity_album_get_data_timeout){
				Toast.makeText(ActivityViewGrowthInfo.this, "����ʧ��,�������", Toast.LENGTH_SHORT).show();
			}
			
			
			else if(msg.what == STOP_LOCATION_CLIENT){// �رյ�ͼ����
				location_client.stop();
			}
		}
		
	};
	
	/**
	 * ���� listview �߶� scrollview �� listview����
	 * 
	 * @param listView
	 *            Ҫ���ø߶ȵ�listview����
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
//		// ���ñ߾�
////		 ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
//		listView.setLayoutParams(params);
//	}
	
	/**
	 * *****************************  �����˽���  *****************************************
	 */
	
	/**
	 * ��ѯ�����б�
	 */
	private void f_get_comment_list(){
		get_growth_comment_list_request = new interface_app_get_baby_growth_comment_req();
		String user_id = PreferencesUtils.getString(this, "user_id");
		get_growth_comment_list_request.setUser_id(user_id);
		get_growth_comment_list_request.setRecord_id(record_id);
		System.out.println("--------------------------   ��ѯ�ɳ���¼�������б�  -------------------------");
		System.out.println("user_id-->"+user_id);
		System.out.println("record_id-->"+record_id);
		new thread_album_get_growth_comment_list(widge_show_change_handler, get_growth_comment_list_request).start();
	}
	
	/**
	 * ��ѯ�����б�
	 */
	private void f_get_praise_list(){
		get_growth_praise_list_request = new interface_app_get_baby_growth_praise_req();
		String user_id = PreferencesUtils.getString(this, "user_id");
		get_growth_praise_list_request.setUser_id(user_id);
		get_growth_praise_list_request.setRecord_id(record_id);
		System.out.println("--------------------------   ��ѯ�ɳ���¼�ĵ����б�  -------------------------");
		System.out.println("user_id-->"+user_id);
		System.out.println("record_id-->"+record_id);
		new thread_album_get_growth_praise_list(widge_show_change_handler, get_growth_praise_list_request).start();
	}
	
	/**
	 * ɾ������
	 */
	private void f_delete_comment(String comm_id){
		delete_growth_comment_request = new interface_app_delete_baby_growth_comment_req();
		String user_id = PreferencesUtils.getString(this, "user_id");
		delete_growth_comment_request.setUser_id(user_id);
		delete_growth_comment_request.setComment_id(comm_id);
		System.out.println("--------------------------   ɾ���ɳ���¼������  -------------------------");
		System.out.println("user_id-->"+user_id);
		System.out.println("comm_id-->"+comm_id);
		new thread_album_delete_baby_growth_comment(widge_show_change_handler, delete_growth_comment_request).start();
	}
	
	/**
	 * ���۳ɳ���¼
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
		System.out.println("--------------------------   ��ӳɳ���¼������  -------------------------");
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
	 * ���ɳ���¼����
	 */
	private void f_add_praise(String location, String remark){
		praise_growth_request = new interface_app_praise_baby_growth_req();
		String user_id = PreferencesUtils.getString(this, "user_id");
		praise_growth_request.setUser_id(user_id);
		praise_growth_request.setRecord_id(record_id);
		praise_growth_request.setOper_mation(global_contants.oper_mation);
		praise_growth_request.setLocation(location);
		praise_growth_request.setRemark(remark);
		System.out.println("--------------------------   ���ɳ���¼����  -------------------------");
		System.out.println("user_id-->"+user_id);
		System.out.println("record_id-->"+record_id);
		System.out.println("oper_mation-->"+global_contants.oper_mation);
		System.out.println("location-->"+location);
		System.out.println("remark-->"+remark);
		new thread_album_praise_baby_growth(widge_show_change_handler, praise_growth_request).start();
	
	}
	
	/**
	 * ȡ������
	 */
	private void f_cancel_praise(){
		cancel_praise_growth_request = new interface_app_cancel_praise_baby_growth_req();
		String user_id = PreferencesUtils.getString(this, "user_id");
		cancel_praise_growth_request.setUser_id(user_id);
		cancel_praise_growth_request.setRecord_id(record_id);
		System.out.println("--------------------------   ȡ���ɳ���¼����  -------------------------");
		System.out.println("user_id-->"+user_id);
		System.out.println("record_id-->"+record_id);
		new thread_album_cancel_praise_baby_growth(widge_show_change_handler, cancel_praise_growth_request).start();
	}
	
	/**
	 * ɾ���ɳ���¼
	 */
	private void f_delete_growth_info(){
		delete_growth_request = new interface_app_delete_baby_growth_req();
		String user_id = PreferencesUtils.getString(this, "user_id");
		delete_growth_request.setUser_id(user_id);
		delete_growth_request.setRecord_id(record_id);
		System.out.println("--------------------------   ɾ���ɳ���¼  -------------------------");
		System.out.println("user_id-->"+user_id);
		System.out.println("record_id-->"+record_id);
		new thread_album_delete_baby_growth(widge_show_change_handler, delete_growth_request).start();
		bl_delete_growth_able = false;
	}
	
	/**
	 * ******************************** �����б� ****************************************
	 */
	/**
	 * ��ʼ�������б�
	 */
	private void init_comment_listview(){
		f_get_comment_list();
		f_get_praise_list();
	}

	/**
	 ***************************** �����Ի���  ****************************
	 */
	/**
	 * ���� ������� �ĶԻ���
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
				// �ύ����
				String content_str = comment_content_edittext.getText().toString();
				String location_str = location_info_textview.getText().toString();
				location_str = (location_str==null || location_str.equals("null")) ? "" : location_str;
				if(content_str!=null && content_str.length()>0){
					f_add_comment(content_str, location_str, "", global_contants.oper_mation, "");
				}else{
					Toast.makeText(ActivityViewGrowthInfo.this, "˵���ʲô��...", Toast.LENGTH_LONG).show();
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
	
	/*****************************  �ٶȵ�ͼ��λ   ************************************/
	// ��λ��ַ��Ϣ
		private void getAddress() {
			bl_is_first_loc = false;

			location_client = location_application.getmLocationClient();
			location_application.setmTv(location_info_textview);
			location_application.setHandler(widge_show_change_handler);

			setLocationOption();
			location_client.requestPoi();
		}

		// ���ö�λ��ز���
		private void setLocationOption() {
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true); // ��gps
			option.setServiceName("com.baidu.location.service_v2.9");
			option.setPoiExtraInfo(true);
			option.setAddrType("all");
			option.setPriority(LocationClientOption.GpsFirst); // �����ã�Ĭ����gps����
			option.setPoiNumber(10);
			option.setScanSpan(5000); //���÷���λ����ļ��ʱ��Ϊ5000ms  
			option.disableCache(true);
			location_client.setLocOption(option);
			System.out.println("----------setLocationOption------------");
		}
	
	/**
	 * ****************************  PopWindow  ************************************************
	 */
	  /*
     * ����PopupWindow
     */ 
    private void f_init_popup_window() { 
        LayoutInflater layoutInflater = LayoutInflater.from(this); 
        View popupWindowPage = layoutInflater.inflate(R.layout.view_growth_info_pop_window, null); 
		
		// ����һ��PopupWindow 
        // ����1��contentView ָ��PopupWindow������ 
        // ����2��width ָ��PopupWindow��width 
        // ����3��height ָ��PopupWindow��height 
        int height = utils_density_transform.dip2px(this, 80);
        int width = utils_density_transform.dip2px(this, 120);
        growth_info_pop_window = new PopupWindow(popupWindowPage, width, height); 
 
        growth_info_pop_window.setOutsideTouchable(true);  
        growth_info_pop_window.setAnimationStyle(android.R.style.Animation_Dialog);  
        growth_info_pop_window.setTouchable(true);  
        growth_info_pop_window.setFocusable(true); 
        growth_info_pop_window.setBackgroundDrawable(new BitmapDrawable());//���õ��������ߴ�����ʧ
        growth_info_pop_window.update();
 
        popupWindowPage.setFocusableInTouchMode(true);  
        popupWindowPage.setOnKeyListener(new OnKeyListener() {  
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				 // TODO Auto-generated method stub  
                if ((keyCode == KeyEvent.KEYCODE_MENU)&&(growth_info_pop_window.isShowing())) { 
                	growth_info_pop_window.dismiss();// ����д��ģ��menu��PopupWindow�˳�����  
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
					Toast.makeText(ActivityViewGrowthInfo.this, "���ڴ���ing,���Ժ�...", Toast.LENGTH_LONG);
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
				Intent intent = utils_common_share.shareMethod(hm);// ��������
				ActivityViewGrowthInfo.this.startActivity(Intent.createChooser(intent, "����"));
			}
		});
        
        // ��ȡ��Ļ��PopupWindow��width��height 
        screen_width = this.getWindowManager().getDefaultDisplay().getWidth();
        System.out.println("screen_width = "+screen_width);
    } 
	
	/** * ��ʾPopupWindow���� * * @param popupwindow */
	public void f_popup_windw_showing() {
		 int xOff = screen_width - utils_density_transform.dip2px(this, 60);
		 int yOff = utils_density_transform.dip2px(this, 10);
//		growth_info_pop_window.showAtLocation(view_growth_scrollview, 
//				Gravity.RIGHT | Gravity.TOP, 0,xOff);// X��Y�����ƫ�� 
		growth_info_pop_window.showAsDropDown(view_growth_top_bar_layout, 
				xOff,
				0);// X��Y�����ƫ�� 
		
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
	 * ��ȡ��ʼ  ��������
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
	 * ɾ���������ݿ� ��¼
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
