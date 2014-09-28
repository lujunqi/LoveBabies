package com.xqj.lovebabies.structures;

import java.io.Serializable;

public class interface_app_get_user_detail_resp extends interface_head_resp implements Serializable {
	private String user_id;// 用户id
	private String user_phone;// 登录用户名/手机号码
	private String user_password;
	private String user_nike_name;
	private String user_real_name;
	private String user_sex;
	private String user_signature;
	private String user_email;
	private String user_icon;
	private String user_integral;
	private String vaccine_remind_time;
	private String vaccine_remind_status;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
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

	public String getUser_signature() {
		return user_signature;
	}

	public void setUser_signature(String user_signature) {
		this.user_signature = user_signature;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_icon() {
		return user_icon;
	}

	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
	}

	public String getUser_integral() {
		return user_integral;
	}

	public void setUser_integral(String user_integral) {
		this.user_integral = user_integral;
	}

	public String getVaccine_remind_time() {
		return vaccine_remind_time;
	}

	public void setVaccine_remind_time(String vaccine_remind_time) {
		this.vaccine_remind_time = vaccine_remind_time;
	}

	public String getVaccine_remind_status() {
		return vaccine_remind_status;
	}

	public void setVaccine_remind_status(String vaccine_remind_status) {
		this.vaccine_remind_status = vaccine_remind_status;
	}

}
