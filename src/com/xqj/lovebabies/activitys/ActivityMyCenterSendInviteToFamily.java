package com.xqj.lovebabies.activitys;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qq.QQ.ShareParams;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_get_invite_code_req;
import com.xqj.lovebabies.structures.interface_app_get_invite_code_resp;
import com.xqj.lovebabies.threads.thread_my_center_get_invite_code;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ActivityMyCenterSendInviteToFamily extends Activity {
	
	private Button enter_ecode_dialog_negative_button = null;
	private Button enter_ecode_dialog_positive_button = null;
	private LinearLayout send_weixin_layout = null;
	private LinearLayout send_qq_layout = null;
	private LinearLayout send_duanxin_layout = null;
	
	private String baby_id;
	private String relations;
	private String permission;
	
	private String invite_code;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_my_center_send_invite_to_family_dialog);
		enter_ecode_dialog_negative_button = (Button)findViewById(R.id.enter_ecode_dialog_negative_button);
		enter_ecode_dialog_positive_button = (Button)findViewById(R.id.enter_ecode_dialog_positive_button);
		send_weixin_layout = (LinearLayout)findViewById(R.id.my_center_send_invite_to_family_weixin_layout);
		send_qq_layout = (LinearLayout)findViewById(R.id.my_center_send_invite_to_family_qq_layout);
		send_duanxin_layout = (LinearLayout)findViewById(R.id.my_center_send_invite_to_family_duanxin_layout);
		
		enter_ecode_dialog_negative_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityMyCenterSendInviteToFamily.this.finish();
			}
		});

		enter_ecode_dialog_positive_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityMyCenterSendInviteToFamily.this.finish();
			}
		});
		
		send_qq_layout.setOnClickListener(new OnClickListener() {// 启动QQ
			@Override
			public void onClick(View v) {
				if(invite_code!=null && invite_code.length()>0){
					try{
//						Intent intent = new Intent();
//						ComponentName componentName = new ComponentName(
//						"com.tencent.mobileqq",
//						"com.tencent.mobileqq.activity.ForwardRecentActivity");
//						intent.setComponent(componentName);
//						intent.setType("image/*");
						//---------------
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("text/plain");
						
						intent.putExtra(Intent.EXTRA_TEXT, "您的邀请码是["+invite_code+"],赶快加入爱宝贝参观我的宝宝吧！");
						ActivityMyCenterSendInviteToFamily.this.startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(ActivityMyCenterSendInviteToFamily.this, "未安装QQ", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(ActivityMyCenterSendInviteToFamily.this, "正在获取邀请码，请稍后...", Toast.LENGTH_SHORT).show();
				}
			}
		});
		send_weixin_layout.setOnClickListener(new OnClickListener() {// 启动微信
			@Override
			public void onClick(View v) {
				if(invite_code!=null && invite_code.length()>0){
					try{
						Intent intent = new Intent();
				        ComponentName comp = new ComponentName("com.tencent.mm",
				                        "com.tencent.mm.ui.tools.ShareImgUI");
//				        ComponentName comp = new ComponentName("com.tencent.mm",
//	                            "com.tencent.mm.ui.tools.ShareToTimeLineUI");
				        intent.setComponent(comp);
				        intent.setAction("android.intent.action.SEND");
				        intent.setType("image/*");                   
				        intent.putExtra(Intent.EXTRA_TEXT, "您的邀请码是["+invite_code+"],赶快加入爱宝贝参观我的宝宝吧！");
				        
//						Intent intent = new Intent(Intent.ACTION_MAIN);
//						ComponentName componentName = new ComponentName(
//						"com.tencent.mm",
//						"com.tencent.mm.ui.LauncherUI");
//						intent.setComponent(componentName);
						ActivityMyCenterSendInviteToFamily.this.startActivity(intent);
					} catch (Exception e) {
						Toast.makeText(ActivityMyCenterSendInviteToFamily.this, "未安装微信", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(ActivityMyCenterSendInviteToFamily.this, "正在获取邀请码，请稍后...", Toast.LENGTH_SHORT).show();
				}
			}
		});
		send_duanxin_layout.setOnClickListener(new OnClickListener() {// 启动短信
			@Override
			public void onClick(View v) {
				if(invite_code!=null && invite_code.length()>0){
					try {
//						Intent intent = new Intent(Intent.ACTION_SEND);
//						intent.setType("text/plain");
//						intent.setData(Uri.parse("smsto:" + "13001659841"));
						
						Intent intent = new Intent();
				        ComponentName comp = new ComponentName("com.android.mms",
				                        "com.android.mms.ui.ComposeMessageActivity");
				        intent.setComponent(comp);
						intent.putExtra("sms_body", "您的邀请码是["+invite_code+"],赶快加入爱宝贝参观我的宝宝吧！");
						ActivityMyCenterSendInviteToFamily.this.startActivity(intent);
					} catch (Exception e) {
						Toast.makeText(ActivityMyCenterSendInviteToFamily.this, "短信箱未找到", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(ActivityMyCenterSendInviteToFamily.this, "正在获取邀请码，请稍后...", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		baby_id = getIntent().getStringExtra("baby_id");
		relations = getIntent().getStringExtra("relation");
		permission = getIntent().getStringExtra("permissions");
		f_get_invite_code();
	}

	
	/***
	 * ***********************  网络交互部分   ************************
	 */

	private Handler network_handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_get_invite_code_success){
				interface_app_get_invite_code_resp resp = (interface_app_get_invite_code_resp)msg.obj;
				invite_code = resp.getInviteCode();
				System.out.println("resp.getInviteCode()-->"+resp.getInviteCode());
				if(invite_code==null || invite_code.length()==0){
					Toast.makeText(ActivityMyCenterSendInviteToFamily.this, "操作失败，请重试", Toast.LENGTH_SHORT).show();
					ActivityMyCenterSendInviteToFamily.this.finish();
				}else{
					Toast.makeText(ActivityMyCenterSendInviteToFamily.this, "邀请码："+invite_code, Toast.LENGTH_SHORT).show();
				}
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(ActivityMyCenterSendInviteToFamily.this, "操作失败，请重试", Toast.LENGTH_SHORT).show();
				ActivityMyCenterSendInviteToFamily.this.finish();
			}
		}
	};
	
	/**
	 * 生成邀请码
	 */
	private void f_get_invite_code(){
		interface_app_get_invite_code_req req = new interface_app_get_invite_code_req();
		req.setBaby_id(baby_id);
		req.setPermissions(permission);
		req.setRelation(relations);
		new thread_my_center_get_invite_code(network_handler, req).start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
}
