package com.xqj.lovebabies.activitys;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xqj.lovebabies.R;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.structures.interface_app_add_and_set_my_baby_req;
import com.xqj.lovebabies.threads.thread_my_center_update_my_baby;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMyCenterInviteFamily extends Activity {
	// topbar ���
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;
	
	// ������
	private LinearLayout invite_family_layout;
	private LinearLayout invite_friends_layout;
	private ImageView baby_pic_imageview;
	private EditText relation_edittext;
	private TextView remark_textview;
	
	private String baby_id;
	private String baby_pic;
	private String baby_name;
	private String relation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center_invite_family);
		
		baby_id = getIntent().getStringExtra("baby_id");
		baby_pic = getIntent().getStringExtra("baby_pic");
		baby_name = getIntent().getStringExtra("baby_name");
		relation = getIntent().getStringExtra("relation");
		relation = relation==null?"":relation+",";
		
		System.out.println("ActivityMyCenterInviteFamily:baby_id-->"+baby_id);
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
		head_title.setText("����");
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityMyCenterInviteFamily.this.finish();
			}
		});
	}
	
	/**
	 * ��ʼ��������
	 */
	private void init_main_page(){
		invite_family_layout = (LinearLayout)findViewById(R.id.my_center_invite_family_layout);
		invite_friends_layout = (LinearLayout)findViewById(R.id.my_center_invite_friends_layout);
		baby_pic_imageview = (ImageView)findViewById(R.id.invite_family_baby_pic_imageview);
		relation_edittext = (EditText)findViewById(R.id.my_center_add_baby_relation_edittext);
		remark_textview = (TextView)findViewById(R.id.my_center_invite_family_remark);
		remark_textview.setText(relation+baby_name+"1������"+baby_name
				+"�����׷�˿��Ҳ��Ϊ������¼�أ�����������ǰ�!");
		
		invite_family_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				f_invite_baby_relations(2);
			}
		});
		invite_friends_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				f_invite_baby_relations(3);
			}
		});
		// װ������ͼƬ
		if(baby_pic!=null && baby_pic.length()>0){
				String imgurl = network_interface_paths.get_project_root + baby_pic;
				System.out.println("getBabyPic-->"+imgurl);
//				utils_picture_caches.getInstance().init(ActivityMyCenterInviteFamily.this);// ��ʼ��ͼƬ����
//				DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//				builder.showImageOnLoading(R.drawable.ic_launcher);
//				builder.showImageForEmptyUri(R.drawable.ic_launcher);
//				builder.cacheInMemory(false);
//				builder.cacheOnDisk(true);
//				builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
//				DisplayImageOptions options = builder.build();
//				ImageLoader.getInstance().displayImage(imgurl, baby_pic_imageview, options);
				utils_common_tools.f_display_Image(ActivityMyCenterInviteFamily.this, 
						baby_pic_imageview, imgurl,R.drawable.ic_launcher,
						R.drawable.ic_launcher, ImageScaleType.IN_SAMPLE_INT);
			}else{
				baby_pic_imageview.setImageResource(R.drawable.ic_launcher);;
			}
	}
	
	
	/**
	 * ���뱦��������Ϣ
	 * @param baby_id
	 */
	private int f_invite_baby_relations(int permission){
		if(baby_id==null || baby_id.length()==0){
			Toast.makeText(ActivityMyCenterInviteFamily.this, "baby_id Ϊ��", Toast.LENGTH_SHORT).show();
			return -1;
		}
		String relation = relation_edittext.getText().toString();
		if(relation==null || relation.length()==0){
			Toast.makeText(ActivityMyCenterInviteFamily.this, "��ѡ���뱦���Ĺ�ϵ", Toast.LENGTH_SHORT).show();
			return -1;
		}
		Intent intent = new Intent();
		intent.setClass(ActivityMyCenterInviteFamily.this, ActivityMyCenterSendInviteToFamily.class);
		intent.putExtra("baby_id", baby_id);
		intent.putExtra("relation", relation);
		intent.putExtra("permissions", String.valueOf(permission));
		ActivityMyCenterInviteFamily.this.startActivity(intent);
		return 0;
	}
}
