package com.xqj.lovebabies.widgets;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.TimeUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityNoticeAddContacts;
import com.xqj.lovebabies.adapters.adapter_activity_add_growth_gridview;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_density_transform;
import com.xqj.lovebabies.databases.table_album_gridview_photo_path;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NoticeEditAddView {
	public static final int CHANGE_NOTICE_TYPE = 10001;
	public static final int OPEN_CAMERA_CODE = 10002;
	public static final int OPEN_ALBUM_CODE = 10003;
	public static final int CHANGE_GRIDVIEW_HEIGHT = 10004;
	public static final int GET_NOTICE_CONTACTS = 10005;
	
	private static final int SINGLE_PIC_HEIGHT = 50;// 图片大小
	private static final int PIC_MARGIN = 4;	// 图片间隔
	
	private Activity context;
	private LayoutInflater inflater;
	
	/**
	 * 主页面
	 */
	private View main_view;
	private EditText contact_edittext;
	private ImageView contact_imageview;
	private EditText content_edittext;
	private TextView type_textview_1;
	private TextView type_textview_2;
	private TextView type_textview_3;
	private TextView type_textview_4;
	private GridView pics_gridview;
	private Button submit_button;
	
	/**
	 * 相机相册 选择框
	 */
	private android.app.AlertDialog selectAlertDialog;
	private TextView select_photo = null;
	private TextView select_album = null;
	
	private String upload_growth_pic_path = "";
	private int pic_index = 1;	// 上传图片文件序号
	private List<String> pic_list = new ArrayList<String>();// 图片文件列表
	
	private String user_id;
	
	private adapter_activity_add_growth_gridview gridview_adapter = null;
	
	public NoticeEditAddView(Activity context){
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		
		upload_growth_pic_path = "";
		upload_growth_pic_path += PreferencesUtils.getString(context, "sys_path_sd_card");
		upload_growth_pic_path += PreferencesUtils.getString(context, "sys_path_app_folder");
		upload_growth_pic_path += PreferencesUtils.getString(context, "sys_path_cache");
		user_id = PreferencesUtils.getString(context, "user_id");
	}
	
	public View getView(){
		main_view = inflater.inflate(R.layout.activity_interaction_notice_editor_add, null);
		contact_edittext = (EditText)main_view.findViewById(R.id.interaction_notice_add_contacts_edittext);
		contact_imageview = (ImageView)main_view.findViewById(R.id.interaction_notice_add_contacts_imageview);
		content_edittext = (EditText)main_view.findViewById(R.id.interaction_notice_add_content_edittext);
		type_textview_1 = (TextView)main_view.findViewById(R.id.interaction_notice_add_type_textview_1);
		type_textview_2 = (TextView)main_view.findViewById(R.id.interaction_notice_add_type_textview_2);
		type_textview_3 = (TextView)main_view.findViewById(R.id.interaction_notice_add_type_textview_3);
		type_textview_4 = (TextView)main_view.findViewById(R.id.interaction_notice_add_type_textview_4);
		pics_gridview = (GridView)main_view.findViewById(R.id.interaction_notice_add_gridview_photos);
		submit_button = (Button)main_view.findViewById(R.id.interaction_notice_add_submit_button);
		
		gridview_adapter = new adapter_activity_add_growth_gridview(new Vector<table_album_gridview_photo_path>(), context);
		pics_gridview.setAdapter(gridview_adapter);
		table_album_gridview_photo_path photo_path = new table_album_gridview_photo_path();
		photo_path.setResource_id(R.drawable.growth_29);
		gridview_adapter.addItem(photo_path);
		pics_gridview.setOnItemClickListener(grid_view_item_clicklistener);
		
		contact_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 跳转到选择页面
				Intent intent = new Intent();
				intent.setClass(context, ActivityNoticeAddContacts.class);
				
				context.startActivityForResult(intent, GET_NOTICE_CONTACTS);
				
			}
		});
		type_textview_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				change_ui_handler.sendMessage(
						change_ui_handler.obtainMessage(CHANGE_NOTICE_TYPE, 1, 0));
			}
		});
		type_textview_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				change_ui_handler.sendMessage(
						change_ui_handler.obtainMessage(CHANGE_NOTICE_TYPE, 2, 0));
			}
		});
		type_textview_3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				change_ui_handler.sendMessage(
						change_ui_handler.obtainMessage(CHANGE_NOTICE_TYPE, 3, 0));
			}
		});
		type_textview_4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				change_ui_handler.sendMessage(
						change_ui_handler.obtainMessage(CHANGE_NOTICE_TYPE, 4, 0));
			}
		});
		submit_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 提交
				
			}
		});
		return main_view;
	}
	
	/**
	 * 修改界面UI
	 */
	private Handler change_ui_handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == CHANGE_NOTICE_TYPE){
				switch_type(msg.arg1);
			}else if(msg.what == CHANGE_GRIDVIEW_HEIGHT){
				f_set_gridview_height();
			}
		};
	};
	
	/**
	 * 切换公告类型
	 * @param index
	 */
	private void switch_type(int index){
		if(index==1){
			type_textview_1.setTextColor(context.getResources().getColor(R.color.white));
			type_textview_2.setTextColor(context.getResources().getColor(R.color.gray_textcolor));
			type_textview_3.setTextColor(context.getResources().getColor(R.color.gray_textcolor));
			type_textview_4.setTextColor(context.getResources().getColor(R.color.gray_textcolor));
			
			type_textview_1.setBackgroundResource(R.drawable.interaction_notice_type_tip_selected);
			type_textview_2.setBackgroundResource(R.drawable.interaction_notice_type_tip_normal);
			type_textview_3.setBackgroundResource(R.drawable.interaction_notice_type_tip_normal);
			type_textview_4.setBackgroundResource(R.drawable.interaction_notice_type_tip_normal);
		}else if(index==2){
			type_textview_1.setTextColor(context.getResources().getColor(R.color.gray_textcolor));
			type_textview_2.setTextColor(context.getResources().getColor(R.color.white));
			type_textview_3.setTextColor(context.getResources().getColor(R.color.gray_textcolor));
			type_textview_4.setTextColor(context.getResources().getColor(R.color.gray_textcolor));
			
			type_textview_1.setBackgroundResource(R.drawable.interaction_notice_type_tip_normal);
			type_textview_2.setBackgroundResource(R.drawable.interaction_notice_type_tip_selected);
			type_textview_3.setBackgroundResource(R.drawable.interaction_notice_type_tip_normal);
			type_textview_4.setBackgroundResource(R.drawable.interaction_notice_type_tip_normal);
		}else if(index==3){
			type_textview_1.setTextColor(context.getResources().getColor(R.color.gray_textcolor));
			type_textview_2.setTextColor(context.getResources().getColor(R.color.gray_textcolor));
			type_textview_3.setTextColor(context.getResources().getColor(R.color.white));
			type_textview_4.setTextColor(context.getResources().getColor(R.color.gray_textcolor));
			
			type_textview_1.setBackgroundResource(R.drawable.interaction_notice_type_tip_normal);
			type_textview_2.setBackgroundResource(R.drawable.interaction_notice_type_tip_normal);
			type_textview_3.setBackgroundResource(R.drawable.interaction_notice_type_tip_selected);
			type_textview_4.setBackgroundResource(R.drawable.interaction_notice_type_tip_normal);
		}else if(index==4){
			type_textview_1.setTextColor(context.getResources().getColor(R.color.gray_textcolor));
			type_textview_2.setTextColor(context.getResources().getColor(R.color.gray_textcolor));
			type_textview_3.setTextColor(context.getResources().getColor(R.color.gray_textcolor));
			type_textview_4.setTextColor(context.getResources().getColor(R.color.white));
			
			type_textview_1.setBackgroundResource(R.drawable.interaction_notice_type_tip_normal);
			type_textview_2.setBackgroundResource(R.drawable.interaction_notice_type_tip_normal);
			type_textview_3.setBackgroundResource(R.drawable.interaction_notice_type_tip_normal);
			type_textview_4.setBackgroundResource(R.drawable.interaction_notice_type_tip_selected);
		}
	}
	
	/**************************** 相机  相册 交互部分   **************************************/
	private OnItemClickListener grid_view_item_clicklistener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			if(position == (gridview_adapter.getCount() - 1)){// 最后一个Item，点击添加图片
				// 弹出对话框，选择相机或者图片
				if(pic_index<=9){
					showSelectDialog();
				}else{
					Toast.makeText(context, "最多添加9张图片", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	/**
	 * 弹出 相机或相册 的选择框
	 */
	private void showSelectDialog(){
		selectAlertDialog = new android.app.AlertDialog.Builder(context).create();
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
	 * 打开相机
	 */
	private void startCamera(){
		File picture = new File(upload_growth_pic_path + "/temp_growth.png");
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
		context.startActivityForResult(intent, OPEN_CAMERA_CODE);
	}
	
	/**
	 * 打开相册
	 */
	private void startGallery(){
		 Intent intent = new Intent(Intent.ACTION_PICK, null);  
		     intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");  
	         intent.putExtra("crop", "true");  
        // aspectX aspectY 是宽高的比例  
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        // outputX outputY 是裁剪图片宽高  
        intent.putExtra("outputX", 200);  
        intent.putExtra("outputY", 200);  

        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, OPEN_ALBUM_CODE);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == GET_NOTICE_CONTACTS){// 选择联系人
			
		}else if(resultCode==Activity.RESULT_OK){// 操作成功
			switch (requestCode) {
				case OPEN_CAMERA_CODE:// 相机处理完成
					File picture = new File(upload_growth_pic_path + "/temp_growth.png");
					startPhotoZoom(Uri.fromFile(picture), OPEN_ALBUM_CODE);
					break;
				case OPEN_ALBUM_CODE:// 相册处理完成
					Bundle extras = data.getExtras();  
		            Bitmap bitmap= (Bitmap)extras.get("data");
		            //保存照片
		            String path = upload_growth_pic_path + TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)) + "_" + user_id + ".png";
		            utils_common_tools.saveBimap(bitmap, path);
		            pic_list.add(path);
		            pic_index++;
		            /**
		             * gridview 中显示图片
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
					
					// 调整gridview 的高度
					Message msg = new Message();
					msg.what = CHANGE_GRIDVIEW_HEIGHT;
					change_ui_handler.sendMessage(msg);
					
					break;
				default:
					break;
			}
		}else{
			
		}
	}
	
	/**
	* 获取相机拍照结果并进行处理
	* @param uri
	* @param type
	*/
	public void startPhotoZoom(Uri uri, int type) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例  
	   intent.putExtra("aspectX", 1);  
	   intent.putExtra("aspectY", 1);  
	   // outputX outputY 是裁剪图片宽高  
	   intent.putExtra("outputX", 200);  
	   intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		context.startActivityForResult(intent, type);
	}
	
	/**
	 * 获取相册GridView的高度
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
	 * 设置gridview高度
	 */
	private void f_set_gridview_height(){
		int pic_num = gridview_adapter.getCount();
		ViewGroup.LayoutParams params = pics_gridview.getLayoutParams();
		int gridHeight = utils_density_transform.dip2px(context, f_get_gridview_height(pic_num));
		Log.i("gridViewHeight", "pic_num="+pic_num+"-----------------gridHeight="+gridHeight);
		params.height = gridHeight;
		pics_gridview.setLayoutParams(params);
	}

}
