package com.xqj.lovebabies.structures;

public class interface_app_user_login_req extends interface_head_req {
	public final static String LOGIN_TYPE_ANDROID = "1";
	public final static String LOGIN_TYPE_IOS = "2";
	private String user_name = null;
	private String user_password = null;
	private String login_type = null;
	private String login_code = null;

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public String getLogin_type() {
		return login_type;
	}

	public void setLogin_type(String login_type) {
		this.login_type = login_type;
	}

	public String getLogin_code() {
		return login_code;
	}

	public void setLogin_code(String login_code) {
		this.login_code = login_code;
	}
}
