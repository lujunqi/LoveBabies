package com.xqj.lovebabies.databases;

import java.io.Serializable;

@SuppressWarnings("serial")
public class table_interaction_notice_praise implements Serializable {
	private String notice_id;
	private String praise_user_id;
	private String praise_user_nike_name;
	private String praise_user_icon;

	public String sql_create() {
		String sql = null;
		try {
			sql = "create table if not exists t_interaction_notice_praise(";
			sql += "notice_id varchar(20),";
			sql += "praise_user_id varchar(20),";
			sql += "praise_user_nike_name varchar(20),";
			sql += "praise_user_icon varchar(200)";
			sql += ")";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String sql_drop() {
		String sql = null;
		try {
			sql = "drop table t_interaction_notice_praise";
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

	public String getPraise_user_id() {
		return praise_user_id;
	}

	public void setPraise_user_id(String praise_user_id) {
		this.praise_user_id = praise_user_id;
	}

	public String getPraise_user_nike_name() {
		return praise_user_nike_name;
	}

	public void setPraise_user_nike_name(String praise_user_nike_name) {
		this.praise_user_nike_name = praise_user_nike_name;
	}

	public String getPraise_user_icon() {
		return praise_user_icon;
	}

	public void setPraise_user_icon(String praise_user_icon) {
		this.praise_user_icon = praise_user_icon;
	}

}
