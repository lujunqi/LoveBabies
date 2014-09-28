package com.xqj.lovebabies.activitys;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.TimeUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_fragment_album_add_growth_action_listview;
import com.xqj.lovebabies.commons.utils_common_tools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ActivityAddGrowthSelector extends Activity {
	public static final int OPEN_CAMERA_CODE = 10001;
	public static final int OPEN_ALBUM_CODE = 10002;
	public static final int WRITE_WORD_CODE = 10003;
	
	private String upload_growth_pic_path = "";
	
	private adapter_fragment_album_add_growth_action_listview adapter = null;
	private ListView listView = null;
	private LinearLayout blank_space_layout = null;
	
	private String baby_id;
	
	private String user_id = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.fragment_album_add_growth_action);
		
		upload_growth_pic_path = "";
		upload_growth_pic_path += PreferencesUtils.getString(this, "sys_path_sd_card");
		upload_growth_pic_path += PreferencesUtils.getString(this, "sys_path_app_folder");
		upload_growth_pic_path += PreferencesUtils.getString(this, "sys_path_cache");
		user_id = PreferencesUtils.getString(this, "user_id");
		
		listView = (ListView)this.findViewById(R.id.add_growth_action_listview);
		adapter = new adapter_fragment_album_add_growth_action_listview(this);
		initActionList();
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(itemClickListener);
		
		blank_space_layout = (LinearLayout)findViewById(R.id.fragment_blank_space);
		blank_space_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityAddGrowthSelector.this.finish();
			}
		});
		
		baby_id = getIntent().getStringExtra("baby_id");
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			switch (position) {
			case 0:
//				System.out.println("相机");
				startCamera();// 打开相机
				break;
				
			case 1:
//				System.out.println("写一写");
				Intent intent = new Intent();
				intent.setClass(ActivityAddGrowthSelector.this, ActivityAddBabyGrowthInfo.class);
				intent.putExtra("add_growth_code", String.valueOf(WRITE_WORD_CODE));
				intent.putExtra("baby_id", baby_id);
				startActivity(intent);
				ActivityAddGrowthSelector.this.finish();
				break;

			case 2:
//				System.out.println("上传照片");
				startGallery();// 打开相册
				break;

//			case 3:
//				System.out.println("生长测评");
//				ActivityAddGrowthSelector.this.finish();
//				break;
				
			default:
				break;
			}
		}
	};
	
	
	
	/**
	 * 初始化数据
	 */
	private void initActionList(){
		adapter.addItem(R.drawable.album_add_growth_selection_camera);// 相机
		adapter.addItem(R.drawable.album_add_growth_selection_write);// 写一些
		adapter.addItem(R.drawable.album_add_growth_selection_photo);// 上传照片
//		adapter.addItem(R.drawable.album_add_growth_selection_detect);// 身高体重
	}
	
	/**
	 * 打开相机
	 */
	private void startCamera(){
		File picture = new File(upload_growth_pic_path + "/temp_growth.png");
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
		this.startActivityForResult(intent, OPEN_CAMERA_CODE);
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
        this.startActivityForResult(intent, OPEN_ALBUM_CODE);
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
		this.startActivityForResult(intent, type);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK){// 操作成功
			switch (requestCode) {
				case OPEN_CAMERA_CODE:// 相机处理完成
					// 跳转到添加成长记录页面，结束此页面
//					Intent intent = new Intent();
//					intent.setClass(ActivityAddGrowthSelector.this, ActivityAddBabyGrowthInfo.class);
//					intent.putExtra("add_growth_code", String.valueOf(OPEN_CAMERA_CODE));
//					intent.putExtra("growth_pic_path", upload_growth_pic_path + "/temp_growth.png");
//					startActivity(intent);
//					this.finish();
					File picture = new File(upload_growth_pic_path + "/temp_growth.png");
					startPhotoZoom(Uri.fromFile(picture), OPEN_ALBUM_CODE);
					break;
				case OPEN_ALBUM_CODE:// 相册处理完成
					Bundle extras = data.getExtras();  
		            Bitmap bitmap= (Bitmap)extras.get("data");
		            //保存照片
		            String path = upload_growth_pic_path + TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)) + "_" + user_id + ".png";
		            utils_common_tools.saveBimap(bitmap, path);
		            // 跳转到添加成长记录页面，结束此页面
		            this.finish();
		            Intent intent2 = new Intent();
		            intent2.setClass(ActivityAddGrowthSelector.this, ActivityAddBabyGrowthInfo.class);
		            
					intent2.putExtra("baby_id", baby_id);
					intent2.putExtra("add_growth_code", String.valueOf(OPEN_ALBUM_CODE));
					intent2.putExtra("growth_pic_path", path);
		            startActivity(intent2);
					break;
				default:
					this.finish();
					break;
			}
		}else{
			this.finish();
		}
	}
}
