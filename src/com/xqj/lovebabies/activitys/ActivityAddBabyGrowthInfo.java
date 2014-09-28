package com.xqj.lovebabies.activitys;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.TimeUtils;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_activity_add_growth_gridview;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_density_transform;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.databases.table_album_gridview_photo_path;
import com.xqj.lovebabies.map.baidu_location;
import com.xqj.lovebabies.structures.interface_app_add_baby_growth_req;
import com.xqj.lovebabies.structures.interface_app_add_baby_growth_resp;
import com.xqj.lovebabies.threads.thread_album_add_baby_growth;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ViewSwitcher.ViewFactory;

public class ActivityAddBabyGrowthInfo extends Activity {
	private final int STOP_LOCATION_CLIENT = 9001;// �رյ�ͼ����
	public static final int OPEN_CAMERA_CODE = 10001;
	public static final int OPEN_ALBUM_CODE = 10002;
	private final int msg_what_location_imageswitch_change = 1001;
	private final int msg_what_add_photo_gridview_show = 1002;
	private final int msg_what_change_photo_gridview_height = 1003;
	
	private static final int SINGLE_PIC_HEIGHT = 50;// ͼƬ��С
	private static final int PIC_MARGIN = 4;	// ͼƬ���
	
	// ��ȡ����
	private Dialog dialog;
	private boolean bl_show_dialog = true;// �ܷ���ʾ����
	/**
	 * ������ ѡ���
	 */
	private android.app.AlertDialog selectAlertDialog;
	private TextView select_photo = null;
	private TextView select_album = null;
	
	/**
	 * ���罻��
	 */
	private String upload_growth_pic_path = "";
	private interface_app_add_baby_growth_req add_baby_growth_req = null;
	private interface_app_add_baby_growth_resp add_baby_growth_resp = null;
	private int pic_index = 1;	// �ϴ�ͼƬ�ļ����
	private List<String> pic_list = new ArrayList<String>();// ͼƬ�ļ��б�
	
	/**
	 * �ٶȵ�ͼӦ��
	 */
	private baidu_location location_application = null;
	private LocationClient location_client = null;
	private boolean bl_is_first_loc = true;
	
/*********************************************************************/
	private ImageView growth_info_left_imageview;
	private ImageView growth_info_right_imageview;
	private EditText add_growth_word_record_edittext;
	private EditText add_growth_height_edittext;
	private EditText add_growth_weight_edittext;
	private ImageSwitcher add_growth_location_image_switcher;
	private TextView add_growth_location_textview;
	private ImageView add_growth_location_imageview;
	private GridView fragment_album_gridview_photos;
	
	private adapter_activity_add_growth_gridview gridview_adapter = null;
	
	private boolean bl_show_location = false;
	
	private String add_growth_code = "";
	
	private String baby_id = "";
	
	private boolean bl_save_able = true;
	
	private String user_id = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_album_add_growth);
		location_application = new baidu_location(getApplication());
		
		upload_growth_pic_path = "";
		upload_growth_pic_path += PreferencesUtils.getString(this, "sys_path_sd_card");
		upload_growth_pic_path += PreferencesUtils.getString(this, "sys_path_app_folder");
		upload_growth_pic_path += PreferencesUtils.getString(this, "sys_path_cache");
//		upload_growth_pic_path = folder_paths.path_sd_card + folder_paths.path_application_root + folder_paths.temp_cache;
		user_id = PreferencesUtils.getString(this, "user_id");
		
		growth_info_left_imageview = (ImageView)findViewById(R.id.growth_info_left_imageview);
		growth_info_right_imageview = (ImageView)findViewById(R.id.growth_info_right_imageview);
		add_growth_word_record_edittext = (EditText)findViewById(R.id.add_growth_word_record_edittext);
		add_growth_height_edittext = (EditText)findViewById(R.id.add_growth_height_edittext);
		add_growth_weight_edittext = (EditText)findViewById(R.id.add_growth_weight_edittext);
		add_growth_location_image_switcher = (ImageSwitcher)findViewById(R.id.add_growth_location_image_switcher);
		add_growth_location_textview = (TextView)findViewById(R.id.add_growth_location_textview);
		add_growth_location_imageview = (ImageView)findViewById(R.id.add_growth_location_imageview);
		fragment_album_gridview_photos = (GridView)findViewById(R.id.fragment_album_gridview_photos);
		
		gridview_adapter = new adapter_activity_add_growth_gridview(new Vector<table_album_gridview_photo_path>(), this);
		
		fragment_album_gridview_photos.setAdapter(gridview_adapter);
		fragment_album_gridview_photos.setOnItemClickListener(grid_view_item_clicklistener);
		
		f_init_image_switch();// ��ʼ�� ImageSwitch
		
		growth_info_left_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityAddBabyGrowthInfo.this.finish();
			}
		});
		
		growth_info_right_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(bl_save_able){
					addBabyGrowth();
				}
				else{
					Toast.makeText(ActivityAddBabyGrowthInfo.this, "���ڱ���ing,���Ժ�...", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		showOrHideGridView();
		getAddress();// ��ʼ���ٶȵ�ͼ��λ
	}
	
	/**
	 * ��ʼ�� ImageSwitcher
	 */
	private void f_init_image_switch(){
		//��ʾ��ͼ,���ù������makeView������  
		add_growth_location_image_switcher.setFactory(new ViewFactory() {  
            @Override  
            public View makeView() {  
                // TODO Auto-generated method stub  
                return new ImageView(ActivityAddBabyGrowthInfo.this);  
            }  
        });  
		add_growth_location_image_switcher.setImageResource(R.drawable.growth_33);
		 //�������붯��   
		add_growth_location_image_switcher.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));   
        //�����г�����   
		add_growth_location_image_switcher.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right));   
		add_growth_location_image_switcher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = msg_what_location_imageswitch_change;
				catchUIChangeEventHandler.sendMessage(msg);
			}
		});
	}
	
	/**
	 * ��ʾ�����������ͼƬ����
	 */
	private void showOrHideGridView(){
		baby_id = getIntent().getStringExtra("baby_id");
		add_growth_code = getIntent().getStringExtra("add_growth_code");
//		System.out.println("add_growth_code =============== " + add_growth_code);
		if(add_growth_code.equals(String.valueOf(ActivityAddGrowthSelector.WRITE_WORD_CODE))){
			// �����дһд�����������ͼƬ���ܲ���
			fragment_album_gridview_photos.setVisibility(View.GONE);
		}else{
			fragment_album_gridview_photos.setVisibility(View.VISIBLE);
			String pic_path_1 = getIntent().getStringExtra("growth_pic_path");
			if(pic_path_1!=null && pic_path_1.length()>0){
				pic_index++;
				pic_list.add(pic_path_1);
				table_album_gridview_photo_path photo_path = new table_album_gridview_photo_path();
				photo_path.setImage_path(pic_path_1);
				gridview_adapter.addItem(photo_path);
			}
			table_album_gridview_photo_path photo_path = new table_album_gridview_photo_path();
			photo_path.setResource_id(R.drawable.growth_29);
			gridview_adapter.addItem(photo_path);
		}
	}
	
	private Handler catchUIChangeEventHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == msg_what_location_imageswitch_change){
				// �Ƿ���ʾλ�� �� �л�ͼƬ
				if(!bl_show_location){
					add_growth_location_image_switcher.setImageResource(R.drawable.growth_32);
					add_growth_location_imageview.setImageResource(R.drawable.growth_31);
					setLocationOption();
					location_client.start();
					bl_show_location = true;
				}else{
					add_growth_location_image_switcher.setImageResource(R.drawable.growth_33);
					add_growth_location_imageview.setImageResource(R.drawable.growth_30);
					location_client.stop();
					add_growth_location_textview.setText("��ʾ����λ��");
					bl_show_location = false;
				}
			}else if(msg.what == msg_what_change_photo_gridview_height){
				f_set_gridview_height();
			}
		}
	};
	
	
	private OnItemClickListener grid_view_item_clicklistener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			if(position == (gridview_adapter.getCount() - 1)){// ���һ��Item��������ͼƬ
				// �����Ի���ѡ���������ͼƬ
				if(pic_index<=9){
					showSelectDialog();
				}else{
					Toast.makeText(ActivityAddBabyGrowthInfo.this, "������9��ͼƬ", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	
	/**
	 * ��ȡ���GridView�ĸ߶�
	 * @param num
	 * @return
	 */
	private int f_get_gridview_height(int num){
		if(num>0 && num<=3){
			return SINGLE_PIC_HEIGHT + 20;
		}else if(num>3 && num<=6){
			return (SINGLE_PIC_HEIGHT*2)+PIC_MARGIN + 20;
		}else if(num>6){
			return (SINGLE_PIC_HEIGHT*3)+(PIC_MARGIN*2) + 20;
		}else{
			return 0;			
		}
	}
	
	/**
	 * ����gridview�߶�
	 */
	private void f_set_gridview_height(){
		int pic_num = gridview_adapter.getCount();
		ViewGroup.LayoutParams params = fragment_album_gridview_photos.getLayoutParams();
		int gridHeight = utils_density_transform.dip2px(this, f_get_gridview_height(pic_num));
		Log.i("gridViewHeight", "pic_num="+pic_num+"-----------------gridHeight="+gridHeight);
		params.height = gridHeight;
		fragment_album_gridview_photos.setLayoutParams(params);
	}
	
	
	/**************************** ���  ��� ��������   **************************************/
	/**
	 * ���� �������� ��ѡ���
	 */
	private void showSelectDialog(){
		selectAlertDialog = new android.app.AlertDialog.Builder(this).create();
		selectAlertDialog.show();
		Window window = selectAlertDialog.getWindow();
		window.setContentView(R.layout.album_add_growth_pic_select_dialog);
		select_photo = (TextView)window.findViewById(R.id.add_growth_select_photo_textview);		
		select_album = (TextView)window.findViewById(R.id.add_growth_select_album_textview);	
		select_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectAlertDialog.dismiss();
				startCamera();
			}
		});
		select_album.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectAlertDialog.dismiss();
				startGallery();
			}
		});
	}
	
	/**
	 * �����
	 */
	private void startCamera(){
		File picture = new File(upload_growth_pic_path + "/temp_growth.png");
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
		this.startActivityForResult(intent, OPEN_CAMERA_CODE);
	}
	
	/**
	 * �����
	 */
	private void startGallery(){
		 Intent intent = new Intent(Intent.ACTION_PICK, null);  
		     intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");  
	         intent.putExtra("crop", "true");  
        // aspectX aspectY �ǿ�ߵı���  
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        // outputX outputY �ǲü�ͼƬ���  
        intent.putExtra("outputX", 200);  
        intent.putExtra("outputY", 200);  

        intent.putExtra("return-data", true);
        this.startActivityForResult(intent, OPEN_ALBUM_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK){// �����ɹ�
			switch (requestCode) {
				case OPEN_CAMERA_CODE:// ����������
					File picture = new File(upload_growth_pic_path + "/temp_growth.png");
					startPhotoZoom(Uri.fromFile(picture), OPEN_ALBUM_CODE);
					break;
				case OPEN_ALBUM_CODE:// ��ᴦ�����
					Bundle extras = data.getExtras();  
		            Bitmap bitmap= (Bitmap)extras.get("data");
		            //������Ƭ
		            String path = upload_growth_pic_path + TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)) + "_" + user_id + ".png";
		            utils_common_tools.saveBimap(bitmap, path);
		            pic_list.add(path);
		            pic_index++;
		            /**
		             * gridview ����ʾͼƬ
		             */
		            gridview_adapter.removeAll();
		            table_album_gridview_photo_path table_photo_path = null;
					for(String temp : pic_list){
						table_photo_path = new table_album_gridview_photo_path();
			            table_photo_path.setImage_path(temp);
						gridview_adapter.addItem(table_photo_path);
					}
					table_photo_path = new table_album_gridview_photo_path();
					table_photo_path.setResource_id(R.drawable.growth_29);
					gridview_adapter.addItem(table_photo_path);
					
					// ����gridview �ĸ߶�
					Message msg = new Message();
					msg.what = msg_what_change_photo_gridview_height;
					catchUIChangeEventHandler.sendMessage(msg);
					
					break;
				default:
					//this.finish();
					break;
			}
		}else{
			
		}
	}
	
	/**
	* ��ȡ������ս�������д���
	* @param uri
	* @param type
	*/
	public void startPhotoZoom(Uri uri, int type) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���  
	   intent.putExtra("aspectX", 1);  
	   intent.putExtra("aspectY", 1);  
	   // outputX outputY �ǲü�ͼƬ���  
	   intent.putExtra("outputX", 200);  
	   intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		this.startActivityForResult(intent, type);
	}

	/*****************************  ���罻������  ****************************************/
	/**
	 * ��ӱ����ɳ���¼
	 * @return
	 */
	private int addBabyGrowth(){
		if(baby_id==null || baby_id.length()==0){
			Toast.makeText(this, "�Ҳ��������������²���", Toast.LENGTH_SHORT).show();
			return -1;
		}
		
		String word_record = add_growth_word_record_edittext.getText().toString();
		if(word_record==null || word_record.length()==0){
			Toast.makeText(this, "˵���ʲô��", Toast.LENGTH_LONG).show();
			return -1;
		}
		String heightStr = add_growth_height_edittext.getText().toString();
		String weightStr = add_growth_weight_edittext.getText().toString();
		heightStr = heightStr==null?"":heightStr;
		weightStr = weightStr==null?"":weightStr;
		String locationStr = "";
		if(bl_show_location && !add_growth_location_textview.getText().equals("��ʾ����λ��")
				 && !add_growth_location_textview.getText().equals("null")){
			locationStr = add_growth_location_textview.getText().toString();
		}
		
		add_baby_growth_req = new interface_app_add_baby_growth_req();
		add_baby_growth_req.setBaby_id(baby_id);
		add_baby_growth_req.setUser_id(user_id);
		add_baby_growth_req.setLocations(locationStr);
		add_baby_growth_req.setWord_record(word_record);
		add_baby_growth_req.setWeight(weightStr);
		add_baby_growth_req.setHeight(heightStr);
		// ���ͼƬ
		add_baby_growth_req.setGrowth_pic_list(pic_list);
		
//		int pic_count = 0;
//		for(String pic_path : pic_list){
//			pic_count++;
//			System.out.println("pic_"+pic_count+"-->"+pic_path);
//		}
//		System.out.println("----------------------��ӱ����ɳ���¼--------------------------");
		
		new thread_album_add_baby_growth(network_handler, add_baby_growth_req).start();
		bl_save_able = false;
		f_show_connect_dialog();
		return 0;
	}
	
	/**
	 * ����ӻ�������ȡ������
	 */
	private Handler network_handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == message_what_values.activity_album_request_success) {
				f_close_connect_dialog();
				Toast.makeText(ActivityAddBabyGrowthInfo.this, "����ɹ�", Toast.LENGTH_LONG).show();
				// ��ӳɹ������سɳ���¼�б���ҳ��
				ActivityAddBabyGrowthInfo.this.finish();
			}else if(msg.what == message_what_values.activity_album_get_data_timeout){
				Toast.makeText(ActivityAddBabyGrowthInfo.this, "����ʧ�ܣ������������", Toast.LENGTH_LONG).show();
				bl_save_able = true;
				f_close_connect_dialog();
			}
			else if(msg.what == STOP_LOCATION_CLIENT){// �رյ�ͼ����
				location_client.stop();
			}
		}
	};
	
	/**
	 * ***********************  ���� �ȴ� ����  **********************************
	 */
	/**
	 * ��ʾ������
	 */
	private void f_show_connect_dialog(){
		bl_show_dialog = false;
		dialog = progressDialog("���Ե�", "�����ύ������..");
		dialog.show();
	}
	/**
	 * ���������
	 */
	private Dialog progressDialog(String title, String msg) {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage(msg);
		return dialog;
	}
	/**
	 * ���ؽ�����
	 */
	private void f_close_connect_dialog(){
		if(dialog!=null){
			dialog.dismiss();
		}
		bl_show_dialog = true;
	}
	
	
	/*****************************  �ٶȵ�ͼ��λ   ************************************/
	// ��λ��ַ��Ϣ
		private void getAddress() {
			bl_is_first_loc = false;

			location_client = location_application.getmLocationClient();
			location_application.setmTv(add_growth_location_textview);
			location_application.setHandler(network_handler);

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
//			System.out.println("----------setLocationOption------------");
		}

}
