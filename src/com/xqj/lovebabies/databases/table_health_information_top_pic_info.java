package com.xqj.lovebabies.databases;

import java.io.Serializable;

@SuppressWarnings("serial")
public class table_health_information_top_pic_info implements Serializable {
	private int resource_id;
	private String top_id;
	private String title;
	private String pic_name;
	private String s_pic_name;
	
	public String sql_create() {
		String sql = null;
		try {
			sql = "create table if not exists t_health_information_top(";
			sql += "top_id varchar(20),";
			sql += "title varchar(200),";
			sql += "s_pic_name varchar(200),";
			sql += "pic_name varchar(200)";
			sql += ")";

		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String sql_drop() {
		String sql = null;
		try {
			sql = "drop table t_health_information_top";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}
	
	public int getResource_id() {
		return resource_id;
	}
	public void setResource_id(int resource_id) {
		this.resource_id = resource_id;
	}
	public String getTop_id() {
		return top_id;
	}
	public void setTop_id(String top_id) {
		this.top_id = top_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPic_name() {
		return pic_name;
	}
	public void setPic_name(String pic_name) {
		this.pic_name = pic_name;
	}
	public String getS_pic_name() {
		return s_pic_name;
	}
	public void setS_pic_name(String s_pic_name) {
		this.s_pic_name = s_pic_name;
	}
}
