package com.xqj.lovebabies.databases;

import java.io.Serializable;

@SuppressWarnings("serial")
public class table_album_my_baby implements Serializable {
	private String baby_id;// 宝宝ID
	private String user_id;// 用户ID
	private String baby_name;// 宝宝名字
	private String baby_sex;// 宝宝性别
	private String birthday;// 宝宝生日
	private String baby_pic;// 宝宝头像
	private String relation;
	private String baby_age;
	private String permissons="0";// 宝贝操作权限，1：有删除修改权限
	

	public String sql_create() {
		String sql = null;
		try {
			sql = "create table if not exists t_baby_my_baby(";
			sql += "baby_id varchar(20),";
			sql += "user_id varchar(20),";
			sql += "baby_name varchar(200),";
			sql += "baby_sex varchar(10),";
			sql += "birthday varchar(50),";
			sql += "baby_pic varchar(500),";
			sql += "relation varchar(200),";
			sql += "permissons varchar(20),";
			sql += "baby_age varchar(20)";
			sql += ")";

		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String sql_drop() {
		String sql = null;
		try {
			sql = "drop table t_baby_my_baby";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	
	public String getBaby_id() {
		return baby_id;
	}
	public void setBaby_id(String baby_id) {
		this.baby_id = baby_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getBaby_name() {
		return baby_name;
	}
	public void setBaby_name(String baby_name) {
		this.baby_name = baby_name;
	}
	public String getBaby_sex() {
		return baby_sex;
	}
	public void setBaby_sex(String baby_sex) {
		this.baby_sex = baby_sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getBaby_pic() {
		return baby_pic;
	}
	public void setBaby_pic(String baby_pic) {
		this.baby_pic = baby_pic;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getBaby_age() {
		return baby_age;
	}
	public void setBaby_age(String baby_age) {
		this.baby_age = baby_age;
	}

	public String getPermissons() {
		return permissons;
	}

	public void setPermissons(String permissons) {
		this.permissons = permissons;
	}
	
}
