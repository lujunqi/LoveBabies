package com.xqj.lovebabies.activitys;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.StringUtils;
import cn.trinea.android.common.util.TimeUtils;
import cn.trinea.android.common.view.DropDownListView;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.adapters.adapter_activity_chat_gridview_image_inside;
import com.xqj.lovebabies.adapters.adapter_activity_chat_listview;
import com.xqj.lovebabies.adapters.adapter_activity_chat_listview_b;
import com.xqj.lovebabies.broadcasts.activity_chat_message_receiver;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_picture_caches;
import com.kubility.demo.MP3Recorder;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import com.xqj.lovebabies.threads.thread_common_upload_files;
import com.xqj.lovebabies.widgets.TopActionBar;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ActivityInteractionChat extends Activity {
	private utils_common_tools tools = new utils_common_tools();
	private TopActionBar cmd_topactionbar_menu = null;
	private Button cmd_button_option_image_message = null;
	private Button cmd_button_send_voice_message = null;
	private Button cmd_button_option_voice_message = null;
	private Button cmd_button_option_text_message = null;
	private Button cmd_button_send_text_message = null;
	private Button cmd_button_add_image_from_inside = null;
	private Button cmd_button_add_image_from_local = null;
	private Button cmd_button_add_image_from_camera = null;
	private EditText cmd_edittext_text_message = null;
	private ImageView cmd_imageview_voice_amp_indicator = null;
	private LinearLayout cmd_layout_image_options = null;
	private DropDownListView cmd_listview_chat_message = null;
	private GridView cmd_gridview_image_inside = null;
	private listener_activity_chat_on_click on_click_listener = null;
	private listener_activity_chat_text_message_text_change text_watcher = null;
	private int history_message_page = 0;
	private table_interaction_message t_interaction_message = null;
	private dbs_interaction_message db_interaction_message = null;
	private dbs_interaction_contacts db_interaction_contacts = null;
	private List<table_interaction_message> temp_list_messages = null;
	private LinkedList<table_interaction_message> list_messages = null;
	private adapter_activity_chat_listview list_messages_adapter = null;
	private activity_chat_message_receiver broadcast_interaction_message_receiver = null;
	private table_interaction_contacts contacts_receiver = null;
	private table_interaction_contacts contacts_sender = null;
	private MP3Recorder voice_recorder = null;
	private thread_interaction_update_voice_amp_indicator t_voice_amp_indicator_update = null;
	private String voice_record_temp_file_path = null;
	private String voice_record_temp_file_name = null;
	private String picture_temp_file_path = null;
	private String picture_temp_file_name = null;

	private MediaPlayer mplayer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_interaction_chat);
		try {
			// ��ȡ�������ID����Ϣ��receiver
			Intent intent = getIntent();
			contacts_receiver = new table_interaction_contacts();
			Bundle bundle = intent.getBundleExtra("chat_receiver");
			contacts_receiver = (table_interaction_contacts) bundle.getSerializable("chat_receiver");
			// ��ȡ�Լ���ID����Ϣ��sender
			contacts_sender = new table_interaction_contacts();
			contacts_sender.setUser_id(PreferencesUtils.getString(this, "user_id"));
			contacts_sender.setUser_real_name(PreferencesUtils.getString(this, "user_real_name"));
			contacts_sender.setUser_icon_path(PreferencesUtils.getString(this, "user_icon"));
			// ��ʼ��
			init();
		} catch (Exception e) {
			// TODO: handle exception
		}

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
		// ��ʼ����Ϣ�б�
		try {
			f_action_option_text_message();
			history_message_page = 1;
			list_messages.clear();
			register_message_broadcast_receiver();
			f_action_get_history_message();
		} catch (Exception e) {
			// TODO: handle exception
		}
		// ��ʼ������״̬��
		set_chat_status(true);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		unregister_message_broadcast_receiver();
		set_chat_status(false);
		super.onPause();
	}

	private void init() {
		try {
			int image_option_icon_size = (int) getResources().getDimension(R.dimen.activity_chat_image_option_icon_size);
			int image_option_icon_text_size = (int) getResources().getDimension(R.dimen.activity_chat_image_option_text_size);
			on_click_listener = new listener_activity_chat_on_click(handler);
			text_watcher = new listener_activity_chat_text_message_text_change(handler);
			// --
			list_messages = new LinkedList<table_interaction_message>();
			cmd_listview_chat_message = (DropDownListView) findViewById(R.id.activity_chat_listview_message);
			list_messages_adapter = new adapter_activity_chat_listview(this, handler, list_messages, contacts_receiver, contacts_sender);
			cmd_listview_chat_message.setAdapter(list_messages_adapter);
			cmd_listview_chat_message.setOnDropDownListener(new listener_activity_chat_listview_on_drop_down(handler));
			cmd_listview_chat_message.setItemsCanFocus(false);

			// --
			cmd_topactionbar_menu = (TopActionBar) findViewById(R.id.activity_chat_topactionbar_menu);
			cmd_topactionbar_menu.getCmd_imagebutton_more().setImageResource(R.drawable.actionbar_icon_cancel);
			cmd_topactionbar_menu.getCmd_imagebutton_more().setOnClickListener(new listener_activity_chat_on_click(handler));
			cmd_topactionbar_menu.getCmd_textview_title_name().setText(contacts_receiver.getUser_real_name());
			try {
				cmd_topactionbar_menu.getCmd_imagebutton_menu().setImageResource(R.drawable.default_logo);
				String imgurl = network_interface_paths.get_project_root + contacts_receiver.getUser_icon_path();
				utils_picture_caches.getInstance().init(ActivityInteractionChat.this);// ��ʼ��ͼƬ����
				ImageLoader.getInstance().displayImage(imgurl, cmd_topactionbar_menu.getCmd_imagebutton_menu());
			} catch (Exception e) {
				// TODO: handle exception
			}
			// --
			cmd_imageview_voice_amp_indicator = (ImageView) findViewById(R.id.activity_interaction_chat_voice_amp_indicator);
			// --
			cmd_button_option_image_message = (Button) findViewById(R.id.activity_chat_button_option_image_message);
			cmd_button_option_image_message.setOnClickListener(on_click_listener);
			cmd_button_option_image_message.setText("ͼƬ");
			// --
			cmd_button_send_voice_message = (Button) findViewById(R.id.activity_chat_button_send_voice_message);
			cmd_button_send_voice_message.setOnTouchListener(new listener_activity_chat_button_on_touch(handler));
			cmd_button_send_voice_message.setOnLongClickListener(new listener_activity_chat_button_on_touch(handler));
			// --
			cmd_button_send_voice_message.setText("��ס˵��");
			cmd_button_send_voice_message.setVisibility(View.GONE);
			// --
			cmd_button_option_voice_message = (Button) findViewById(R.id.activity_chat_button_option_voice_message);
			cmd_button_option_voice_message.setOnClickListener(on_click_listener);
			cmd_button_option_voice_message.setText("����");
			cmd_button_option_voice_message.setVisibility(View.GONE);
			// --
			cmd_button_option_text_message = (Button) findViewById(R.id.activity_chat_button_option_text_message);
			cmd_button_option_text_message.setOnClickListener(on_click_listener);
			cmd_button_option_text_message.setText("����");
			// --
			cmd_button_send_text_message = (Button) findViewById(R.id.activity_chat_button_send_text_message);
			cmd_button_send_text_message.setOnClickListener(on_click_listener);
			cmd_button_send_text_message.setText("����");
			cmd_button_send_text_message.setVisibility(View.GONE);
			// --
			cmd_edittext_text_message = (EditText) findViewById(R.id.activity_chat_edittext_message);
			cmd_edittext_text_message.addTextChangedListener(text_watcher);
			// --
			cmd_layout_image_options = (LinearLayout) findViewById(R.id.activity_chat_layout_image_options);
			cmd_layout_image_options.setVisibility(View.GONE);
			//
			cmd_button_add_image_from_camera = (Button) findViewById(R.id.activity_chat_button_add_image_from_camera);
			cmd_button_add_image_from_camera.setOnClickListener(on_click_listener);
			cmd_button_add_image_from_camera.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.activity_chat_add_image_camare, image_option_icon_size, image_option_icon_size), null, null);
			cmd_button_add_image_from_camera.setTextSize(image_option_icon_text_size);
			cmd_button_add_image_from_camera.setText("����");
			// --
			cmd_button_add_image_from_inside = (Button) findViewById(R.id.activity_chat_button_add_image_from_inside);
			cmd_button_add_image_from_inside.setOnClickListener(on_click_listener);
			cmd_button_add_image_from_inside.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.activity_chat_add_image_inside, image_option_icon_size, image_option_icon_size), null, null);
			cmd_button_add_image_from_inside.setTextSize(image_option_icon_text_size);
			cmd_button_add_image_from_inside.setText("����");
			// --
			cmd_button_add_image_from_local = (Button) findViewById(R.id.activity_chat_button_add_image_from_local);
			cmd_button_add_image_from_local.setOnClickListener(on_click_listener);
			cmd_button_add_image_from_local.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.activity_chat_add_image_local, image_option_icon_size, image_option_icon_size), null, null);
			cmd_button_add_image_from_local.setTextSize(image_option_icon_text_size);
			cmd_button_add_image_from_local.setText("���");
			// --
			cmd_gridview_image_inside = (GridView) findViewById(R.id.activity_chat_gridview_inside_image);
			cmd_gridview_image_inside.setAdapter(new adapter_activity_chat_gridview_image_inside(this));
			cmd_gridview_image_inside.setOnItemClickListener(new listener_activity_chat_inside_image_gridview_on_item_click(handler));
			f_action_option_text_message();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void set_chat_status(boolean onoroff) {
		try {
			if (onoroff) {
				// ���ں�receiver����Ͱ�IDд�����ã����㲥�����ȡ
				PreferencesUtils.putString(this, "chat_user_id", contacts_receiver.getUser_id());
			} else {
				PreferencesUtils.putString(this, "chat_user_id", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// ���ذ�ť
			if (msg.what == message_what_values.widget_top_action_bar_button_menu_click) {
				f_action_exit_window();
			}
			// ���ͼƬ��ť����ͼƬ��Դѡ����
			if (msg.what == message_what_values.activity_interaction_chat_button_option_image_on_click) {
				f_action_show_image_options();
			}
			// ������鰴ť��������ѡ����
			if (msg.what == message_what_values.activity_interaction_chat_button_option_image_from_inside_on_click) {
				f_action_option_image_from_inside();
			}
			// �������ť�������ճ���
			if (msg.what == message_what_values.activity_interaction_chat_button_option_image_from_camera_on_click) {
				f_action_send_image_from_camera();
			}
			// �������ͼƬ��ť��������ͼƬѡ�����
			if (msg.what == message_what_values.activity_interaction_chat_button_option_image_from_local_on_click) {
				f_action_send_image_from_local();
			}
			// ����ѡ�񲢷���
			if (msg.what == message_what_values.activity_interaction_chat_message_send_image_inside) {
				f_action_send_image_from_inside(msg);
			}
			// ���������ťѡ����������Ϣģʽ
			if (msg.what == message_what_values.activity_interaction_chat_button_option_voice_on_click) {
				f_action_option_voice_message();
			}
			// �����������Ͱ�ť��ʼ¼��
			if (msg.what == message_what_values.activity_interaction_chat_button_send_voice_on_longclick) {
				f_action_record_voice_begin();
			}
			// �ɿ��������Ͱ�ť����¼�������������ļ�
			if (msg.what == message_what_values.activity_interaction_chat_button_send_voice_on_touch) {
				f_action_record_voice_end();
				f_action_upload_voice();
			}
			// �����ļ����ͳɹ�
			if (msg.what == message_what_values.activity_interaction_chat_message_send_voice_success) {
				f_action_upload_voice_success();
			}
			// �����ļ�����ʧ��
			if (msg.what == message_what_values.activity_interaction_chat_message_send_voice_failure) {
				Toast.makeText(getApplicationContext(), "���粻����,��������ʧ��!", Toast.LENGTH_LONG).show();
			}
			// ͼƬ�ļ����ͳɹ�
			if (msg.what == message_what_values.activity_interaction_chat_message_send_image_success) {
				f_action_upload_image_success();
			}
			// ͼƬ�ļ����ͳɹ�
			if (msg.what == message_what_values.activity_interaction_chat_message_send_image_failure) {
				Toast.makeText(getApplicationContext(), "���粻����,ͼƬ����ʧ��!", Toast.LENGTH_LONG).show();
			}
			// ����ı���ťѡ���ı���Ϣģʽ
			if (msg.what == message_what_values.activity_interaction_chat_button_option_text_on_click) {
				f_action_option_text_message();
			}
			// �ı������ֱ仯
			if (msg.what == message_what_values.activity_interaction_chat_edittext_text_on_change) {
				f_action_text_message_text_change(msg);
			}
			// �����ı���Ϣ
			if (msg.what == message_what_values.activity_interaction_chat_button_send_text_on_click) {
				f_action_send_text_message();
			}
			// �ı���Ϣ���ͳɹ�
			if (msg.what == message_what_values.activity_interaction_chat_message_send_text_success) {
				f_action_send_text_message_success();
			}
			// �ı���Ϣ����ʧ��
			if (msg.what == message_what_values.activity_interaction_chat_message_send_text_failure) {

			}
			// �ɹ�����һ���Է�����Ϣ
			if (msg.what == message_what_values.activity_interaction_chat_message_receive_message) {
				f_action_receive_message_success(msg);
			}
			// ��Ϣlistview������Ӧ
			if (msg.what == message_what_values.activity_interaction_chat_listview_message_drop_down) {
				f_action_get_history_message();
			}
			// ����¼�����
			if (msg.what == message_what_values.activity_interaction_chat_message_update_voice_amplitude) {
				f_action_update_voice_amp_indicator();
			}
			// ����������Ϣ
			if (msg.what == message_what_values.activity_interaction_chat_message_play_voice) {
				f_action_play_voice_message(msg);
			}
		};
	};

	// ¼����ʼ����
	private void f_action_record_voice_begin() {
		try {
			// --¼��
			voice_record_temp_file_name = TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)) + "_" + contacts_sender.getUser_id() + ".mp3";
			// --
			voice_record_temp_file_path = "";
			voice_record_temp_file_path += PreferencesUtils.getString(this, "sys_path_sd_card");
			voice_record_temp_file_path += PreferencesUtils.getString(this, "sys_path_app_folder");
			voice_record_temp_file_path += PreferencesUtils.getString(this, "sys_path_temp");
			voice_record_temp_file_path += File.separator;
			voice_record_temp_file_path += voice_record_temp_file_name;
			// --
			voice_recorder = new MP3Recorder(voice_record_temp_file_path, 11025);
			voice_recorder.start();
			System.out.println("voice_recorder.start()");
			// --��ʾ��Ƶ���
			cmd_imageview_voice_amp_indicator.setVisibility(View.VISIBLE);
			thread_interaction_update_voice_amp_indicator.thread_status = true;
			t_voice_amp_indicator_update = new thread_interaction_update_voice_amp_indicator(handler);
			t_voice_amp_indicator_update.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ¼����ɴ���
	private void f_action_record_voice_end() {
		try {
			cmd_imageview_voice_amp_indicator.setVisibility(View.GONE);
			thread_interaction_update_voice_amp_indicator.thread_status = false;
			voice_recorder.stop();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ¼���ļ����ʹ���
	private void f_action_upload_voice() {
		try {
			voice_record_temp_file_path = voice_record_temp_file_path == null ? "" : voice_record_temp_file_path;
			interface_app_upload_file_req req = new interface_app_upload_file_req();
			req.setUpload_file_path(voice_record_temp_file_path);
			req.setUpload_user_id(contacts_sender.getUser_id());
			req.setUpload_file_type("voice");
			thread_common_upload_files upload = new thread_common_upload_files(handler, req);
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
			String voice_time_leng = tools.get_voice_length(voice_record_temp_file_path);
			msg += "[" + voice_time_leng + "]";
			String remote_uri = network_interface_paths.get_temp_file + "/" + contacts_sender.getUser_id() + "/" + voice_record_temp_file_name;
			msg += remote_uri;
			// --
			t_interaction_message = new table_interaction_message();
			t_interaction_message.setMessage_receiver(contacts_receiver.getUser_id());
			t_interaction_message.setMessage_content(msg);
			t_interaction_message.setMessage_content_length(voice_time_leng);
			t_interaction_message.setMessage_direction_type(table_interaction_message.message_direction_type_mt);
			t_interaction_message.setMessage_media_type(table_interaction_message.message_media_type_voice);
			t_interaction_message.setMessage_occurrence_time(TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
			t_interaction_message.setMessage_sender(contacts_sender.getUser_id());
			// ɾ������¼���ļ�
			try {
				File file = new File(voice_record_temp_file_path);
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			// ������Ϣ
			if (!StringUtils.isBlank(msg)) {
				new thread_interaction_xmpp_send_text_message(t_interaction_message, handler).start();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ¼���ļ��ϴ�ʧ��
	private void f_action_upload_voice_failure() {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ���������ʾ��
	private void f_action_update_voice_amp_indicator() {
		try {
			int vuSize = 0;
			vuSize = (int) voice_recorder.getMaxAmplitude();
			vuSize = 7 * vuSize / 30;
			if (vuSize <= 1) {
				cmd_imageview_voice_amp_indicator.setImageResource(R.drawable.voice_indicator_amp1);
			}
			if (vuSize == 2) {
				cmd_imageview_voice_amp_indicator.setImageResource(R.drawable.voice_indicator_amp2);
			}
			if (vuSize == 3) {
				cmd_imageview_voice_amp_indicator.setImageResource(R.drawable.voice_indicator_amp3);
			}
			if (vuSize == 4) {
				cmd_imageview_voice_amp_indicator.setImageResource(R.drawable.voice_indicator_amp4);
			}
			if (vuSize == 5) {
				cmd_imageview_voice_amp_indicator.setImageResource(R.drawable.voice_indicator_amp5);
			}
			if (vuSize == 6) {
				cmd_imageview_voice_amp_indicator.setImageResource(R.drawable.voice_indicator_amp6);
			}
			if (vuSize >= 7) {
				cmd_imageview_voice_amp_indicator.setImageResource(R.drawable.voice_indicator_amp7);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_exit_window() {
		try {
			finish();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ������ʷ�����¼
	private void f_action_get_history_message() {
		String sql = "";
		try {
			int history_message_pagesize = 8;
			history_message_page = history_message_page <= 0 ? 1 : history_message_page;
			db_interaction_message = new dbs_interaction_message(this);
			sql = "select * from t_interaction_message where 1=1 ";
			sql += "and (message_sender='" + contacts_receiver.getUser_id() + "' or message_receiver='" + contacts_receiver.getUser_id() + "') ";
			sql += "order by message_occurrence_time desc ";
			sql += "limit " + ((history_message_page - 1) * history_message_pagesize) + "," + history_message_pagesize;
			temp_list_messages = db_interaction_message.do_select_data(sql);
			// ---
			temp_list_messages = temp_list_messages == null ? new ArrayList<table_interaction_message>() : temp_list_messages;
			if (temp_list_messages.size() > 0) {
				for (int i = 0; i < temp_list_messages.size(); i++) {
					t_interaction_message = temp_list_messages.get(i);
					list_messages.addFirst(t_interaction_message);
				}
				list_messages_adapter.notifyDataSetChanged();

				cmd_listview_chat_message.smoothScrollToPosition(history_message_pagesize + 1);
				history_message_page += 1;
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			cmd_listview_chat_message.onDropDownComplete();

		}
	}

	// ������Ϣ����ʾ������Ự��
	private void f_action_receive_message_success(Message message) {
		try {
			t_interaction_message = (table_interaction_message) message.obj;
			// �����������,�������Ϣ��������һ��������û��� ʵʱ���½���
			if (StringUtils.isEquals(t_interaction_message.getMessage_sender(), contacts_receiver.getUser_id())) {
				list_messages.addLast(t_interaction_message);
				list_messages_adapter.notifyDataSetChanged();
				cmd_listview_chat_message.smoothScrollToPosition(list_messages.size());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ��Ϣ������ı��ı��¼�
	private void f_action_text_message_text_change(Message message) {
		try {
			if (message.arg1 == 0) {
				cmd_button_option_voice_message.setVisibility(View.VISIBLE);
				cmd_button_send_text_message.setVisibility(View.GONE);
			} else {
				cmd_button_option_voice_message.setVisibility(View.GONE);
				cmd_button_send_text_message.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_show_image_options() {
		try {
			// ��ʾͼƬ����ѡ���������飬����ͼƬ������ͷ
			if (cmd_layout_image_options.getVisibility() == View.GONE) {
				cmd_layout_image_options.setVisibility(View.VISIBLE);
			} else {
				cmd_layout_image_options.setVisibility(View.GONE);
			}
			// �������ѡ������ʾ�Ľ�ѡ�������
			if (cmd_gridview_image_inside.getVisibility() == View.VISIBLE) {
				cmd_gridview_image_inside.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_option_voice_message() {
		try {
			// �л���������Ϣģʽ
			cmd_layout_image_options.setVisibility(View.GONE);
			// --
			cmd_edittext_text_message.setVisibility(View.GONE);
			// --
			cmd_button_option_image_message.setVisibility(View.VISIBLE);
			// --
			cmd_button_option_text_message.setVisibility(View.VISIBLE);
			// --
			cmd_button_send_voice_message.setVisibility(View.VISIBLE);
			// --
			cmd_button_send_text_message.setVisibility(View.GONE);
			// --
			cmd_button_option_voice_message.setVisibility(View.GONE);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_play_voice_message(Message message) {
		try {
			String uri = message.obj.toString();
			uri = uri.substring(uri.indexOf("http://"), uri.length());
			if (mplayer != null && mplayer.isPlaying()) mplayer.stop();
			mplayer = new MediaPlayer();
			try {
				mplayer.setDataSource(uri);
				mplayer.prepare();
				mplayer.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	private void f_action_option_text_message() {
		try {
			// �л����ı���Ϣģʽ
			LinearLayout.LayoutParams params = null;
			// --
			cmd_layout_image_options.setVisibility(View.GONE);
			// --
			cmd_edittext_text_message.setVisibility(View.VISIBLE);
			// --
			cmd_button_option_image_message.setVisibility(View.VISIBLE);
			// --
			cmd_button_option_text_message.setVisibility(View.GONE);
			// --
			cmd_button_send_voice_message.setVisibility(View.GONE);
			// --
			cmd_button_send_text_message.setVisibility(View.GONE);
			// --
			cmd_button_option_voice_message.setVisibility(View.VISIBLE);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ��̬ע����Ϣ���չ㲥
	private void register_message_broadcast_receiver() {
		try {
			IntentFilter filter = new IntentFilter();
			broadcast_interaction_message_receiver = new activity_chat_message_receiver(handler);
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

	// �����ı���Ϣ
	private void f_action_send_text_message() {
		String msg = null;
		try {
			// ������Ϣ����
			msg = cmd_edittext_text_message.getText().toString();
			msg = msg == null ? "" : msg.trim();
			t_interaction_message = new table_interaction_message();
			t_interaction_message.setMessage_receiver(contacts_receiver.getUser_id());
			t_interaction_message.setMessage_content(msg);
			t_interaction_message.setMessage_content_length(String.valueOf(msg.length()));
			t_interaction_message.setMessage_direction_type(table_interaction_message.message_direction_type_mt);
			t_interaction_message.setMessage_media_type(table_interaction_message.message_media_type_text);
			t_interaction_message.setMessage_occurrence_time(TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
			t_interaction_message.setMessage_sender(contacts_sender.getUser_id());
			// ���edittext
			cmd_edittext_text_message.setText("");
			// ������Ϣ
			if (!StringUtils.isBlank(msg)) {
				new thread_interaction_xmpp_send_text_message(t_interaction_message, handler).start();
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
			db_interaction_message.do_insert_data(t_interaction_message);
			// ����������Ϣ���Ự
			db_interaction_contacts = new dbs_interaction_contacts(this);
			String sql = "update t_interaction_contacts set user_last_session_content='" + t_interaction_message.getMessage_content() + "',";
			sql += "user_last_session_time='" + TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) + "' ";
			sql += "where user_id='" + t_interaction_message.getMessage_receiver() + "'";
			System.out.println("---------����������Ϣ���Ự----------");
			System.out.println(sql);
			db_interaction_contacts.do_execute_sql(sql);
			
			// �����������
			list_messages.addLast(t_interaction_message);
			list_messages_adapter.notifyDataSetChanged();
			cmd_listview_chat_message.smoothScrollToPosition(list_messages.size());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_option_image_from_inside() {
		try {// ��ʾ�ڲ�����ѡ����
			if (cmd_gridview_image_inside.getVisibility() == View.GONE) {
				cmd_gridview_image_inside.setVisibility(View.VISIBLE);
			} else {
				cmd_gridview_image_inside.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ѡ�񲢷��ͱ���
	private void f_action_send_image_from_inside(Message message) {
		try {
			String msg = null;
			int index = message.arg1;
			if (index >= 0 && index < 10) {
				msg = "[/img00" + index + "]";
			} else if (index >= 10 && index < 100) {
				msg = "[/img0" + index + "]";
			} else if (index >= 100 && index < 1000) {
				msg = "[/img" + index + "]";
			}
			// ������Ϣ����
			t_interaction_message = new table_interaction_message();
			t_interaction_message.setMessage_receiver(contacts_receiver.getUser_id());
			t_interaction_message.setMessage_content(msg);
			t_interaction_message.setMessage_content_length(String.valueOf(msg.length()));
			t_interaction_message.setMessage_direction_type(table_interaction_message.message_direction_type_mt);
			t_interaction_message.setMessage_media_type(table_interaction_message.message_media_type_text);
			t_interaction_message.setMessage_occurrence_time(TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
			t_interaction_message.setMessage_sender(contacts_sender.getUser_id());
			// ���ر���ѡ����
			f_action_option_image_from_inside();
			// ������Ϣ
			if (!StringUtils.isBlank(msg)) {
				new thread_interaction_xmpp_send_text_message(t_interaction_message, handler).start();
			}
		} catch (Exception e) {
			// TODO: handle exception
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
			picture_temp_file_path = "";
			picture_temp_file_path += PreferencesUtils.getString(this, "sys_path_sd_card");
			picture_temp_file_path += PreferencesUtils.getString(this, "sys_path_app_folder");
			picture_temp_file_path += PreferencesUtils.getString(this, "sys_path_temp");
			picture_temp_file_path += File.separatorChar;
			picture_temp_file_path += "temp.png";
			File temp = new File(picture_temp_file_path);

			Uri imageUri = Uri.fromFile(temp);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, 1);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			switch (requestCode) {
			case 1:
				// ��������ͷ
				startPhotoZoom(Uri.fromFile(new File(picture_temp_file_path)), 3);
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
			picture_temp_file_name = TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)) + "_" + contacts_sender.getUser_id() + ".jpg";
			// --
			picture_temp_file_path = "";
			picture_temp_file_path += PreferencesUtils.getString(this, "sys_path_sd_card");
			picture_temp_file_path += PreferencesUtils.getString(this, "sys_path_app_folder");
			picture_temp_file_path += PreferencesUtils.getString(this, "sys_path_temp");
			picture_temp_file_path += File.separator;
			picture_temp_file_path += picture_temp_file_name;
			utils_common_tools.saveBimap(bitmap, picture_temp_file_path);
			picture_temp_file_path = picture_temp_file_path == null ? "" : picture_temp_file_path;
			// --
			interface_app_upload_file_req req = new interface_app_upload_file_req();
			req.setUpload_file_path(picture_temp_file_path);
			req.setUpload_user_id(contacts_sender.getUser_id());
			req.setUpload_file_type("picture");
			thread_common_upload_files upload = new thread_common_upload_files(handler, req);
			upload.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// ͼƬ�ļ��ϴ��ɹ�,��ʼ���Ͷ�Ӧ���ı���Ϣ��openfire
	private void f_action_upload_image_success() {
		try {
			String msg = null;
			msg = "[pic]";
			String remote_uri = network_interface_paths.get_temp_file + "/" + contacts_sender.getUser_id() + "/" + picture_temp_file_name;
			msg += remote_uri;
			// --
			t_interaction_message = new table_interaction_message();
			t_interaction_message.setMessage_receiver(contacts_receiver.getUser_id());
			t_interaction_message.setMessage_content(msg);
			t_interaction_message.setMessage_direction_type(table_interaction_message.message_direction_type_mt);
			t_interaction_message.setMessage_media_type(table_interaction_message.message_media_type_image);
			t_interaction_message.setMessage_occurrence_time(TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
			t_interaction_message.setMessage_sender(contacts_sender.getUser_id());
			// ɾ�������ļ�
			try {
				File file = new File(picture_temp_file_path);
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			// ������Ϣ
			if (!StringUtils.isBlank(msg)) {
				new thread_interaction_xmpp_send_text_message(t_interaction_message, handler).start();
			}
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

}
