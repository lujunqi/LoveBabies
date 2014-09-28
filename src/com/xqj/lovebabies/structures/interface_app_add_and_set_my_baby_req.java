package com.xqj.lovebabies.structures;

import java.util.List;

/**
 * Ìí¼Ó\ÐÞ¸Ä±¦±´ÇëÇó
 * @author Administrator
 *
 */
public class interface_app_add_and_set_my_baby_req extends interface_head_req {
	private String user_id="";
	private String baby_id="";
	private String baby_name="";
	private String birthday="";
	private String sex="";
	private String relation="";
	private String age="";
	private String upload_file="";

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBaby_id() {
		return baby_id;
	}

	public void setBaby_id(String baby_id) {
		this.baby_id = baby_id;
	}

	public String getBaby_name() {
		return baby_name;
	}

	public void setBaby_name(String baby_name) {
		this.baby_name = baby_name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getUpload_file() {
		return upload_file;
	}

	public void setUpload_file(String upload_file) {
		this.upload_file = upload_file;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	
}
