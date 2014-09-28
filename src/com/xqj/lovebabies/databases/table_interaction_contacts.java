package com.xqj.lovebabies.databases;

import java.io.Serializable;

@SuppressWarnings("serial")
public class table_interaction_contacts implements Serializable {
	public final static String user_status_online = "[online]";
	public final static String user_status_offline = "[offline]";
	public final static String user_sex_man = "男";
	public final static String user_sex_woman = "女";
	private String user_id;// 用户ID
	private String user_nike_name;// 用户昵称
	private String user_real_name;// 用户真实姓名
	private String user_sex;// 用户真实姓名
	private String user_phone;// 用户电话
	private String user_icon_path;// 用户icon网络地址
	private String user_status;// 用户在线情况
	private String user_signature;// 用户的个性签名
	private String user_last_session_time;// 最后会话时间
	private String user_last_session_content;// 最后会话内容
	private String first_letter;// 真实姓名的首字母

	public String sql_create() {
		String sql = null;
		try {
			sql = "create table if not exists t_interaction_contacts(";
			sql += "user_id varchar(20),";
			sql += "user_nike_name varchar(200),";
			sql += "user_real_name varchar(200),";
			sql += "user_sex varchar(10),";
			sql += "user_phone varchar(50),";
			sql += "user_icon_path varchar(500),";
			sql += "user_status varchar(20),";
			sql += "user_last_session_time varchar(20),";
			sql += "user_last_session_content varchar(2000),";
			sql += "first_letter varchar(10),";
			sql += "user_signature varchar(500)";
			sql += ")";

		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String sql_drop() {
		String sql = null;
		try {
			sql = "drop table t_interaction_contacts";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_nike_name() {
		return user_nike_name;
	}

	public void setUser_nike_name(String user_nike_name) {
		this.user_nike_name = user_nike_name;
	}

	public String getUser_real_name() {
		return user_real_name;
	}

	public void setUser_real_name(String user_real_name) {
		this.user_real_name = user_real_name;
	}

	public String getUser_sex() {
		return user_sex;
	}

	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public String getUser_icon_path() {
		return user_icon_path;
	}

	public void setUser_icon_path(String user_icon_path) {
		this.user_icon_path = user_icon_path;
	}

	public String getUser_last_session_time() {
		return user_last_session_time;
	}

	public void setUser_last_session_time(String user_last_session_time) {
		this.user_last_session_time = user_last_session_time;
	}

	public String getUser_last_session_content() {
		return user_last_session_content;
	}

	public void setUser_last_session_content(String user_last_session_content) {
		this.user_last_session_content = user_last_session_content;
	}

	public String getUser_status() {
		return user_status;
	}

	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}

	public String getUser_signature() {
		return user_signature;
	}

	public void setUser_signature(String user_signature) {
		this.user_signature = user_signature;
	}

	public String getFirst_letter() {
		return first_letter;
	}

	public void setFirst_letter(String first_letter) {
		this.first_letter = first_letter;
	}
}
