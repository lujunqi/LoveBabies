package com.xqj.lovebabies.widgets;

import com.xqj.lovebabies.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;

public class AlbumAddBabyAlerDialog {
	public static final int POSITIVE_KEY = 1;
	public static final int NEGATIVE_KEY = -1;
	private Context context;
	private Handler handler;
	private android.app.AlertDialog ad;
	private Button positive_button = null;
	private Button negtive_button = null;
	private ImageView close_button = null;
	
	
	public AlbumAddBabyAlerDialog(Context context, final Handler handler) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.handler = handler;
	}
	
	/**
	 * 显示对话框
	 */
	public void show(){
		ad=new android.app.AlertDialog.Builder(context).create();
		ad.show();
		Window window = ad.getWindow();
		window.setContentView(R.layout.fragment_album_add_baby_dialog);
		positive_button = (Button)window.findViewById(R.id.album_remind_box_add_baby);
		negtive_button = (Button)window.findViewById(R.id.album_remind_box_enter_ecode);
		close_button = (ImageView)window.findViewById(R.id.album_add_baby_close);
		positive_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = POSITIVE_KEY;
				handler.sendMessage(message);
				dismiss();
			}
		});
		negtive_button.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Message message = new Message();
						message.what = NEGATIVE_KEY;
						handler.sendMessage(message);
						dismiss();
					}
				});
		close_button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}
	
	
	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		ad.dismiss();
	}
	
}
