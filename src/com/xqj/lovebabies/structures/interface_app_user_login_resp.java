package com.xqj.lovebabies.structures;

public class interface_app_user_login_resp extends interface_head_resp {
	public final static String USER_TYPE_SCHOOLMASTER = "1";
	public final static String USER_TYPE_TEACHER = "2";
	public final static String USER_TYPE_PARENTS = "3";
	private String user_id = null;
	private String user_icon = null;
	private String user_type = null;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_icon() {
		return user_icon;
	}

	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

}
