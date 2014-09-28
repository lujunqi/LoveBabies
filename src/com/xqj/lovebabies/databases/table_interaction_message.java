package com.xqj.lovebabies.databases;

import java.io.Serializable;

//互动消息列表
@SuppressWarnings("serial")
public class table_interaction_message implements Serializable {
	public final static String message_media_type_text = "[txt]";
	public final static String message_media_type_image = "[img]";
	public final static String message_media_type_voice = "[rec]";
	public final static String message_direction_type_mo = "[mo]";
	public final static String message_direction_type_mt = "[mt]";
	public final static String message_read_status_unread = "[unread]";
	public final static String message_read_status_read = "[read]";
	private int message_id;// 消息ID
	private String message_media_type;// 消息的媒体类别
	private String message_direction_type;// 消息方向类别，是发出去，还是接收到的。
	private String message_content;// 消息内容
	private String message_content_length;// 消息长度
	private String message_occurrence_time;// 消息发生时间
	private String message_sender;// 消息发送人ID
	private String message_receiver;// 消息接受人ID
	private String message_read_status;// 消息的阅读状态

	public String sql_create() {
		String sql = null;
		try {
			sql = "create table if not exists t_interaction_message(";
			sql += "message_id integer,";
			sql += "message_media_type varchar(10),";
			sql += "message_direction_type varchar(10),";
			sql += "message_content varchar(2000),";
			sql += "message_content_length varchar(100),";
			sql += "message_occurrence_time varchar(50),";
			sql += "message_sender varchar(50),";
			sql += "message_receiver varchar(50),";
			sql += "message_read_status varchar(50)";
			sql += ")";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String sql_drop() {
		String sql = null;
		try {
			sql = "drop table t_interaction_message";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public int getMessage_id() {
		return message_id;
	}

	public void setMessage_id(int message_id) {
		this.message_id = message_id;
	}

	public String getMessage_media_type() {
		return message_media_type;
	}

	public void setMessage_media_type(String message_media_type) {
		this.message_media_type = message_media_type;
	}

	public String getMessage_direction_type() {
		return message_direction_type;
	}

	public void setMessage_direction_type(String message_direction_type) {
		this.message_direction_type = message_direction_type;
	}

	public String getMessage_content() {
		return message_content;
	}

	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}

	public String getMessage_content_length() {
		return message_content_length;
	}

	public void setMessage_content_length(String message_content_length) {
		this.message_content_length = message_content_length;
	}

	public String getMessage_occurrence_time() {
		return message_occurrence_time;
	}

	public void setMessage_occurrence_time(String message_occurrence_time) {
		this.message_occurrence_time = message_occurrence_time;
	}

	public String getMessage_sender() {
		return message_sender;
	}

	public void setMessage_sender(String message_sender) {
		this.message_sender = message_sender;
	}

	public String getMessage_receiver() {
		return message_receiver;
	}

	public void setMessage_receiver(String message_receiver) {
		this.message_receiver = message_receiver;
	}

	public String getMessage_read_status() {
		return message_read_status;
	}

	public void setMessage_read_status(String message_read_status) {
		this.message_read_status = message_read_status;
	}

}
