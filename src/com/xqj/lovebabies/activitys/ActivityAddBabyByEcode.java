package com.xqj.lovebabies.activitys;

import java.lang.ref.WeakReference;

import cn.trinea.android.common.util.PreferencesUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityMyCenterAddMyBaby.NetWorkHandler;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.structures.interface_app_invite_baby_req;
import com.xqj.lovebabies.threads.thread_my_center_invite_baby_code;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityAddBabyByEcode extends Activity {
	
	private Button enter_ecode_dialog_negative_button = null;
	private Button enter_ecode_dialog_positive_button = null;
	private EditText add_baby_ecode_edittext = null;
	
	private NetWorkHandler network_handler = new NetWorkHandler(this);
	
	private String user_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.fragment_album_enter_ecode_dialog);
		
		user_id = PreferencesUtils.getString(this, "user_id");
		
		add_baby_ecode_edittext = (EditText)findViewById(R.id.enter_ecode_dialog_editText);
		enter_ecode_dialog_negative_button = (Button)findViewById(R.id.enter_ecode_dialog_negative_button);
		enter_ecode_dialog_positive_button = (Button)findViewById(R.id.enter_ecode_dialog_positive_button);

		enter_ecode_dialog_negative_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityAddBabyByEcode.this.setResult(ActivityMyCenterMyCareBaby.RESULT_CANCEL);
				ActivityAddBabyByEcode.this.finish();
			}
		});

		enter_ecode_dialog_positive_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				f_invite_baby();
			}
		});
		
	}
	
	/***
	 * ***********************  Õ¯¬ÁΩªª•≤ø∑÷   ************************
	 */
	static class NetWorkHandler extends Handler {
		WeakReference<ActivityAddBabyByEcode> mActivity;
		public NetWorkHandler(ActivityAddBabyByEcode activity){
			mActivity = new WeakReference<ActivityAddBabyByEcode>(activity);  
		}
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == message_what_values.activity_my_center_add_baby_success){
				Toast.makeText(mActivity.get(), "ÃÌº”≥…π¶", Toast.LENGTH_SHORT).show();
				mActivity.get().setResult(ActivityMyCenterMyCareBaby.RESULT_ADD_BABY);
				mActivity.get().finish();
			}else if(msg.what == message_what_values.fragment_my_center_get_data_failed){
				Toast.makeText(mActivity.get(), "ÃÌº” ß∞‹", Toast.LENGTH_SHORT).show();
			}
		}
	}
	//  ‰»Î—˚«Î¬ÎÃÌº”±¶±¶
	private int f_invite_baby(){
		if(user_id==null || user_id.length()==0){
			Toast.makeText(this, "Õ¯¬Á≥¨ ±£¨«Î÷ÿ–¬µ«¬º", Toast.LENGTH_SHORT).show();
			return -1;
		}else{
			String invite_code = add_baby_ecode_edittext.getText().toString();
			if(invite_code==null || invite_code.length()==0){
				Toast.makeText(this, "«Î ‰»Î—˚«Î¬Î", Toast.LENGTH_SHORT).show();
				return -1;
			}
			interface_app_invite_baby_req req = new interface_app_invite_baby_req();
			req.setUser_id(user_id);
			req.setInvitation_code(invite_code);
			new thread_my_center_invite_baby_code(network_handler, req).start();
			return 0;
		}
		
	}

}
