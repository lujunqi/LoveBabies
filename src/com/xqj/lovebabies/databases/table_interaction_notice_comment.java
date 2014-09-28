package com.xqj.lovebabies.databases;

import java.io.Serializable;

@SuppressWarnings("serial")
public class table_interaction_notice_comment implements Serializable {
	private String notice_id;
	private String comment_user_id;
	private String comment_user_nike_name;
	private String comment_user_icon;
	private String comment_id;
	private String comment_content;
	private String comment_time;

	public String sql_create() {
		String sql = null;
		try {
			sql = "create table if not exists t_interaction_notice_comment(";
			sql += "notice_id varchar(20),";
			sql += "comment_user_id varchar(20),";
			sql += "comment_user_nike_name varchar(20),";
			sql += "comment_user_icon varchar(200),";
			sql += "comment_id varchar(20),";
			sql += "comment_content varchar(200),";
			sql += "comment_time varchar(200)";
			sql += ")";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String sql_drop() {
		String sql = null;
		try {
			sql = "drop table t_interaction_notice_comment";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String getNotice_id() {
		return notice_id;
	}

	public void setNotice_id(String notice_id) {
		this.notice_id = notice_id;
	}

	public String getComment_user_id() {
		return comment_user_id;
	}

	public void setComment_user_id(String comment_user_id) {
		this.comment_user_id = comment_user_id;
	}

	public String getComment_user_nike_name() {
		return comment_user_nike_name;
	}

	public void setComment_user_nike_name(String comment_user_nike_name) {
		this.comment_user_nike_name = comment_user_nike_name;
	}

	public String getComment_user_icon() {
		return comment_user_icon;
	}

	public void setComment_user_icon(String comment_user_icon) {
		this.comment_user_icon = comment_user_icon;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public String getComment_time() {
		return comment_time;
	}

	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}

}
