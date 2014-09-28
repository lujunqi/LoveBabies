package com.xqj.lovebabies.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.trinea.android.common.util.StringUtils;
import cn.trinea.android.common.util.TimeUtils;

import com.xqj.lovebabies.broadcasts.activity_chat_message_receiver;
import com.xqj.lovebabies.commons.utils_common_tools;
import com.xqj.lovebabies.commons.utils_xmpp_client;
import com.xqj.lovebabies.databases.dbs_interaction_contacts;
import com.xqj.lovebabies.databases.dbs_interaction_message;
import com.xqj.lovebabies.databases.table_interaction_contacts;
import com.xqj.lovebabies.databases.table_interaction_message;

//im消息接收监听
public class utils_xmpp_client_message implements MessageListener {
	public final static String ACTION_RECEIVE_INTERACTION_MESSAGE = "com.xqj.lovebabies.broadcast.RECEIVEMESSAGE";

	private utils_common_tools tools = new utils_common_tools();
	private Context context = null;
	private table_interaction_message t_interaction_message = null;
	private dbs_interaction_message db_interaction_message = null;
	private dbs_interaction_contacts db_interaction_contacts = null;

	public utils_xmpp_client_message(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public void processMessage(Chat arg0, Message arg1) {
		// TODO Auto-generated method stub
		try {
			db_interaction_message = new dbs_interaction_message(context);
			t_interaction_message = new table_interaction_message();
			// --
			String sender = arg1.getFrom();
			String receiver = arg1.getTo();
			String content = arg1.getBody();
			String content_type = null;
			// 获取标准格式的sender
			if (!StringUtils.isEquals(utils_xmpp_client.getInstance().get_connection().getServiceName(), sender)) {
				sender = sender.substring(0, sender.lastIndexOf("@"));
				t_interaction_message.setMessage_sender(sender);
			} else {
				t_interaction_message.setMessage_sender(sender);
			}
			// 获取标准格式的receiver
			receiver = receiver.substring(0, receiver.lastIndexOf("@"));
			t_interaction_message.setMessage_receiver(receiver);
			// 获取媒体相关信息
			if (content.length() > 5) {
				// 长度小于或者等5的都是文本消息
				content_type = content.substring(0, 5);
				if (StringUtils.isEquals("[rec]", content_type)) {
					content_type = table_interaction_message.message_media_type_voice;
					content = content.substring(content.indexOf("http://"), content.length());
				} else if (StringUtils.isEquals("[pic]", content_type)) {
					content_type = table_interaction_message.message_media_type_image;
					content = content.substring(content.indexOf("http://"), content.length());
				} else {
					content_type = table_interaction_message.message_media_type_text;
				}
			} else {
				content_type = table_interaction_message.message_media_type_text;
			}
			t_interaction_message.setMessage_media_type(content_type);
			t_interaction_message.setMessage_content(content);
			t_interaction_message.setMessage_content_length(tools.get_voice_length(content));
			// 设置消息方向
			t_interaction_message.setMessage_direction_type(table_interaction_message.message_direction_type_mo);
			// 设置消息阅读状态
			t_interaction_message.setMessage_read_status(table_interaction_message.message_read_status_unread);
			// 设置消息接收时间
			t_interaction_message.setMessage_occurrence_time(TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
			// 保存消息到数据库
			db_interaction_message.do_insert_data(t_interaction_message);
			// 保存最后会话记录到contacts

			try {
				String sql = null;
				if (StringUtils.isEquals(t_interaction_message.getMessage_media_type(), table_interaction_message.message_media_type_image)) {
					content = "[图片]";
				} else if (StringUtils.isEquals(t_interaction_message.getMessage_media_type(), table_interaction_message.message_media_type_voice)) {
					content = "[语音]";
				} else {
					content = t_interaction_message.getMessage_content();
				}
				db_interaction_contacts = new dbs_interaction_contacts(context);
				sql = "update t_interaction_contacts set user_last_session_content='" + content + "',";
				sql += "user_last_session_time='" + TimeUtils.getCurrentTimeInString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) + "' ";
				sql += "where user_id='" + t_interaction_message.getMessage_sender() + "'";
				db_interaction_contacts.do_execute_sql(sql);
			} catch (Exception e) {
				// TODO: handle exception
			}

			// 发送广播
			System.out.println("FROM:" + t_interaction_message.getMessage_sender() + ";To:" + t_interaction_message.getMessage_receiver() + "Content:" + t_interaction_message.getMessage_content());
			Intent intent = new Intent();
			intent.setAction(ACTION_RECEIVE_INTERACTION_MESSAGE);
			Bundle bundle = new Bundle();
			bundle.putSerializable("received_interaction_message", t_interaction_message);
			intent.putExtra("interaction_message", bundle);
			context.sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}
}
