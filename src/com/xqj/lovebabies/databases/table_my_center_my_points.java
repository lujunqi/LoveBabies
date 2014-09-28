package com.xqj.lovebabies.databases;

import java.io.Serializable;

@SuppressWarnings("serial")
public class table_my_center_my_points implements Serializable {
	private String user_id = "";
	private String consume_style = "";
	private String integral_count = "";
	private String time = "";
	private String remark = "";
	
	public String sql_create() {
		String sql = null;
		try {
			sql = "create table if not exists t_my_center_my_points(";
			sql += "user_id varchar(20),";
			sql += "consume_style varchar(200),";
			sql += "integral_count varchar(20),";
			sql += "time varchar(200),";
			sql += "remark varchar(2000)";
			sql += ")";

		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String sql_drop() {
		String sql = null;
		try {
			sql = "drop table t_my_center_my_points";
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
	public String getConsume_style() {
		return consume_style;
	}
	public void setConsume_style(String consume_style) {
		this.consume_style = consume_style;
	}
	public String getIntegral_count() {
		return integral_count;
	}
	public void setIntegral_count(String integral_count) {
		this.integral_count = integral_count;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}
