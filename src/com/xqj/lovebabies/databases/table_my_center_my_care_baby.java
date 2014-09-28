package com.xqj.lovebabies.databases;

import java.io.Serializable;

@SuppressWarnings("serial")
public class table_my_center_my_care_baby implements Serializable {
	private int resource_id;
	private String baby_id;
	private String user_id;
	private String baby_name;
	private String birthday;
	private String relations;
	private String baby_sex;
	private String baby_pic;
	public int getResource_id() {
		return resource_id;
	}
	public void setResource_id(int resource_id) {
		this.resource_id = resource_id;
	}
	public String getBaby_name() {
		return baby_name;
	}
	public void setBaby_name(String baby_name) {
		this.baby_name = baby_name;
	}
	public String getRelations() {
		return relations;
	}
	public void setRelations(String relations) {
		this.relations = relations;
	}
	public String getBaby_id() {
		return baby_id;
	}
	public void setBaby_id(String baby_id) {
		this.baby_id = baby_id;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getBaby_sex() {
		return baby_sex;
	}
	public void setBaby_sex(String baby_sex) {
		this.baby_sex = baby_sex;
	}
	public String getBaby_pic() {
		return baby_pic;
	}
	public void setBaby_pic(String baby_pic) {
		this.baby_pic = baby_pic;
	}
	
}
