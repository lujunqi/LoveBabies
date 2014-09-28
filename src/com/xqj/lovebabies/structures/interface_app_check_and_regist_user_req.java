package com.xqj.lovebabies.structures;

/**
 * 验证手通过后添加用户
 * @author Administrator
 *
 */
public class interface_app_check_and_regist_user_req extends interface_head_req {
	private String phone;
	private String v_code;
	private String password;
	private String nick_name;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getV_code() {
		return v_code;
	}
	public void setV_code(String v_code) {
		this.v_code = v_code;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	
}
