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
//				System.out.println("���");
				startCamera();// �����
				break;
				
			case 1:
//				System.out.println("дһд");
				Intent intent = new Intent();
				intent.setClass(ActivityAddGrowthSelector.this, ActivityAddBabyGrowthInfo.class);
				intent.putExtra("add_growth_code", String.valueOf(WRITE_WORD_CODE));
				intent.putExtra("baby_id", baby_id);
				startActivity(intent);
				ActivityAddGrowthSelector.this.finish();
				break;

			case 2:
//				System.out.println("�ϴ���Ƭ");
				startGallery();// �����
				break;

//			case 3:
//				System.out.println("��������");
//				ActivityAddGrowthSelector.this.finish();
//				break;
				
			default:
				break;
			}
		}
	};
	
	
	
	/**
	 * ��ʼ������
	 */
	private void initActionList(){
		adapter.addItem(R.drawable.album_add_growth_selection_camera);// ���
		adapter.addItem(R.drawable.album_add_growth_selection_write);// дһЩ
		adapter.addItem(R.drawable.album_add_growth_selection_photo);// �ϴ���Ƭ
//		adapter.addItem(R.drawable.album_add_growth_selection_detect);// �������
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
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK){// �����ɹ�
			switch (requestCode) {
				case OPEN_CAMERA_CODE:// ����������
					// ��ת����ӳɳ���¼ҳ�棬������ҳ��
//					Intent intent = new Intent();
//					intent.setClass(ActivityAddGrowthSelector.this, ActivityAddBabyGrowthInfo.class);
//					intent.putExtra("add_growth_code", String.valueOf(OPEN_CAMERA_CODE));
//					intent.putExtra("growth_pic_path", upload_growth_pic_path + "/temp_growth.png");
//					startActivity(intent);
//					this.finish();
					File picture = new File(upload_growth_pic_path + "/temp_growth.png");
					startPhotoZoom(Uri.fromFile(picture), OPEN_ALBUM_CODE);
					break;
				case OPEN_ALBUM_CODE:// ��ᴦ�����
					Bundle extras = data.getExtras();  
		            Bitmap bitmap= (Bitmap)extras.get("data");
		            //������Ƭ
		            String path = upload_growth_pic_path + TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)) + "_" + user_id + ".png";
		            utils_common_tools.saveBimap(bitmap, path);
		            // ��ת����ӳɳ���¼ҳ�棬������ҳ��
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
