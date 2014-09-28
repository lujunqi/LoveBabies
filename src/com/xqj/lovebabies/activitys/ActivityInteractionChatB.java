package com.xqj.lovebabies.activitys;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.StringUtils;
import cn.trinea.android.common.util.TimeUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_activity_chat_gridview_image_inside;
import com.xqj.lovebabies.adapters.adapter_activity_chat_listview_b;
import com.xqj.lovebabies.broadcasts.activity_chat_message_receiver;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.contants.global_contants;
import com.xqj.lovebabies.contants.message_what_values;
import com.xqj.lovebabies.contants.network_interface_paths;
import com.xqj.lovebabies.databases.dbs_interaction_contacts;
import com.xqj.lovebabies.databases.dbs_interaction_message;
import com.xqj.lovebabies.databases.table_interaction_contacts;
import com.xqj.lovebabies.databases.table_interaction_message;
import com.xqj.lovebabies.listeners.*;
import com.xqj.lovebabies.structures.interface_app_upload_file_req;
import com.xqj.lovebabies.threads.thread_interaction_update_voice_amp_indicator;
import com.xqj.lovebabies.threads.thread_interaction_xmpp_send_text_message;
import com.xqj.lovebabies.threads.thread_upload_media_files;
import com.xqj.lovebabies.widgets.UIListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityInteractionChatB extends Activity {
	private utils_common_tools tools = new utils_common_tools();
	private final int SHOW_MEDIA_BTNS = 10001;
	private final int SHOW_FACES_BTNS = 10002;
	private final int HIDE_FACES_MEDIA = 10003;
	private final int SHOW_RECORD_BTN = 10004;
	private final int HIDE_RECORD_BTN = 10005;
	
	// 头部UI
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;

	// 输入文本内容或者录制语音并发送 部分UI
	private ImageView record_imageview;
	private ImageView show_faces_imageview;
	private Button send_message_btn;
	private EditText content_edittext;
	private RelativeLayout content_layout;
	private Button record_voice_btn;

	// 选择 表情、相机、图片 部分UI
	private LinearLayout show_media_btns_layout;
	private LinearLayout get_face_layout;
	private LinearLayout get_photo_layout;
	private LinearLayout get_album_layout;

	// 选择表情图像 UI
	private GridView chat_faces_gridview;

	private ImageView record_progress_imageview;// 录音进度动画
	private RelativeLayout record_progress_layout;// 录音进度layout
	// 会话
	private UIListView chat_listview;

	private adapter_activity_chat_gridview_image_inside adapter_faces_gridview;

	// 录音
	private boolean bl_long_pressed = false;// 是否长按录音按钮
	private MediaRecorder mRecorder = null;	// 录音
	private MediaPlayer mPlayer = null;		// 播放器
	private Date beginRecordDate = null;// 开始录音日期
	private Date stopRecordDate = null; // 结束录音日期
	private int[] record_arm_pic_list = new int[] { R.drawable.amp2,
			R.drawable.amp3, R.drawable.amp4, R.drawable.amp5, R.drawable.amp6,
			R.drawable.amp7, R.drawable.amp1 };
	private int record_arm_count = 0;
	private int time_delay = 500;
	private String volName;
	private String voice_record_temp_file_name = null;
	private String upload_file_path = null;
	private String picture_temp_file_name = null;
	private thread_interaction_update_voice_amp_indicator t_voice_amp_indicator_update = null;
	
	// 消息处理
	private table_interaction_message t_interaction_message = null;
	private dbs_interaction_message db_interaction_message = null;
	private dbs_interaction_contacts db_interaction_contacts = null;

	private table_interaction_contacts contacts_receiver = null;
	private table_interaction_contacts contacts_sender = null;

	private activity_chat_message_receiver broadcast_interaction_message_receiver = null;

	private int history_message_page = 0;
	private List<table_interaction_message> temp_list_messages = null;
	private adapter_activity_chat_listview_b list_messages_adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_interaction_chat_b);

		try {
			upload_file_path = "";
			upload_file_path += PreferencesUtils.getString(this,
					global_contants.SYS_PATH_SD_CARD);
			upload_file_path += PreferencesUtils.getString(this,
					global_contants.SYS_PATH_APP_FOLDER);
			upload_file_path += PreferencesUtils.getString(this,
					global_contants.SYS_PATH_UP);
			upload_file_path += File.separator;
			
			// 获取聊天好友ID，消息的receiver
			Intent intent = getIntent();
			contacts_receiver = new table_interaction_contacts();
			Bundle bundle = intent.getBundleExtra("chat_receiver");
			contacts_receiver = (table_interaction_contacts) bundle
					.getSerializable("chat_receiver");
			// 获取自己的ID，消息的sender
			contacts_sender = new table_interaction_contacts();
			contacts_sender.setUser_id(PreferencesUtils.getString(this,
					"user_id"));
			contacts_sender.setUser_real_name(PreferencesUtils.getString(this,
					"user_real_name"));
			contacts_sender.setUser_icon_path(PreferencesUtils.getString(this,
					"user_icon"));
			// 初始化
			init_top_bar();
			init_bottom_bar();
			init_dialogue();

			// 查询缓存消息
			history_message_page = 1;
			f_action_get_history_message();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 初始化头部
	 */
	private void init_top_bar() {
		head_btn_right = (ImageView) findViewById(R.id.head_right_imageview);
		head_btn_left = (ImageView) findViewById(R.id.head_left_imageview);
		head_title = (TextView) findViewById(R.id.head_title_textview);
		head_btn_right.setVisibility(View.INVISIBLE);
		head_title.setText(contacts_receiver.getUser_real_name());
		head_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(ActivityPersonalInfo.RESULT_CANCEL);
				ActivityInteractionChatB.this.finish();
			}
		});
	}

	/**
	 * 初始化底部发送、录音、选择表情栏
	 */
	private void init_bottom_bar() {
		// 发送信息栏
		record_imageview = (ImageView) findViewById(R.id.chat_record_imageview);
		show_faces_imageview = (ImageView) findViewById(R.id.chat_show_faces_imageview);
		send_message_btn = (Button) findViewById(R.id.chat_send_message_btn);
		content_edittext = (EditText) findViewById(R.id.chat_mesage_content_edittext);
		content_layout = (RelativeLayout) findViewById(R.id.chat_send_message_content_layout);
		record_voice_btn = (Button) findViewById(R.id.chat_get_record_voice_btn);
		record_voice_btn.setVisibility(View.GONE);
		show_faces_imageview.setOnClickListener(new OnClickListener() {// 显示多媒体框
					@Override
					public void onClick(View arg0) {
						change_ui_handler.sendMessage(change_ui_handler
								.obtainMessage(SHOW_MEDIA_BTNS));
					}
				});
		content_edittext.setOnClickListener(new OnClickListener() {// 输入框点击
					@Override
					public void onClick(View arg0) {
						change_ui_handler.sendMessage(change_ui_handler
								.obtainMessage(HIDE_FACES_MEDIA));
					}
				});
		send_message_btn.setOnClickListener(new OnClickListener() {// 发送按钮点击
					@Override
					public void onClick(View arg0) {
						// 发送内容，清空文本框
						f_action_send_text_message();
					}
				});
		record_imageview.setOnClickListener(new OnClickListener() {// 隐藏\显示录音按钮
					@Override
					public void onClick(View arg0) {
						if (record_voice_btn.getVisibility() == View.VISIBLE) {// 隐藏录音按钮
							change_ui_handler.sendMessage(change_ui_handler
									.obtainMessage(HIDE_RECORD_BTN));
						} else {// 显示录音按钮
								// 隐藏下方多媒体选择框
							change_ui_handler.sendMessage(change_ui_handler
									.obtainMessage(HIDE_FACES_MEDIA));
							change_ui_handler.sendMessage(change_ui_handler
									.obtainMessage(SHOW_RECORD_BTN));
						}
					}
				});
		// record_voice_btn.setOnLongClickListener(record_btn_longclick_listener);
		// record_voice_btn.setOnTouchListener(record_btn_ontouch_listener);
		record_voice_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("onClick ...");
				Log.v("TEST", "onClick ...");
			}
		});
		record_voice_btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				System.out.println("onTouch ...");
				Log.v("TEST", "onTouch ...");
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下
					record_voice_btn
							.setBackgroundResource(R.drawable.chart_radio_get_record_pressed);
					
					show_record_progress_dialog();//显示录音进度
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// 松开
					record_voice_btn
							.setBackgroundResource(R.drawable.chart_radio_get_record_normal);
					dismiss_record_progress_dialog();// 隐藏录音进度
					if (bl_long_pressed) {
						// 停止录音，上传录音
						f_action_record_voice_end();
						long voice_seconds = tools.get_second_differ(
								beginRecordDate, stopRecordDate);
						if (voice_seconds < 1) {// 1 second
							show_toast("录音时间太短");
							// 如果有录音文件，删除录音文件
						} else if (voice_seconds > 5 * 60) {// 5 minute
							show_toast("录音时间太长");
						} else {
							show_toast("录音完成");
							// 发送录音
							f_action_upload_voice();
						}
						bl_long_pressed = false;
					} else {
						show_toast("未开始录音");
					}
				}
				return false;
			}
		});
		record_voice_btn.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("onLongClick...");
				Log.v("TEST", "onLongClick ...");
				System.out.println("onLongClick...");
				bl_long_pressed = true;
				try {
					// 开始录音
					f_action_record_voice_begin();
				} catch (Exception ex) {

				}
				return false;
			}
		});

		// 选择表情、相机、图片 栏
		show_media_btns_layout = (LinearLayout) findViewById(R.id.chat_send_media_btns_layout);
		get_face_layout = (LinearLayout) findViewById(R.id.chat_get_faces_layout);
		get_photo_layout = (LinearLayout) findViewById(R.id.chat_get_photo_layout);
		get_album_layout = (LinearLayout) findViewById(R.id.chat_get_album_layout);
		show_media_btns_layout.setVisibility(View.GONE);
		get_face_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				change_ui_handler.sendMessage(change_ui_handler
						.obtainMessage(SHOW_FACES_BTNS));
				// 隐藏录音按钮
				change_ui_handler.sendMessage(change_ui_handler
						.obtainMessage(HIDE_RECORD_BTN));
			}
		});
		get_photo_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				f_action_send_image_from_camera();
			}
		});
		get_album_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				f_action_send_image_from_local();
			}
		});

		// 表情栏
		chat_faces_gridview = (GridView) findViewById(R.id.chat_faces_gridview);
		chat_faces_gridview.setVisibility(View.GONE);
		adapter_faces_gridview = new adapter_activity_chat_gridview_image_inside(
				this);
		chat_faces_gridview.setAdapter(adapter_faces_gridview);
		System.out.println("global_contants_faces-->"
				+ global_contants.faceImgId.length + "||"
				+ global_contants.faceImgName.length);
		chat_faces_gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				if (index < global_contants.faceImgName.length) {
					String face_name = global_contants.faceImgName[index];
					String edittext_string = content_edittext.getText()
							.toString();
					String final_string = edittext_string + face_name;
					content_edittext.setText(final_string);
					content_edittext.setSelection(final_string.length());
				}
			}
		});
		
		record_progress_imageview = (ImageView)findViewById(R.id.recording_progress_imageview);
		record_progress_layout = (RelativeLayout)findViewById(R.id.recording_progress_layout);
		record_progress_layout.setVisibility(View.GONE);
	}

	/**
	 * 初始化对话
	 */
	private void init_dialogue() {
		chat_listview = (UIListView) findViewById(R.id.activity_chat_listview_message);
		list_messages_adapter = new adapter_activity_chat_listview_b(this, 
				handleSetReaded, handlePlayAudio, contacts_receiver, contacts_sender);
		chat_listview.setAdapter(list_messages_adapter);
		chat_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

			}
		});
	}

	private Handler change_ui_handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == SHOW_MEDIA_BTNS) {// 显示\隐藏选择图片、相机、图片的按钮
				if (show_media_btns_layout.getVisibility() == View.VISIBLE
						|| chat_faces_gridview.getVisibility() == View.VISIBLE) {
					show_media_btns_layout.setVisibility(View.GONE);
					chat_faces_gridview.setVisibility(View.GONE);
					show_faces_imageview
							.setImageResource(R.drawable.chart_faces_show);
				} else {
					show_media_btns_layout.setVisibility(View.VISIBLE);
					show_faces_imageview
							.setImageResource(R.drawable.chart_faces_hide);
					hiddenInputMethod();
				}
			} else if (msg.what == SHOW_FACES_BTNS) {// 显示\隐藏表情选择栏
				if (chat_faces_gridview.getVisibility() == View.VISIBLE) {
					chat_faces_gridview.setVisibility(View.GONE);
				} else {
					chat_faces_gridview.setVisibility(View.VISIBLE);
					show_media_btns_layout.setVisibility(View.GONE);
				}
			} else if (msg.what == HIDE_FACES_MEDIA) {// 隐藏下方多媒体选择框
				chat_faces_gridview.setVisibility(View.GONE);
				show_media_btns_layout.setVisibility(View.GONE);
				show_faces_imageview
						.setImageResource(R.drawable.chart_faces_show);
			} else if (msg.what == SHOW_RECORD_BTN) {// 显示录音按钮
				content_layout.setVisibility(View.GONE);// 隐藏文本输入框
				hiddenInputMethod();// 隐藏键盘
				record_voice_btn.setVisibility(View.VISIBLE);// 显示录音按钮
				record_imageview
						.setImageResource(R.drawable.selector_interaction_chat_keyboard_button);
			} else if (msg.what == HIDE_RECORD_BTN) {// 隐藏录音按钮
				content_layout.setVisibility(View.VISIBLE);// 显示文本输入框
				record_voice_btn.setVisibility(View.GONE);// 隐藏录音按钮
				record_imageview
						.setImageResource(R.drawable.selector_interaction_chat_record_button);
			}
		};
	};

	// 显示录音进度
	private void show_record_progress_dialog() {
		// 开始动画
		record_progress_layout.setVisibility(View.VISIBLE);
		record_progress_imageview.setImageResource(R.drawable.amp1);
		progress_handler.postDelayed(progress_runnable, time_delay);
	}

	// 隐藏录音进度
	private void dismiss_record_progress_dialog() {
		record_arm_count = 0;
		record_progress_layout.setVisibility(View.GONE);
	}

	// 动态显示进度
	Handler progress_handler = new Handler();
	Runnable progress_runnable = new Runnable() {
		@Override
		public void run() {
			progress_handler.postDelayed(this, time_delay);
			if (record_progress_imageview != null) {
				int index = record_arm_count % record_arm_pic_list.length;
				record_progress_imageview
						.setImageResource(record_arm_pic_list[index]);
				record_arm_count++;
			}
		}
	};

	// //检查录音路径
	// private boolean check_voice_dir(){
	// boolean sdCardExist = Environment.getExternalStorageState().equals(
	// android.os.Environment.MEDIA_MOUNTED);
	// if(sdCardExist){
	// String path = Environment.getExternalStorageDirectory().getPath()
	// + "/loveBaby/vol";
	// File pathFile = new File(path);
	// if (!pathFile.exists()) {
	// // 按照指定的路径创建文件夹
	// pathFile.mkdirs();
	// }
	// return true;
	// }else{
	// return false;
	// }
	// }

	/**
	 * 隐藏输入法
	 */
	private void hiddenInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(content_edittext.getWindowToken(), 0);
	}

	/**
	 * 消息处理
	 */
	// 加载历史聊天记录
	private void f_action_get_history_message() {
		String sql = "";
		try {
			int history_message_pagesize = 8;
			history_message_page = history_message_page <= 0 ? 1
					: history_message_page;
			db_interaction_message = new dbs_interaction_message(this);
			sql = "select * from t_interaction_message where 1=1 ";
			sql += "and (message_sender='" + contacts_receiver.getUser_id()
					+ "' or message_receiver='"
					+ contacts_receiver.getUser_id() + "') ";
			sql += "order by message_occurrence_time desc ";
			sql += "limit "
					+ ((history_message_page - 1) * history_message_pagesize)
					+ "," + history_message_pagesize;
			temp_list_messages = db_interaction_message.do_select_data(sql);
			// ---
			temp_list_messages = temp_list_messages == null ? new ArrayList<table_interaction_message>()
					: temp_list_messages;
			if (temp_list_messages.size() > 0) {
				for (int i = 0; i < temp_list_messages.size(); i++) {
					t_interaction_message = temp_list_messages.get(i);
					list_messages_adapter.addFirst(t_interaction_message);
				}
				list_messages_adapter.notifyDataSetChanged();
				history_message_page += 1;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 接收消息并显示在聊天会话中
	private void f_action_receive_message_success(Message message) {
		try {
			t_interaction_message = (table_interaction_message) message.obj;
			// 更新聊天界面,如果发消息的是正在一起聊天的用户就 实时更新界面
			if (StringUtils.isEquals(t_interaction_message.getMessage_sender(),
					contacts_receiver.getUser_id())) {
				list_messages_adapter.addItem(t_interaction_message);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 动态注册消息接收广播
	private void register_message_broadcast_receiver() {
		try {
			IntentFilter filter = new IntentFilter();
			broadcast_interaction_message_receiver = new activity_chat_message_receiver(
					network_handler);
			filter.addAction(utils_xmpp_client_message.ACTION_RECEIVE_INTERACTION_MESSAGE);
			registerReceiver(broadcast_interaction_message_receiver, filter);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 动态注销消息接收广播
	private void unregister_message_broadcast_receiver() {
		try {
			unregisterReceiver(broadcast_interaction_message_receiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 文本消息
	 */
	// 发送文本消息
	private void f_action_send_text_message() {
		String msg = null;
		try {
			// 生成消息对象
			System.out.println("发送文本消息,生成消息对象");
			System.out.println(contacts_sender.getUser_id() + "发送给"
					+ contacts_receiver.getUser_id());
			msg = content_edittext.getText().toString();
			msg = msg == null ? "" : msg.trim();
			t_interaction_message = new table_interaction_message();
			t_interaction_message.setMessage_receiver(contacts_receiver
					.getUser_id());
			t_interaction_message.setMessage_content(msg);
			t_interaction_message.setMessage_content_length(String.valueOf(msg
					.length()));
			t_interaction_message
					.setMessage_direction_type(table_interaction_message.message_direction_type_mt);
			t_interaction_message
					.setMessage_media_type(table_interaction_message.message_media_type_text);
			t_interaction_message.setMessage_occurrence_time(TimeUtils
					.getCurrentTimeInString(new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss")));
			t_interaction_message.setMessage_sender(contacts_sender
					.getUser_id());
			// 清空edittext
			content_edittext.setText("");
			// 发送消息
			if (!StringUtils.isBlank(msg)) {
				new thread_interaction_xmpp_send_text_message(
						t_interaction_message, network_handler).start();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 发送文本消息成功
	private void f_action_send_text_message_success() {
		try {
			// 保存message到数据库
			db_interaction_message = new dbs_interaction_message(this);
			t_interaction_message.setMessage_read_status("1");//已读
			db_interaction_message.do_insert_data(t_interaction_message);
			// 更新最新消息到会话
			db_interaction_contacts = new dbs_interaction_contacts(this);
			String sql = "update t_interaction_contacts set user_last_session_content='"
					+ t_interaction_message.getMessage_content() + "',";
			sql += "user_last_session_time='"
					+ TimeUtils.getCurrentTimeInString(new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss")) + "' ";
			sql += "where user_id='"
					+ t_interaction_message.getMessage_receiver() + "'";
			System.out.println("---------更新最新消息到会话----------");
			System.out.println(sql);
			db_interaction_contacts.do_execute_sql(sql);

			// 更新聊天界面
			list_messages_adapter.addItem(t_interaction_message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 更新消息为已读
	 */
	private boolean update_message_readed(table_interaction_message message){
		try {
			// 保存message到数据库
			db_interaction_message = new dbs_interaction_message(this);
			String sql = "update t_interaction_message set message_read_status=1 where message_sender="+message.getMessage_sender();
			System.out.println("update_sql-->"+sql);
			db_interaction_message.do_execute_sql(sql);
			//修改页面
			message.setMessage_read_status("1");//改成已读
			list_messages_adapter.notifyDataSetChanged();
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	

	/**
	 * 录音、照相、图像控制
	 */
	// 录音开始处理
	private void f_action_record_voice_begin() {
		try {
			System.out.println("开始录音...");
			if(mRecorder!=null){
				f_action_record_voice_end();
			}
			// --录音
			voice_record_temp_file_name = "a_"+TimeUtils
					.getCurrentTimeInString(new SimpleDateFormat(
							"yyyyMMddHHmmss", Locale.CHINA))
					+ "_" + contacts_sender.getUser_id() + ".amr";
			// --
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			mRecorder.setOutputFile(upload_file_path+voice_record_temp_file_name);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			try {
				mRecorder.prepare();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("Start Recording", "prepare() failed");
			}
			mRecorder.start();
			beginRecordDate = new Date();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 录音完成处理
	private void f_action_record_voice_end() {
		try {
			System.out.println("停止录音...");
			if(mRecorder!=null){
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
			}
			stopRecordDate = new Date();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 录音文件发送处理
	private void f_action_upload_voice() {
		try {
			interface_app_upload_file_req req = new interface_app_upload_file_req();
			req.setUpload_file_path(upload_file_path);
			req.setUpload_file_name(voice_record_temp_file_name);
			req.setUpload_user_id(contacts_sender.getUser_id());
			req.setUpload_file_type("voice");
			thread_upload_media_files upload = new thread_upload_media_files(
					network_handler, req);
			upload.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 录音文件上传成功,开始发送对应的文本信息到openfire
	private void f_action_upload_voice_success() {
		try {
			String msg = null;
			msg = "[rec]";
			String voice_time_leng = tools
					.get_voice_length(upload_file_path+voice_record_temp_file_name);
			msg += "[" + voice_time_leng + "]";
			
			msg += voice_record_temp_file_name;
			// --
			t_interaction_message = new table_interaction_message();
			t_interaction_message.setMessage_receiver(contacts_receiver
					.getUser_id());
			t_interaction_message.setMessage_content(msg);
			t_interaction_message.setMessage_content_length(voice_time_leng);
			t_interaction_message
					.setMessage_direction_type(table_interaction_message.message_direction_type_mt);
			t_interaction_message
					.setMessage_media_type(table_interaction_message.message_media_type_voice);
			t_interaction_message.setMessage_occurrence_time(TimeUtils
					.getCurrentTimeInString(new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss")));
			t_interaction_message.setMessage_sender(contacts_sender
					.getUser_id());
			// 删除本地录音文件
			try {
//				File file = new File(voice_record_temp_file_path);
//				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 发送消息
			if (!StringUtils.isBlank(msg)){
				new thread_interaction_xmpp_send_text_message(
						t_interaction_message, network_handler).start();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 播放音频的Handler
	 */
	private Handler handlePlayAudio = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			System.out.println("handlePlayAudio["+msg.arg1+"]["+msg.obj.toString()+"]");
			if(msg.arg1==1){//录音文件加载成功
				//设置成已读
				String fileName = (String)msg.obj;
				stopPlaying();				// 先停止上一个播放器
				startPlaying(fileName);		// 重新开始一个播放器
			}else{
				
			}
		}
	};
	
	/**
	 * 设置成已读
	 */
	private Handler handleSetReaded = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			System.out.println("handleSetReaded["+msg.arg1+"]["+msg.obj.toString()+"]");
			table_interaction_message message = (table_interaction_message)msg.obj;
			update_message_readed(message);
		}
	};

	
	/**
	 * 开始播放
	 * @param mFileName
	 */
	private void startPlaying(String mFileName) {
		System.out.println("Start Playing File Name ...-----"+mFileName);
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e("Play Record", "prepare() failed");
		}
	}

	/**
	 * 停止播放
	 */
	private void stopPlaying() {
		if(mPlayer!=null){
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	// 图片文件上传成功,开始发送对应的文本信息到openfire
	private void f_action_upload_image_success() {
		try {
			String msg = null;
			msg = "[pic]";
			String remote_uri = network_interface_paths.get_temp_file + "/"
					+ contacts_sender.getUser_id() + "/"
					+ picture_temp_file_name;
			msg += remote_uri;
			System.out.println("f_action_upload_image_success["+msg+"]");
			// --
			t_interaction_message = new table_interaction_message();
			t_interaction_message.setMessage_receiver(contacts_receiver
					.getUser_id());
			t_interaction_message.setMessage_content(msg);
			t_interaction_message
					.setMessage_direction_type(table_interaction_message.message_direction_type_mt);
			t_interaction_message
					.setMessage_media_type(table_interaction_message.message_media_type_image);
			t_interaction_message.setMessage_occurrence_time(TimeUtils
					.getCurrentTimeInString(new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss")));
			t_interaction_message.setMessage_sender(contacts_sender
					.getUser_id());
			// 删除本地文件
			try {
//				File file = new File(upload_file_path+picture_temp_file_name);
//				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			// 发送消息
			if (!StringUtils.isBlank(msg)) {
				new thread_interaction_xmpp_send_text_message(
						t_interaction_message, network_handler).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void f_action_send_image_from_local() {
		try {// 显示本地图片选择器
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, 2);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_send_image_from_camera() {
		try {// 显示摄像头
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			String picture_temp_file_path = upload_file_path + "temp.png";
			File temp = new File(picture_temp_file_path);
			Uri imageUri = Uri.fromFile(temp);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, 1);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 剪切从相机或者相册中获取的图片
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
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			switch (requestCode) {
			case 1:
				// 来自摄像头
				startPhotoZoom(Uri.fromFile(new File(upload_file_path + "temp.png")),
						3);
				break;
			case 2:
				// 来自相册
				startPhotoZoom(data.getData(), 3);
				break;
			case 3:
				// 来自剪切处理后的图片
				Bundle extras = data.getExtras();
				Bitmap bitmap = (Bitmap) extras.get("data");
				f_action_upload_image(bitmap);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 图片文件发送处理
	private void f_action_upload_image(Bitmap bitmap) {
		try {
			// --
			picture_temp_file_name = "p_"+TimeUtils
					.getCurrentTimeInString(new SimpleDateFormat(
							"yyyyMMddHHmmss", Locale.CHINA))
					+ "_" + contacts_sender.getUser_id() + ".jpg";
			// 保存文件
			save_bitmap(bitmap, upload_file_path+picture_temp_file_name);
			interface_app_upload_file_req req = new interface_app_upload_file_req();
			req.setUpload_file_path(upload_file_path);
			req.setUpload_file_name(picture_temp_file_name);
			req.setUpload_user_id(contacts_sender.getUser_id());
			req.setUpload_file_type("picture");
			thread_upload_media_files upload = new thread_upload_media_files(
					network_handler, req);
			upload.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
 /** 
  * 保存图片
  */  
 public void save_bitmap(Bitmap bm, String fileName) { 
 	try{
 		File dirFile = new File(fileName);  
 		if(!dirFile.exists()){  
 			dirFile.createNewFile();
 		}  
 		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dirFile));  
 		bm.compress(Bitmap.CompressFormat.PNG, 90, bos);  
 		bos.flush();  
 		bos.close();  
 	}catch(IOException e){
 		e.printStackTrace();
 	}
 }

	private Handler network_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 语音文件发送成功
			if (msg.what == message_what_values.activity_interaction_chat_message_send_voice_success) {
				f_action_upload_voice_success();
			}
			// 语音文件发送失败
			else if (msg.what == message_what_values.activity_interaction_chat_message_send_voice_failure) {
				Toast.makeText(getApplicationContext(), "语音发送失败!",
						Toast.LENGTH_LONG).show();
			}
			// 图片文件发送成功
			else if (msg.what == message_what_values.activity_interaction_chat_message_send_image_success) {
				f_action_upload_image_success();
			}
			// 图片文件发送成功
			else if (msg.what == message_what_values.activity_interaction_chat_message_send_image_failure) {
				Toast.makeText(getApplicationContext(), "网络不给力,图片发送失败!",
						Toast.LENGTH_LONG).show();
			}
			// 文本消息发送成功
			else if (msg.what == message_what_values.activity_interaction_chat_message_send_text_success) {
				f_action_send_text_message_success();
				// listview滑动到下一条消息
				
			}
			// 文本消息发送失败
			else if (msg.what == message_what_values.activity_interaction_chat_message_send_text_failure) {

			}
			// 成功接收一条对方的消息
			else if (msg.what == message_what_values.activity_interaction_chat_message_receive_message) {
				f_action_receive_message_success(msg);
			}
		}
	};

	private void show_toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			// 注册消息接收器
			register_message_broadcast_receiver();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregister_message_broadcast_receiver();
		super.onPause();
	}

}
