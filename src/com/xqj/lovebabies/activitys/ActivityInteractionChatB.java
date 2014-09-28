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
	
	// ͷ��UI
	private ImageView head_btn_right;
	private ImageView head_btn_left;
	private TextView head_title;

	// �����ı����ݻ���¼������������ ����UI
	private ImageView record_imageview;
	private ImageView show_faces_imageview;
	private Button send_message_btn;
	private EditText content_edittext;
	private RelativeLayout content_layout;
	private Button record_voice_btn;

	// ѡ�� ���顢�����ͼƬ ����UI
	private LinearLayout show_media_btns_layout;
	private LinearLayout get_face_layout;
	private LinearLayout get_photo_layout;
	private LinearLayout get_album_layout;

	// ѡ�����ͼ�� UI
	private GridView chat_faces_gridview;

	private ImageView record_progress_imageview;// ¼�����ȶ���
	private RelativeLayout record_progress_layout;// ¼������layout
	// �Ự
	private UIListView chat_listview;

	private adapter_activity_chat_gridview_image_inside adapter_faces_gridview;

	// ¼��
	private boolean bl_long_pressed = false;// �Ƿ񳤰�¼����ť
	private MediaRecorder mRecorder = null;	// ¼��
	private MediaPlayer mPlayer = null;		// ������
	private Date beginRecordDate = null;// ��ʼ¼������
	private Date stopRecordDate = null; // ����¼������
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
	
	// ��Ϣ����
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
			
			// ��ȡ�������ID����Ϣ��receiver
			Intent intent = getIntent();
			contacts_receiver = new table_interaction_contacts();
			Bundle bundle = intent.getBundleExtra("chat_receiver");
			contacts_receiver = (table_interaction_contacts) bundle
					.getSerializable("chat_receiver");
			// ��ȡ�Լ���ID����Ϣ��sender
			contacts_sender = new table_interaction_contacts();
			contacts_sender.setUser_id(PreferencesUtils.getString(this,
					"user_id"));
			contacts_sender.setUser_real_name(PreferencesUtils.getString(this,
					"user_real_name"));
			contacts_sender.setUser_icon_path(PreferencesUtils.getString(this,
					"user_icon"));
			// ��ʼ��
			init_top_bar();
			init_bottom_bar();
			init_dialogue();

			// ��ѯ������Ϣ
			history_message_page = 1;
			f_action_get_history_message();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * ��ʼ��ͷ��
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
	 * ��ʼ���ײ����͡�¼����ѡ�������
	 */
	private void init_bottom_bar() {
		// ������Ϣ��
		record_imageview = (ImageView) findViewById(R.id.chat_record_imageview);
		show_faces_imageview = (ImageView) findViewById(R.id.chat_show_faces_imageview);
		send_message_btn = (Button) findViewById(R.id.chat_send_message_btn);
		content_edittext = (EditText) findViewById(R.id.chat_mesage_content_edittext);
		content_layout = (RelativeLayout) findViewById(R.id.chat_send_message_content_layout);
		record_voice_btn = (Button) findViewById(R.id.chat_get_record_voice_btn);
		record_voice_btn.setVisibility(View.GONE);
		show_faces_imageview.setOnClickListener(new OnClickListener() {// ��ʾ��ý���
					@Override
					public void onClick(View arg0) {
						change_ui_handler.sendMessage(change_ui_handler
								.obtainMessage(SHOW_MEDIA_BTNS));
					}
				});
		content_edittext.setOnClickListener(new OnClickListener() {// �������
					@Override
					public void onClick(View arg0) {
						change_ui_handler.sendMessage(change_ui_handler
								.obtainMessage(HIDE_FACES_MEDIA));
					}
				});
		send_message_btn.setOnClickListener(new OnClickListener() {// ���Ͱ�ť���
					@Override
					public void onClick(View arg0) {
						// �������ݣ�����ı���
						f_action_send_text_message();
					}
				});
		record_imageview.setOnClickListener(new OnClickListener() {// ����\��ʾ¼����ť
					@Override
					public void onClick(View arg0) {
						if (record_voice_btn.getVisibility() == View.VISIBLE) {// ����¼����ť
							change_ui_handler.sendMessage(change_ui_handler
									.obtainMessage(HIDE_RECORD_BTN));
						} else {// ��ʾ¼����ť
								// �����·���ý��ѡ���
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
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// ����
					record_voice_btn
							.setBackgroundResource(R.drawable.chart_radio_get_record_pressed);
					
					show_record_progress_dialog();//��ʾ¼������
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {// �ɿ�
					record_voice_btn
							.setBackgroundResource(R.drawable.chart_radio_get_record_normal);
					dismiss_record_progress_dialog();// ����¼������
					if (bl_long_pressed) {
						// ֹͣ¼�����ϴ�¼��
						f_action_record_voice_end();
						long voice_seconds = tools.get_second_differ(
								beginRecordDate, stopRecordDate);
						if (voice_seconds < 1) {// 1 second
							show_toast("¼��ʱ��̫��");
							// �����¼���ļ���ɾ��¼���ļ�
						} else if (voice_seconds > 5 * 60) {// 5 minute
							show_toast("¼��ʱ��̫��");
						} else {
							show_toast("¼�����");
							// ����¼��
							f_action_upload_voice();
						}
						bl_long_pressed = false;
					} else {
						show_toast("δ��ʼ¼��");
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
					// ��ʼ¼��
					f_action_record_voice_begin();
				} catch (Exception ex) {

				}
				return false;
			}
		});

		// ѡ����顢�����ͼƬ ��
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
				// ����¼����ť
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

		// ������
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
	 * ��ʼ���Ի�
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
			if (msg.what == SHOW_MEDIA_BTNS) {// ��ʾ\����ѡ��ͼƬ�������ͼƬ�İ�ť
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
			} else if (msg.what == SHOW_FACES_BTNS) {// ��ʾ\���ر���ѡ����
				if (chat_faces_gridview.getVisibility() == View.VISIBLE) {
					chat_faces_gridview.setVisibility(View.GONE);
				} else {
					chat_faces_gridview.setVisibility(View.VISIBLE);
					show_media_btns_layout.setVisibility(View.GONE);
				}
			} else if (msg.what == HIDE_FACES_MEDIA) {// �����·���ý��ѡ���
				chat_faces_gridview.setVisibility(View.GONE);
				show_media_btns_layout.setVisibility(View.GONE);
				show_faces_imageview
						.setImageResource(R.drawable.chart_faces_show);
			} else if (msg.what == SHOW_RECORD_BTN) {// ��ʾ¼����ť
				content_layout.setVisibility(View.GONE);// �����ı������
				hiddenInputMethod();// ���ؼ���
				record_voice_btn.setVisibility(View.VISIBLE);// ��ʾ¼����ť
				record_imageview
						.setImageResource(R.drawable.selector_interaction_chat_keyboard_button);
			} else if (msg.what == HIDE_RECORD_BTN) {// ����¼����ť
				content_layout.setVisibility(View.VISIBLE);// ��ʾ�ı������
				record_voice_btn.setVisibility(View.GONE);// ����¼����ť
				record_imageview
						.setImageResource(R.drawable.selector_interaction_chat_record_button);
			}
		};
	};

	// ��ʾ¼������
	private void show_record_progress_dialog() {
		// ��ʼ����
		record_progress_layout.setVisibility(View.VISIBLE);
		record_progress_imageview.setImageResource(R.drawable.amp1);
		progress_handler.postDelayed(progress_runnable, time_delay);
	}

	// ����¼������
	private void dismiss_record_progress_dialog() {
		record_arm_count = 0;
		record_progress_layout.setVisibility(View.GONE);
	}

	// ��̬��ʾ����
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

	// //���¼��·��
	// private boolean check_voice_dir(){
	// boolean sdCardExist = Environment.getExternalStorageState().equals(
	// android.os.Environment.MEDIA_MOUNTED);
	// if(sdCardExist){
	// String path = Environment.getExternalStorageDirectory().getPath()
	// + "/loveBaby/vol";
	// File pathFile = new File(path);
	// if (!pathFile.exists()) {
	// // ����ָ����·�������ļ���
	// pathFile.mkdirs();
	// }
	// return true;
	// }else{
	// return false;
	// }
	// }

	/**
	 * �������뷨
	 */
	private void hiddenInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(content_edittext.getWindowToken(), 0);
	}

	/**
	 * ��Ϣ����
	 */
	// ������ʷ�����¼
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

	// ������Ϣ����ʾ������Ự��
	private void f_action_receive_message_success(Message message) {
		try {
			t_interaction_message = (table_interaction_message) message.obj;
			// �����������,�������Ϣ��������һ��������û��� ʵʱ���½���
			if (StringUtils.isEquals(t_interaction_message.getMessage_sender(),
					contacts_receiver.getUser_id())) {
				list_messages_adapter.addItem(t_interaction_message);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ��̬ע����Ϣ���չ㲥
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

	// ��̬ע����Ϣ���չ㲥
	private void unregister_message_broadcast_receiver() {
		try {
			unregisterReceiver(broadcast_interaction_message_receiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * �ı���Ϣ
	 */
	// �����ı���Ϣ
	private void f_action_send_text_message() {
		String msg = null;
		try {
			// ������Ϣ����
			System.out.println("�����ı���Ϣ,������Ϣ����");
			System.out.println(contacts_sender.getUser_id() + "���͸�"
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
			// ���edittext
			content_edittext.setText("");
			// ������Ϣ
			if (!StringUtils.isBlank(msg)) {
				new thread_interaction_xmpp_send_text_message(
						t_interaction_message, network_handler).start();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// �����ı���Ϣ�ɹ�
	private void f_action_send_text_message_success() {
		try {
			// ����message�����ݿ�
			db_interaction_message = new dbs_interaction_message(this);
			t_interaction_message.setMessage_read_status("1");//�Ѷ�
			db_interaction_message.do_insert_data(t_interaction_message);
			// ����������Ϣ���Ự
			db_interaction_contacts = new dbs_interaction_contacts(this);
			String sql = "update t_interaction_contacts set user_last_session_content='"
					+ t_interaction_message.getMessage_content() + "',";
			sql += "user_last_session_time='"
					+ TimeUtils.getCurrentTimeInString(new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss")) + "' ";
			sql += "where user_id='"
					+ t_interaction_message.getMessage_receiver() + "'";
			System.out.println("---------����������Ϣ���Ự----------");
			System.out.println(sql);
			db_interaction_contacts.do_execute_sql(sql);

			// �����������
			list_messages_adapter.addItem(t_interaction_message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * ������ϢΪ�Ѷ�
	 */
	private boolean update_message_readed(table_interaction_message message){
		try {
			// ����message�����ݿ�
			db_interaction_message = new dbs_interaction_message(this);
			String sql = "update t_interaction_message set message_read_status=1 where message_sender="+message.getMessage_sender();
			System.out.println("update_sql-->"+sql);
			db_interaction_message.do_execute_sql(sql);
			//�޸�ҳ��
			message.setMessage_read_status("1");//�ĳ��Ѷ�
			list_messages_adapter.notifyDataSetChanged();
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	

	/**
	 * ¼�������ࡢͼ�����
	 */
	// ¼����ʼ����
	private void f_action_record_voice_begin() {
		try {
			System.out.println("��ʼ¼��...");
			if(mRecorder!=null){
				f_action_record_voice_end();
			}
			// --¼��
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

	// ¼����ɴ���
	private void f_action_record_voice_end() {
		try {
			System.out.println("ֹͣ¼��...");
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

	// ¼���ļ����ʹ���
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

	// ¼���ļ��ϴ��ɹ�,��ʼ���Ͷ�Ӧ���ı���Ϣ��openfire
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
			// ɾ������¼���ļ�
			try {
//				File file = new File(voice_record_temp_file_path);
//				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// ������Ϣ
			if (!StringUtils.isBlank(msg)){
				new thread_interaction_xmpp_send_text_message(
						t_interaction_message, network_handler).start();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * ������Ƶ��Handler
	 */
	private Handler handlePlayAudio = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			System.out.println("handlePlayAudio["+msg.arg1+"]["+msg.obj.toString()+"]");
			if(msg.arg1==1){//¼���ļ����سɹ�
				//���ó��Ѷ�
				String fileName = (String)msg.obj;
				stopPlaying();				// ��ֹͣ��һ��������
				startPlaying(fileName);		// ���¿�ʼһ��������
			}else{
				
			}
		}
	};
	
	/**
	 * ���ó��Ѷ�
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
	 * ��ʼ����
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
	 * ֹͣ����
	 */
	private void stopPlaying() {
		if(mPlayer!=null){
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	// ͼƬ�ļ��ϴ��ɹ�,��ʼ���Ͷ�Ӧ���ı���Ϣ��openfire
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
			// ɾ�������ļ�
			try {
//				File file = new File(upload_file_path+picture_temp_file_name);
//				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			// ������Ϣ
			if (!StringUtils.isBlank(msg)) {
				new thread_interaction_xmpp_send_text_message(
						t_interaction_message, network_handler).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void f_action_send_image_from_local() {
		try {// ��ʾ����ͼƬѡ����
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, 2);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_send_image_from_camera() {
		try {// ��ʾ����ͷ
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

	// ���д������������л�ȡ��ͼƬ
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			switch (requestCode) {
			case 1:
				// ��������ͷ
				startPhotoZoom(Uri.fromFile(new File(upload_file_path + "temp.png")),
						3);
				break;
			case 2:
				// �������
				startPhotoZoom(data.getData(), 3);
				break;
			case 3:
				// ���Լ��д�����ͼƬ
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

	// ͼƬ�ļ����ʹ���
	private void f_action_upload_image(Bitmap bitmap) {
		try {
			// --
			picture_temp_file_name = "p_"+TimeUtils
					.getCurrentTimeInString(new SimpleDateFormat(
							"yyyyMMddHHmmss", Locale.CHINA))
					+ "_" + contacts_sender.getUser_id() + ".jpg";
			// �����ļ�
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
  * ����ͼƬ
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
			// �����ļ����ͳɹ�
			if (msg.what == message_what_values.activity_interaction_chat_message_send_voice_success) {
				f_action_upload_voice_success();
			}
			// �����ļ�����ʧ��
			else if (msg.what == message_what_values.activity_interaction_chat_message_send_voice_failure) {
				Toast.makeText(getApplicationContext(), "��������ʧ��!",
						Toast.LENGTH_LONG).show();
			}
			// ͼƬ�ļ����ͳɹ�
			else if (msg.what == message_what_values.activity_interaction_chat_message_send_image_success) {
				f_action_upload_image_success();
			}
			// ͼƬ�ļ����ͳɹ�
			else if (msg.what == message_what_values.activity_interaction_chat_message_send_image_failure) {
				Toast.makeText(getApplicationContext(), "���粻����,ͼƬ����ʧ��!",
						Toast.LENGTH_LONG).show();
			}
			// �ı���Ϣ���ͳɹ�
			else if (msg.what == message_what_values.activity_interaction_chat_message_send_text_success) {
				f_action_send_text_message_success();
				// listview��������һ����Ϣ
				
			}
			// �ı���Ϣ����ʧ��
			else if (msg.what == message_what_values.activity_interaction_chat_message_send_text_failure) {

			}
			// �ɹ�����һ���Է�����Ϣ
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
			// ע����Ϣ������
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
