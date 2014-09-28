package com.xqj.lovebabies.activitys;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.TimeUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_fragment_album_popwindow;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_density_transform;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_album_gridview_photo_path;
import com.xqj.lovebabies.fragments.FragmentMyCenter;
import com.xqj.lovebabies.structures.interface_app_get_user_detail_resp;
import com.xqj.lovebabies.structures.interface_app_upload_user_head_icon_req;
import com.xqj.lovebabies.threads.thread_my_center_update_user_head_icon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 用户信息
 * @author Administrator
 */
public class ActivityPersonalInfo extends Activity {
	public static final int OPEN_CAMERA_CODE = 10001;
	public static final int OPEN_ALBUM_CODE = 10002;
	public static final int RESULT_CHANGE_NICK_NAME = 10003;
	public static final int RESULT_CHANGE_PASSWORD = 10004;
	public static final int RESULT_CHANGE_EMAIL = 10005;
	public static final int RESULT_CHANGE_PHONE = 10006;
	public static final int RESULT_CANCEL = -100;
	public static final int RESULT_FINISH = 100;
	// PopupWindow对象
	private PopupWindow pop_window = null;
	private Button pop_window_cancel_btn = null;// 取消
	private Button pop_window_photo_btn = null;// 照相
	private Button pop_window_album_btn = null;// 相册
	
	// topBar
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	// 主要组件
	private RelativeLayout head_info_layout;
	private RelativeLayout user_nick_layout;
	private RelativeLayout account_layout;
	private RelativeLayout password_layout;
	private RelativeLayout email_layout;
	
	private ImageView head_icon_imageview;
	private TextView user_nick_textview;
	private TextView account_textview;
	private TextView password_textview;
	private TextView email_textview;
	
	// 字段
	private String upload_growth_pic_path;
	private String user_id;
	private String user_nick;
	private String user_pwd;
	private String user_email;
	private String user_name;
	private String user_pic;
	private String local_user_icon_path = "";
	private Bitmap local_user_icon_bmp;
	private boolean bl_update_user_head_icon = false;
//	private interface_app_get_user_detail_resp resp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_personal_info);
		
//		Bundle budnle = getIntent().getExtras();
//		resp = (interface_app_get_user_detail_resp)budnle.getSerializable("user_detail_resp");
		
		upload_growth_pic_path = "";
		upload_growth_pic_path += PreferencesUtils.getString(this, "sys_path_sd_card");
		upload_growth_pic_path += PreferencesUtils.getString(this, "sys_path_app_folder");
		upload_growth_pic_path += PreferencesUtils.getString(this, "sys_path_cache");
		user_id = PreferencesUtils.getString(this, "user_id");
		user_nick = PreferencesUtils.getString(this, "user_nick_name");
		user_pwd = PreferencesUtils.getString(this, "user_password");
		user_email = PreferencesUtils.getString(this, "user_email");
		user_name = PreferencesUtils.getString(this, "user_name");
		user_pic = PreferencesUtils.getString(this, "user_icon");
		
		init_top_bar();
		init_main_page();
		f_init_popup_window();
		
	}
	
	/**
	 * 初始化头部
	 */
	private void init_top_bar(){
		head_btn_right = (ImageView)findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView)findViewById(R.id.head_left_imageview);
		head_title = (TextView)findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		
		head_title.setText("用户信息");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(bl_update_user_head_icon && local_user_icon_bmp!=null){
					Intent intent = new Intent();
					ByteArrayOutputStream baos=new ByteArrayOutputStream();  
					local_user_icon_bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);  
					byte [] bitmapByte =baos.toByteArray();  
					intent.putExtra("bitmap", bitmapByte);  
					setResult(FragmentMyCenter.RESULT_EDIT_PERSONLA_INFO, intent);
					System.out.println("setResult(FragmentMyCenter.RESULT_EDIT_PERSONLA_INFO, intent);");
				}else{
					setResult(FragmentMyCenter.RESULT_CANCEL);
					System.out.println("setResult(FragmentMyCenter.RESULT_CANCEL);");
				}
				
				ActivityPersonalInfo.this.finish();
			}
		});
	}
	
	/**
	 * 初始化主页面
	 */
	private void init_main_page(){
		head_info_layout = (RelativeLayout)findViewById(R.id.my_center_personal_info_head_layout);
		user_nick_layout = (RelativeLayout)findViewById(R.id.my_center_user_nick_layout);
		account_layout = (RelativeLayout)findViewById(R.id.my_center_account_layout);
		password_layout = (RelativeLayout)findViewById(R.id.my_center_password_layout);
		email_layout = (RelativeLayout)findViewById(R.id.my_center_email_layout);
		
		//头像
		head_info_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(pop_window.isShowing()){
					pop_window.dismiss();
				}else{
					f_popup_windw_showing();
				}
			}
		});
		//昵称
		user_nick_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityPersonalInfo.this, ActivityMyCenterChangeNickname.class);
				intent.putExtra("user_id", user_id);
				intent.putExtra("user_nick", user_nick);
				ActivityPersonalInfo.this.startActivityForResult(intent, RESULT_CHANGE_NICK_NAME);
			}
		});
		//账户
		account_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityPersonalInfo.this, ActivityMyCenterMyAccount.class);
				ActivityPersonalInfo.this.startActivityForResult(intent, RESULT_CHANGE_PHONE);
			}
		});
		//密码
		password_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityPersonalInfo.this, ActivityMyCenterChangePassword.class);
				intent.putExtra("user_id", user_id);
				intent.putExtra("user_pwd", user_pwd);
				ActivityPersonalInfo.this.startActivityForResult(intent, RESULT_CHANGE_PASSWORD);
			}
		});
		//邮箱
		email_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ActivityPersonalInfo.this, ActivityMyCenterChangeEmail.class);
				intent.putExtra("user_id", user_id);
				intent.putExtra("user_email", user_email);
				ActivityPersonalInfo.this.startActivityForResult(intent, RESULT_CHANGE_EMAIL);
			}
		});
		
		head_icon_imageview = (ImageView)findViewById(R.id.my_center_head_icon_imageview);
		user_nick_textview = (TextView)findViewById(R.id.my_center_user_nick_textview);
		account_textview = (TextView)findViewById(R.id.my_center_my_account_textview);
		password_textview = (TextView)findViewById(R.id.my_center_my_password_textview);
		email_textview = (TextView)findViewById(R.id.my_center_my_email_textview);
		

		user_nick_textview.setText(user_nick);
		account_textview.setText(user_name);
		email_textview.setText(user_email);
		// 装载网络图片  头像
		if(user_pic!=null && user_pic.length()>0){
			String imgurl = network_interface_paths.get_project_root + user_pic;
			System.out.println("getUser_icon-->"+imgurl);
//			utils_picture_caches.getInstance().init(ActivityPersonalInfo.this);// 初始化图片缓存
//			DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//			builder.showImageOnLoading(R.drawable.default_head_icon);
//			builder.showImageForEmptyUri(R.drawable.default_head_icon);
//			builder.cacheInMemory(false);
//			builder.cacheOnDisk(true);
//			builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//			DisplayImageOptions options = builder.build();
//			ImageLoader.getInstance().displayImage(imgurl, head_icon_imageview, options);
			utils_common_tools.f_display_Image(ActivityPersonalInfo.this, 
					head_icon_imageview, imgurl,R.drawable.default_head_icon,
					R.drawable.default_head_icon, ImageScaleType.IN_SAMPLE_INT);
		}else{
			head_icon_imageview.setImageResource(R.drawable.default_head_icon);
		}
	}
	
	
	/**
	 * 初始化popWindow
	 */
	 /*
     * 创建PopupWindow
     */ 
    private void f_init_popup_window() { 
        LayoutInflater layoutInflater = LayoutInflater.from(this); 
        View popupWindowPage = layoutInflater.inflate(R.layout.activity_my_center_personal_info_pop_window, null); 
        pop_window_cancel_btn = (Button)popupWindowPage.findViewById(R.id.pop_window_cancel_btn);
        pop_window_photo_btn = (Button)popupWindowPage.findViewById(R.id.pop_window_photo_btn);
        pop_window_album_btn = (Button)popupWindowPage.findViewById(R.id.pop_window_album_btn);
        pop_window_cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop_window.dismiss();
			}
		});
        pop_window_photo_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开相机
				pop_window.dismiss();
				startCamera();
			}
		});
        pop_window_album_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开相册
				pop_window.dismiss();
				startGallery();
			}
		});
        
        int screen_width = this.getWindowManager().getDefaultDisplay().getWidth();
        int screen_height = this.getWindowManager().getDefaultDisplay().getHeight();
        System.out.println("screen_width = "+screen_width);
        pop_window = new PopupWindow(popupWindowPage, screen_width, screen_height); 
 
        pop_window.setOutsideTouchable(true);  
        pop_window.setAnimationStyle(android.R.style.Animation_Dialog);  
        pop_window.setTouchable(true);  
        pop_window.setFocusable(true); 
        pop_window.setBackgroundDrawable(new BitmapDrawable());
        pop_window.update();  
 
        popupWindowPage.setFocusableInTouchMode(true);  
        popupWindowPage.setOnKeyListener(new OnKeyListener() {  
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_MENU)&&(pop_window.isShowing())) {  
                	pop_window.dismiss();// 这里写明模拟menu的PopupWindow退出就行  
                    return true;  
                }  
                return false;
			}  
        }); 
    } 
	
	/** * 显示PopupWindow窗口 * * @param popupwindow */
	public void f_popup_windw_showing() {
		View main_view = this.findViewById(android.R.id.content).getRootView();
		pop_window.showAtLocation(main_view, Gravity.BOTTOM, 0, 0); 
	}
	
	/**
	 * 相机与相册
	 */
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK){// 操作成功
			switch (requestCode) {
				case OPEN_CAMERA_CODE:// 相机处理完成
					File picture = new File(upload_growth_pic_path + "/temp_growth.png");
					startPhotoZoom(Uri.fromFile(picture), OPEN_ALBUM_CODE);
					break;
				case OPEN_ALBUM_CODE:// 相册处理完成
					Bundle extras = data.getExtras();  
					local_user_icon_bmp= (Bitmap)extras.get("data");
		            //保存照片
					String local_icon_file_name = TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)) + "_" + user_id + ".png";
		            local_user_icon_path = upload_growth_pic_path + local_icon_file_name;
		            utils_common_tools.saveBimap(local_user_icon_bmp, local_user_icon_path);
		            // 上传头像
		            f_upload_head_icon(local_user_icon_path);
		            
					break;
				default:
					this.finish();
					break;
			}
		}else if(resultCode==RESULT_CANCEL){
			// 用户取消操作
		}else if(resultCode==RESULT_CHANGE_NICK_NAME){
			Uri uriData = data.getData();
			user_nick_textview.setText(uriData.toString());
		}else if(resultCode==RESULT_CHANGE_PASSWORD){
		}else if(resultCode==RESULT_CHANGE_EMAIL){
			Uri uriData = data.getData();
			email_textview.setText(uriData.toString());
		}else if(resultCode == RESULT_CHANGE_PHONE){
			Uri uriData = data.getData();
			account_textview.setText(uriData.toString());
		}
	}
	
	//-----------------网络交互部分-------------------------
	private Handler network_handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_update_user_head_icon_success){
				if(local_user_icon_bmp!=null){
					head_icon_imageview.setImageBitmap(local_user_icon_bmp);
					bl_update_user_head_icon = true;
					System.out.println("bl_update_user_head_icon = true;");
				}
			}else if(msg.what == message_what_values.activity_my_center_update_user_detail_success){
				
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(ActivityPersonalInfo.this, "操作失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	/**
	 * 更新用户头像
	 * @param image_path
	 */
	private void f_upload_head_icon(String image_path){
		System.out.println("-------------f_upload_head_icon---------------");
		interface_app_upload_user_head_icon_req req = new interface_app_upload_user_head_icon_req();
		req.setUser_id(user_id);
		req.setImage_path(image_path);
		new thread_my_center_update_user_head_icon(this, network_handler, req).start();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		account_textview.setText(PreferencesUtils.getString(this, "user_name"));
		super.onResume();
	}
	
}
