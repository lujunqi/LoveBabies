package com.xqj.lovebabies.broadcasts;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.StringUtils;

import com.xqj.lovebabies.R;
import com.xqj.lovebabies.activitys.ActivityInteractionChatB;
import com.xqj.lovebabies.activitys.ActivityInteractionNoticeDetail;
import com.xqj.lovebabies.databases.*;
import com.xqj.lovebabies.services.service_xmpp_receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

//根据软件状态 在接受到消息的时候 发出提示音
public class activity_main_message_receiver extends BroadcastReceiver {
	private table_interaction_message t_interaction_message = null;
	private table_interaction_contacts t_interaction_contacts_sender = null;
	private table_interaction_notice t_interaction_notice = null;
	private dbs_interaction_contacts db_interaction_contacts = null;
	private NotificationManager nm = null;
	private Notification notification = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try {
			// --
			start_local_services(context);
			// --
			Bundle bundle = intent.getBundleExtra("interaction_message");
			t_interaction_message = (table_interaction_message) bundle.getSerializable("received_interaction_message");

			// 如果接受的消息不是正在与我聊天的用户发送的就发出提示音
			if (!StringUtils.isEquals(t_interaction_message.getMessage_sender(), PreferencesUtils.getString(context, "chat_user_id"))) {
				// 获取消息发送人的详情
				db_interaction_contacts = new dbs_interaction_contacts(context);
				List<table_interaction_contacts> list = db_interaction_contacts.do_select_data("select * from t_interaction_contacts where user_id='" + t_interaction_message.getMessage_sender() + "'");
				list = list == null ? new ArrayList<table_interaction_contacts>() : list;
				t_interaction_contacts_sender = list.get(0);
				t_interaction_contacts_sender = t_interaction_contacts_sender == null ? new table_interaction_contacts() : t_interaction_contacts_sender;
				// --
				nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				intent = new Intent();
				String notification_title = "";
				String notification_content = "";
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				// --
				if (StringUtils.isEquals(t_interaction_message.getMessage_sender(), "admin")) {//
					// 公告来自admin
					notification_title = "您有新的公告";
					bundle = new Bundle();
					bundle.putSerializable("notice", t_interaction_notice);
					intent.putExtra("chat_receiver", bundle);

					intent.setClass(context, ActivityInteractionNoticeDetail.class);
				} else {
					// 普通聊天消息
					notification_title = "您有新的消息";
					bundle = new Bundle();
					bundle.putSerializable("chat_receiver", t_interaction_contacts_sender);
					intent.putExtra("chat_receiver", bundle);
					intent.setClass(context, ActivityInteractionChatB.class);
					//
					notification_content = t_interaction_contacts_sender.getUser_real_name() + ":";
					if (StringUtils.isEquals(t_interaction_message.getMessage_media_type(), table_interaction_message.message_media_type_image)) {
						notification_content += "[图片]";
					} else if (StringUtils.isEquals(t_interaction_message.getMessage_media_type(), table_interaction_message.message_media_type_voice)) {
						notification_content += "[语音]";
					} else {
						notification_content += t_interaction_message.getMessage_content();
					}
				}
				PendingIntent pintent = PendingIntent.getActivity(context, 100, intent, 0);
				Builder builder = new NotificationCompat.Builder(context);
				builder.setContentTitle(notification_title);
				builder.setContentText(notification_content);
				builder.setSmallIcon(R.drawable.ic_launcher);
				builder.setAutoCancel(true);
				builder.setDefaults(Notification.DEFAULT_SOUND);
				builder.setContentIntent(pintent);
				notification = builder.build();
				nm.notify(t_interaction_message.getMessage_sender(), 10, notification);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	private void start_local_services(Context context) {
		try {
			Intent intent = new Intent();
			intent.setClass(context, service_xmpp_receiver.class);
			context.startService(intent);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
