package com.xqj.lovebabies.activitys;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.TimeUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityMyCenterAddMyBaby.NetWorkHandler;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.table_album_my_baby;
import com.xqj.lovebabies.structures.interface_app_add_and_set_my_baby_req;
import com.xqj.lovebabies.threads.thread_my_center_update_my_baby;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMyCenterModifyMyBaby extends Activity {
	public static final int OPEN_CAMERA_CODE = 10001;
	public static final int OPEN_ALBUM_CODE = 10002;
	// topbar 组件
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	// 主界面部分
	private LinearLayout add_baby_relation_layout;
	private EditText add_baby_nick_edittext;
	private EditText add_baby_birthday_edittext;
//	private EditText add_baby_relation_edittext;
	private ImageView add_baby_pic_imageview;
	private Button add_baby_girl_btn;
	private Button add_baby_boy_btn;
	private int baby_sex = 1;//宝贝性别：1  男    2 女
	
	// PopupWindow对象
	private PopupWindow pop_window = null;
	private Button pop_window_cancel_btn = null;// 取消
	private Button pop_window_photo_btn = null;// 照相
	private Button pop_window_album_btn = null;// 相册
	
	private String upload_baby_pic_path;
	private String local_baby_icon_path = "";
	private Bitmap local_baby_icon_bmp;
	
	private String user_id;
	private String baby_id;
	private table_album_my_baby my_baby;
	
	private NetWorkHandler network_handler = new NetWorkHandler(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_add_my_baby);
		
		Bundle bundle = getIntent().getExtras();
		my_baby = (table_album_my_baby)bundle.get("my_baby");
		user_id = PreferencesUtils.getString(this, "user_id");
		System.out.println("ModifyBabyUser_id-->"+user_id);
		
		upload_baby_pic_path = "";
		upload_baby_pic_path += PreferencesUtils.getString(this, "sys_path_sd_card");
		upload_baby_pic_path += PreferencesUtils.getString(this, "sys_path_app_folder");
		upload_baby_pic_path += PreferencesUtils.getString(this, "sys_path_cache");
		
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
		
		head_title.setText("修改宝宝");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityMyCenterModifyMyBaby.this.setResult(ActivityMyCenterMyBaby.RESULT_CANCEL);
				ActivityMyCenterModifyMyBaby.this.finish();
			}
		});
		head_btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				f_modify_my_baby();
			}
		});
	}
	
	/**
	 * 初始化主界面
	 */
	private void init_main_page(){
		add_baby_nick_edittext = (EditText)findViewById(R.id.my_center_add_baby_nick_edittext);
		add_baby_birthday_edittext = (EditText)findViewById(R.id.my_center_add_baby_birthday_edittext);
//		add_baby_relation_edittext = (EditText)findViewById(R.id.my_center_add_baby_relation_edittext);
		add_baby_pic_imageview = (ImageView)findViewById(R.id.my_center_add_baby_pic_imageview);
		add_baby_girl_btn = (Button)findViewById(R.id.my_center_add_baby_sex_girl_btn);
		add_baby_boy_btn = (Button)findViewById(R.id.my_center_add_baby_sex_boy_btn);
		add_baby_relation_layout = (LinearLayout)findViewById(R.id.my_center_add_baby_relation_layout);
		add_baby_relation_layout.setVisibility(View.GONE);
		
		if(my_baby!=null){
			baby_id = my_baby.getBaby_id();
			System.out.println("modify-baby-baby_id-->"+baby_id);
			add_baby_nick_edittext.setText(my_baby.getBaby_name());
			add_baby_birthday_edittext.setText(my_baby.getBirthday());
			if(my_baby.getBaby_sex().equals("2")){// 女孩
				add_baby_girl_btn.setBackgroundResource(R.drawable.my_center_add_baby_sex_selected);
				add_baby_boy_btn.setBackgroundResource(R.drawable.my_center_add_baby_sex_normal);
			}else{// 男孩
				add_baby_girl_btn.setBackgroundResource(R.drawable.my_center_add_baby_sex_normal);
				add_baby_boy_btn.setBackgroundResource(R.drawable.my_center_add_baby_sex_selected);
			}
		}
		
		add_baby_girl_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				baby_sex = 2;
				add_baby_girl_btn.setBackgroundResource(R.drawable.my_center_add_baby_sex_selected);
				add_baby_boy_btn.setBackgroundResource(R.drawable.my_center_add_baby_sex_normal);
			}
		});
		add_baby_boy_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				baby_sex = 1;
				add_baby_girl_btn.setBackgroundResource(R.drawable.my_center_add_baby_sex_normal);
				add_baby_boy_btn.setBackgroundResource(R.drawable.my_center_add_baby_sex_selected);
			}
		});
		add_baby_pic_imageview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(pop_window.isShowing()){
					pop_window.dismiss();
				}else{
					f_popup_windw_showing();
				}
			}
		});
		
	 // 装载网络图片
	if(my_baby.getBaby_pic()!=null && my_baby.getBaby_pic().length()>0){
			String imgurl = network_interface_paths.get_project_root + my_baby.getBaby_pic();
			System.out.println("MOdifyBabyGetBabyPic-->"+imgurl);
//			utils_picture_caches.getInstance().init(ActivityMyCenterModifyMyBaby.this);// 初始化图片缓存
//			DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//			builder.showImageOnLoading(R.drawable.my_center_add_baby_logo);
//			builder.showImageForEmptyUri(R.drawable.my_center_add_baby_logo);
//			builder.cacheInMemory(false);
//			builder.cacheOnDisk(true);
//			builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//			DisplayImageOptions options = builder.build();
//			ImageLoader.getInstance().displayImage(imgurl, add_baby_pic_imageview, options);
			utils_common_tools.f_display_Image(ActivityMyCenterModifyMyBaby.this, 
					add_baby_pic_imageview, imgurl,R.drawable.my_center_add_baby_logo,
					R.drawable.my_center_add_baby_logo, ImageScaleType.IN_SAMPLE_INT);
		}else{
			add_baby_pic_imageview.setImageResource(R.drawable.my_center_add_baby_logo);
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
		File picture = new File(upload_baby_pic_path + "/temp_baby_pic.png");
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
					File picture = new File(upload_baby_pic_path + "/temp_growth.png");
					startPhotoZoom(Uri.fromFile(picture), OPEN_ALBUM_CODE);
					break;
				case OPEN_ALBUM_CODE:// 相册处理完成
					Bundle extras = data.getExtras();  
					local_baby_icon_bmp= (Bitmap)extras.get("data");
		            //保存照片
					String local_icon_file_name = TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)) + "_" + user_id + ".png";
		            local_baby_icon_path = upload_baby_pic_path + local_icon_file_name;
		            utils_common_tools.saveBimap(local_baby_icon_bmp, local_baby_icon_path);
		            add_baby_pic_imageview.setImageBitmap(local_baby_icon_bmp);
					break;
				default:
					this.finish();
					break;
			}
		}
	}
	
	/***
	 * ***********************  网络交互部分   ************************
	 */
	static class NetWorkHandler extends Handler {
		WeakReference<ActivityMyCenterModifyMyBaby> mActivity;
		public NetWorkHandler(ActivityMyCenterModifyMyBaby activity){
			mActivity = new WeakReference<ActivityMyCenterModifyMyBaby>(activity);  
		}
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_update_baby_success){
				Toast.makeText(mActivity.get(), "修改宝宝成功", Toast.LENGTH_SHORT).show();
				mActivity.get().setResult(ActivityMyCenterMyBaby.RESULT_MODIFY_BABY_INFO);
				mActivity.get().finish();
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(mActivity.get(), "修改宝宝失败", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * 修改宝宝信息
	 * @param baby_id
	 */
	private int f_modify_my_baby(){
		if(baby_id!=null && baby_id.length()>0
				&& user_id!=null && user_id.length()>0){
			String baby_name = add_baby_nick_edittext.getText().toString();
			String birthday = add_baby_birthday_edittext.getText().toString();
//			String relation = add_baby_relation_edittext.getText().toString();
			if(baby_name==null || baby_name.length()==0){
				Toast.makeText(ActivityMyCenterModifyMyBaby.this, "请输入宝宝名称", Toast.LENGTH_SHORT).show();
				return -1;
			}
			interface_app_add_and_set_my_baby_req req = new interface_app_add_and_set_my_baby_req();
			req.setUser_id(user_id);
			req.setBaby_id(baby_id);
			req.setBaby_name(baby_name);
			req.setBirthday(birthday);
//			req.setRelation(relation);
			req.setSex(String.valueOf(baby_sex));
			req.setUpload_file(local_baby_icon_path);
			new thread_my_center_update_my_baby(network_handler, req).start();
		}else{
			Toast.makeText(ActivityMyCenterModifyMyBaby.this, "用户/宝宝ID为空", Toast.LENGTH_SHORT).show();
			return -1;
		}
		return 0;
	}
}
