package com.xqj.lovebabies.fragments;

import java.util.HashMap;

import cn.trinea.android.common.util.PreferencesUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityMain;
import com.xqj.lovebabies.activitys.ActivityMyCenterFavorite;
import com.xqj.lovebabies.activitys.ActivityMyCenterMyBaby;
import com.xqj.lovebabies.activitys.ActivityMyCenterMyCareBaby;
import com.xqj.lovebabies.activitys.ActivityMyCenterPoints;
import com.xqj.lovebabies.activitys.ActivityMyCenterSetting;
import com.xqj.lovebabies.activitys.ActivityPersonalInfo;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.structures.interface_app_get_user_detail_req;
import com.xqj.lovebabies.structures.interface_app_get_user_detail_resp;
import com.xqj.lovebabies.threads.thread_my_center_get_user_detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMyCenter  extends Fragment{
	public static final int RESULT_CANCEL = -900;
	public static final int RESULT_EDIT_PERSONLA_INFO = 2001;
	// UI ���
	private View main_view;
	private RelativeLayout my_center_self_info_layout;
	private RelativeLayout my_center_my_baby_layout;
	private RelativeLayout my_center_my_care_baby_layout;
	private RelativeLayout my_center_points_layout;
	private RelativeLayout my_center_favorite_layout;
	private RelativeLayout my_center_setting_layout;
	private ImageView head_icon_imageview;
	private TextView user_name_textview;
	private TextView account_textview;
	
	// ���罻��
	private String user_id;
	private interface_app_get_user_detail_req req = null;
	private interface_app_get_user_detail_resp resp;
	
	private HashMap<String, Object> map = null;
	private OnHeadIconModifiedListener onHeadIconModifiedListener = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try{
			main_view = inflater.inflate(R.layout.fragment_my_center, container, false);
			
			init_action_bar();// ��ʼ��ͷ��actionBar��
			init_main_page();// ��ʼ��������
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		user_id = PreferencesUtils.getString(getActivity(), "user_id");
		get_user_detail();
		return main_view;
	}
	
	/**
	 * ��ʼ��ͷ��actionBar��
	 */
	private void init_action_bar() {
		try {
			
			ActivityMain.main_action_bar.getCmd_textview_title_name().setText("�ҵ�");
			
			ActivityMain.main_action_bar.getCmd_imagebutton_more().setVisibility(View.INVISIBLE);
			
			ActivityMain.main_action_bar.getCmd_imageview_title_icon().setVisibility(View.INVISIBLE);
			
			ActivityMain.main_action_bar.getCmd_imageview_title_action().setVisibility(View.INVISIBLE);
		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * ��ʼ��������
	 */
	private void init_main_page(){
		my_center_self_info_layout = (RelativeLayout)main_view.findViewById(R.id.my_center_self_info_layout);
		my_center_my_baby_layout = (RelativeLayout)main_view.findViewById(R.id.my_center_my_baby_layout);
		my_center_my_care_baby_layout = (RelativeLayout)main_view.findViewById(R.id.my_center_my_care_baby_layout);
		my_center_points_layout = (RelativeLayout)main_view.findViewById(R.id.my_center_my_points_layout);
		my_center_favorite_layout = (RelativeLayout)main_view.findViewById(R.id.my_center_my_favorite_layout);
		my_center_setting_layout = (RelativeLayout)main_view.findViewById(R.id.my_center_setting_layout);
		//������Ϣ
		my_center_self_info_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityPersonalInfo.class);
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("user_detail_resp", resp);
//				intent.putExtras(bundle);
				startActivityForResult(intent, RESULT_EDIT_PERSONLA_INFO);
			}
		});
		//�ҵı���
		my_center_my_baby_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityMyCenterMyBaby.class);
				getActivity().startActivity(intent);
			}
		});
		//�ҹ�ע�ı���
		my_center_my_care_baby_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityMyCenterMyCareBaby.class);
				getActivity().startActivity(intent);
			}
		});
		//�ҵĻ���
		my_center_points_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityMyCenterPoints.class);
				getActivity().startActivity(intent);
			}
		});
		//�ҵ��ղ�
		my_center_favorite_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityMyCenterFavorite.class);
				getActivity().startActivity(intent);
			}
		});
		//����
		my_center_setting_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ActivityMyCenterSetting.class);
				getActivity().startActivity(intent);
			}
		});
		
		head_icon_imageview = (ImageView)main_view.findViewById(R.id.my_center_head_icon_imageview);
		user_name_textview = (TextView)main_view.findViewById(R.id.my_center_user_name_textview);
		account_textview = (TextView)main_view.findViewById(R.id.my_center_user_account_textview);
	}
	
	/**
	 * ***************************  ���罻������  ******************************
	 */
	private Handler network_handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.fragment_my_center_get_user_detail_success){// ��ѯ����ɹ�
				resp = (interface_app_get_user_detail_resp)msg.obj;
				if(resp!=null){
					f_set_user_info_to_cache(resp);
					user_name_textview.setText(resp.getUser_nike_name());
					account_textview.setText("�ʺţ�"+resp.getUser_phone());
					// װ������ͼƬ  ͷ��
					if(resp.getUser_icon()!=null && resp.getUser_icon().length()>0){
						String imgurl = network_interface_paths.get_project_root + resp.getUser_icon();
						PreferencesUtils.putString(getActivity(), "user_icon", resp.getUser_icon());
						if(onHeadIconModifiedListener!=null){
							onHeadIconModifiedListener.notifyDataChanged();
						}
						System.out.println("getUser_icon-->"+imgurl);
//						DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//						builder.showImageOnLoading(R.drawable.default_head_icon);
//						builder.showImageForEmptyUri(R.drawable.default_head_icon);
//						builder.cacheInMemory(false);
//						builder.cacheOnDisk(true);
//						builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
////						builder.displayer(new RoundedBitmapDisplayer(20));
//						DisplayImageOptions options = builder.build();
//						ImageLoader.getInstance().displayImage(imgurl, head_icon_imageview, options);
						utils_common_tools.f_display_Image(getActivity(), 
								head_icon_imageview, imgurl,R.drawable.default_head_icon,
								R.drawable.default_head_icon, ImageScaleType.IN_SAMPLE_INT);
					}else{
						head_icon_imageview.setImageResource(R.drawable.default_head_icon);
					}
				}
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){// ��ѯʧ��
				Toast.makeText(getActivity(), "��ѯʧ��", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	
	// �û���Ϣд�뻺��
	private void f_set_user_info_to_cache(interface_app_get_user_detail_resp resp){
		PreferencesUtils.putString(getActivity(), "user_id", resp.getUser_id());
		PreferencesUtils.putString(getActivity(), "user_icon", resp.getUser_icon());
//		PreferencesUtils.putString(getActivity(), "user_password", resp.getUser_password());
		PreferencesUtils.putString(getActivity(), "user_name", resp.getUser_phone());
		PreferencesUtils.putString(getActivity(), "user_nick_name", resp.getUser_nike_name());
		PreferencesUtils.putString(getActivity(), "user_phone", resp.getUser_phone());
		PreferencesUtils.putString(getActivity(), "user_email", resp.getUser_email());
		System.out.println("-----------------�û���Ϣд�뻺��-----------------");
		System.out.println("user_id-->"+resp.getUser_id());
		System.out.println("user_icon-->"+resp.getUser_icon());
//		System.out.println("user_password-->"+resp.getUser_password());
		System.out.println("user_name-->"+resp.getUser_phone());
		System.out.println("user_nick_name-->"+resp.getUser_nike_name());
		System.out.println("user_phone-->"+resp.getUser_phone());
		System.out.println("user_email-->"+resp.getUser_email());
	}
	
	/**
	 * ��ȡ�û�����
	 */
	private void get_user_detail(){
		if(utils_common_tools.get_network_status(getActivity())){
			req = new interface_app_get_user_detail_req();
			req.setUser_id(user_id);
			new thread_my_center_get_user_detail(getActivity(), network_handler, req).start();
		}else{// ������
			String user_nick_name = PreferencesUtils.getString(getActivity(), "user_nick_name");
			String user_phone = PreferencesUtils.getString(getActivity(), "user_phone");
			String head_icon = PreferencesUtils.getString(getActivity(), "user_icon");
			user_name_textview.setText(user_nick_name);
			account_textview.setText("�ʺţ�"+user_phone);
			// װ������ͼƬ  ͷ��
			if(head_icon!=null && head_icon.length()>0){
				String imgurl = network_interface_paths.get_project_root + head_icon;
				if(onHeadIconModifiedListener!=null){
					onHeadIconModifiedListener.notifyDataChanged();
				}
				System.out.println("getUser_icon-->"+imgurl);
//				DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//				builder.showImageOnLoading(R.drawable.default_head_icon);
//				builder.showImageForEmptyUri(R.drawable.default_head_icon);
//				builder.cacheInMemory(false);
//				builder.cacheOnDisk(true);
//				builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
////				builder.displayer(new RoundedBitmapDisplayer(20));
//				DisplayImageOptions options = builder.build();
//				ImageLoader.getInstance().displayImage(imgurl, head_icon_imageview, options);
				utils_common_tools.f_display_Image(getActivity(), 
						head_icon_imageview, imgurl,R.drawable.default_head_icon,
						R.drawable.default_head_icon, ImageScaleType.IN_SAMPLE_INT);
			}else{
				head_icon_imageview.setImageResource(R.drawable.default_head_icon);
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("requestCode-->"+requestCode);
		System.out.println("resultCode-->"+resultCode);
		if(resultCode==RESULT_CANCEL){// �û��޸��²���
		}else if(resultCode==RESULT_EDIT_PERSONLA_INFO){
			// �û���Ϣͷ�����޸�
			if(data !=null)  
	        {  
	            byte [] bis=data.getByteArrayExtra("bitmap");  
	            Bitmap bitmap=BitmapFactory.decodeByteArray(bis, 0, bis.length);  
	            head_icon_imageview.setImageBitmap(bitmap);  
	            System.out.println("updateUserIcon(bitmap)");
	            onHeadIconModifiedListener.updateUserIcon(bitmap);
	            if(map!=null){
	            	map.put("user_icon", bitmap);
	            }
	        }  
		}
	}

	public void setOnHeadIconModifiedListener(OnHeadIconModifiedListener lsn) {
		this.onHeadIconModifiedListener = lsn;
	}
	
	public interface OnHeadIconModifiedListener{
		void updateUserIcon(Bitmap bitmap);
		void notifyDataChanged();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		account_textview.setText("�ʺţ�"+PreferencesUtils.getString(getActivity(), "user_name"));
		super.onResume();
	}
	
}
