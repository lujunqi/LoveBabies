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
			// 获取聊天好友ID，消息的receiver
			Intent intent = getIntent();
			contacts_receiver = new table_interaction_contacts();
			Bundle bundle = intent.getBundleExtra("chat_receiver");
			contacts_receiver = (table_interaction_contacts) bundle.getSerializable("chat_receiver");
			// 获取自己的ID，消息的sender
			contacts_sender = new table_interaction_contacts();
			contacts_sender.setUser_id(PreferencesUtils.getString(this, "user_id"));
			contacts_sender.setUser_real_name(PreferencesUtils.getString(this, "user_real_name"));
			contacts_sender.setUser_icon_path(PreferencesUtils.getString(this, "user_icon"));
			// 初始化
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
		// 初始化消息列表
		try {
			f_action_option_text_message();
			history_message_page = 1;
			list_messages.clear();
			register_message_broadcast_receiver();
			f_action_get_history_message();
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 初始化聊天状态，
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
				utils_picture_caches.getInstance().init(ActivityInteractionChat.this);// 初始化图片缓存
				ImageLoader.getInstance().displayImage(imgurl, cmd_topactionbar_menu.getCmd_imagebutton_menu());
			} catch (Exception e) {
				// TODO: handle exception
			}
			// --
			cmd_imageview_voice_amp_indicator = (ImageView) findViewById(R.id.activity_interaction_chat_voice_amp_indicator);
			// --
			cmd_button_option_image_message = (Button) findViewById(R.id.activity_chat_button_option_image_message);
			cmd_button_option_image_message.setOnClickListener(on_click_listener);
			cmd_button_option_image_message.setText("图片");
			// --
			cmd_button_send_voice_message = (Button) findViewById(R.id.activity_chat_button_send_voice_message);
			cmd_button_send_voice_message.setOnTouchListener(new listener_activity_chat_button_on_touch(handler));
			cmd_button_send_voice_message.setOnLongClickListener(new listener_activity_chat_button_on_touch(handler));
			// --
			cmd_button_send_voice_message.setText("按住说话");
			cmd_button_send_voice_message.setVisibility(View.GONE);
			// --
			cmd_button_option_voice_message = (Button) findViewById(R.id.activity_chat_button_option_voice_message);
			cmd_button_option_voice_message.setOnClickListener(on_click_listener);
			cmd_button_option_voice_message.setText("语音");
			cmd_button_option_voice_message.setVisibility(View.GONE);
			// --
			cmd_button_option_text_message = (Button) findViewById(R.id.activity_chat_button_option_text_message);
			cmd_button_option_text_message.setOnClickListener(on_click_listener);
			cmd_button_option_text_message.setText("键盘");
			// --
			cmd_button_send_text_message = (Button) findViewById(R.id.activity_chat_button_send_text_message);
			cmd_button_send_text_message.setOnClickListener(on_click_listener);
			cmd_button_send_text_message.setText("发送");
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
			cmd_button_add_image_from_camera.setText("拍照");
			// --
			cmd_button_add_image_from_inside = (Button) findViewById(R.id.activity_chat_button_add_image_from_inside);
			cmd_button_add_image_from_inside.setOnClickListener(on_click_listener);
			cmd_button_add_image_from_inside.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.activity_chat_add_image_inside, image_option_icon_size, image_option_icon_size), null, null);
			cmd_button_add_image_from_inside.setTextSize(image_option_icon_text_size);
			cmd_button_add_image_from_inside.setText("表情");
			// --
			cmd_button_add_image_from_local = (Button) findViewById(R.id.activity_chat_button_add_image_from_local);
			cmd_button_add_image_from_local.setOnClickListener(on_click_listener);
			cmd_button_add_image_from_local.setCompoundDrawablesWithIntrinsicBounds(null, tools.get_drawable_from_res(getResources(), R.drawable.activity_chat_add_image_local, image_option_icon_size, image_option_icon_size), null, null);
			cmd_button_add_image_from_local.setTextSize(image_option_icon_text_size);
			cmd_button_add_image_from_local.setText("相册");
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
				// 正在和receiver聊天就把ID写入配置，供广播处理读取
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
			// 返回按钮
			if (msg.what == message_what_values.widget_top_action_bar_button_menu_click) {
				f_action_exit_window();
			}
			// 点击图片按钮弹出图片来源选择器
			if (msg.what == message_what_values.activity_interaction_chat_button_option_image_on_click) {
				f_action_show_image_options();
			}
			// 点击表情按钮弹出表情选择器
			if (msg.what == message_what_values.activity_interaction_chat_button_option_image_from_inside_on_click) {
				f_action_option_image_from_inside();
			}
			// 点击摄像按钮弹出拍照程序
			if (msg.what == message_what_values.activity_interaction_chat_button_option_image_from_camera_on_click) {
				f_action_send_image_from_camera();
			}
			// 点击本地图片按钮弹出本地图片选择程序
			if (msg.what == message_what_values.activity_interaction_chat_button_option_image_from_local_on_click) {
				f_action_send_image_from_local();
			}
			// 表情选择并发送
			if (msg.what == message_what_values.activity_interaction_chat_message_send_image_inside) {
				f_action_send_image_from_inside(msg);
			}
			// 点击语音按钮选择发送语音消息模式
			if (msg.what == message_what_values.activity_interaction_chat_button_option_voice_on_click) {
				f_action_option_voice_message();
			}
			// 长按语音发送按钮开始录音
			if (msg.what == message_what_values.activity_interaction_chat_button_send_voice_on_longclick) {
				f_action_record_voice_begin();
			}
			// 松开语音发送按钮结束录音并发送语音文件
			if (msg.what == message_what_values.activity_interaction_chat_button_send_voice_on_touch) {
				f_action_record_voice_end();
				f_action_upload_voice();
			}
			// 语音文件发送成功
			if (msg.what == message_what_values.activity_interaction_chat_message_send_voice_success) {
				f_action_upload_voice_success();
			}
			// 语音文件发送失败
			if (msg.what == message_what_values.activity_interaction_chat_message_send_voice_failure) {
				Toast.makeText(getApplicationContext(), "网络不给力,语音发送失败!", Toast.LENGTH_LONG).show();
			}
			// 图片文件发送成功
			if (msg.what == message_what_values.activity_interaction_chat_message_send_image_success) {
				f_action_upload_image_success();
			}
			// 图片文件发送成功
			if (msg.what == message_what_values.activity_interaction_chat_message_send_image_failure) {
				Toast.makeText(getApplicationContext(), "网络不给力,图片发送失败!", Toast.LENGTH_LONG).show();
			}
			// 点击文本按钮选择文本消息模式
			if (msg.what == message_what_values.activity_interaction_chat_button_option_text_on_click) {
				f_action_option_text_message();
			}
			// 文本框文字变化
			if (msg.what == message_what_values.activity_interaction_chat_edittext_text_on_change) {
				f_action_text_message_text_change(msg);
			}
			// 发送文本消息
			if (msg.what == message_what_values.activity_interaction_chat_button_send_text_on_click) {
				f_action_send_text_message();
			}
			// 文本消息发送成功
			if (msg.what == message_what_values.activity_interaction_chat_message_send_text_success) {
				f_action_send_text_message_success();
			}
			// 文本消息发送失败
			if (msg.what == message_what_values.activity_interaction_chat_message_send_text_failure) {

			}
			// 成功接收一条对方的消息
			if (msg.what == message_what_values.activity_interaction_chat_message_receive_message) {
				f_action_receive_message_success(msg);
			}
			// 消息listview下拉相应
			if (msg.what == message_what_values.activity_interaction_chat_listview_message_drop_down) {
				f_action_get_history_message();
			}
			// 更新录音振幅
			if (msg.what == message_what_values.activity_interaction_chat_message_update_voice_amplitude) {
				f_action_update_voice_amp_indicator();
			}
			// 播放语音信息
			if (msg.what == message_what_values.activity_interaction_chat_message_play_voice) {
				f_action_play_voice_message(msg);
			}
		};
	};

	// 录音开始处理
	private void f_action_record_voice_begin() {
		try {
			// --录音
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
			// --显示音频振幅
			cmd_imageview_voice_amp_indicator.setVisibility(View.VISIBLE);
			thread_interaction_update_voice_amp_indicator.thread_status = true;
			t_voice_amp_indicator_update = new thread_interaction_update_voice_amp_indicator(handler);
			t_voice_amp_indicator_update.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 录音完成处理
	private void f_action_record_voice_end() {
		try {
			cmd_imageview_voice_amp_indicator.setVisibility(View.GONE);
			thread_interaction_update_voice_amp_indicator.thread_status = false;
			voice_recorder.stop();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 录音文件发送处理
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

	// 录音文件上传成功,开始发送对应的文本信息到openfire
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
			// 删除本地录音文件
			try {
				File file = new File(voice_record_temp_file_path);
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			// 发送消息
			if (!StringUtils.isBlank(msg)) {
				new thread_interaction_xmpp_send_text_message(t_interaction_message, handler).start();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 录音文件上传失败
	private void f_action_upload_voice_failure() {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 更新振幅显示器
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

	// 加载历史聊天记录
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

	// 接收消息并显示在聊天会话中
	private void f_action_receive_message_success(Message message) {
		try {
			t_interaction_message = (table_interaction_message) message.obj;
			// 更新聊天界面,如果发消息的是正在一起聊天的用户就 实时更新界面
			if (StringUtils.isEquals(t_interaction_message.getMessage_sender(), contacts_receiver.getUser_id())) {
				list_messages.addLast(t_interaction_message);
				list_messages_adapter.notifyDataSetChanged();
				cmd_listview_chat_message.smoothScrollToPosition(list_messages.size());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 消息输入框文本改变事件
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
			// 显示图片发送选择器，表情，本地图片、摄像头
			if (cmd_layout_image_options.getVisibility() == View.GONE) {
				cmd_layout_image_options.setVisibility(View.VISIBLE);
			} else {
				cmd_layout_image_options.setVisibility(View.GONE);
			}
			// 如果表情选择器显示的将选择框隐藏
			if (cmd_gridview_image_inside.getVisibility() == View.VISIBLE) {
				cmd_gridview_image_inside.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_option_voice_message() {
		try {
			// 切换到语音消息模式
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
			// 切换到文本消息模式
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

	// 动态注册消息接收广播
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

	// 动态注销消息接收广播
	private void unregister_message_broadcast_receiver() {
		try {
			unregisterReceiver(broadcast_interaction_message_receiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 发送文本消息
	private void f_action_send_text_message() {
		String msg = null;
		try {
			// 生成消息对象
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
			// 清空edittext
			cmd_edittext_text_message.setText("");
			// 发送消息
			if (!StringUtils.isBlank(msg)) {
				new thread_interaction_xmpp_send_text_message(t_interaction_message, handler).start();
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
			db_interaction_message.do_insert_data(t_interaction_message);
			// 更新最新消息到会话
			db_interaction_contacts = new dbs_interaction_contacts(this);
			String sql = "update t_interaction_contacts set user_last_session_content='" + t_interaction_message.getMessage_content() + "',";
			sql += "user_last_session_time='" + TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) + "' ";
			sql += "where user_id='" + t_interaction_message.getMessage_receiver() + "'";
			System.out.println("---------更新最新消息到会话----------");
			System.out.println(sql);
			db_interaction_contacts.do_execute_sql(sql);
			
			// 更新聊天界面
			list_messages.addLast(t_interaction_message);
			list_messages_adapter.notifyDataSetChanged();
			cmd_listview_chat_message.smoothScrollToPosition(list_messages.size());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void f_action_option_image_from_inside() {
		try {// 显示内部表情选择器
			if (cmd_gridview_image_inside.getVisibility() == View.GONE) {
				cmd_gridview_image_inside.setVisibility(View.VISIBLE);
			} else {
				cmd_gridview_image_inside.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 选择并发送表情
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
			// 生成消息对象
			t_interaction_message = new table_interaction_message();
			t_interaction_message.setMessage_receiver(contacts_receiver.getUser_id());
			t_interaction_message.setMessage_content(msg);
			t_interaction_message.setMessage_content_length(String.valueOf(msg.length()));
			t_interaction_message.setMessage_direction_type(table_interaction_message.message_direction_type_mt);
			t_interaction_message.setMessage_media_type(table_interaction_message.message_media_type_text);
			t_interaction_message.setMessage_occurrence_time(TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
			t_interaction_message.setMessage_sender(contacts_sender.getUser_id());
			// 隐藏表情选择器
			f_action_option_image_from_inside();
			// 发送消息
			if (!StringUtils.isBlank(msg)) {
				new thread_interaction_xmpp_send_text_message(t_interaction_message, handler).start();
			}
		} catch (Exception e) {
			// TODO: handle exception
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
				// 来自摄像头
				startPhotoZoom(Uri.fromFile(new File(picture_temp_file_path)), 3);
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

	// 图片文件上传成功,开始发送对应的文本信息到openfire
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
			// 删除本地文件
			try {
				File file = new File(picture_temp_file_path);
				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			// 发送消息
			if (!StringUtils.isBlank(msg)) {
				new thread_interaction_xmpp_send_text_message(t_interaction_message, handler).start();
			}
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

}
