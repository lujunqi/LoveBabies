package com.xqj.lovebabies.structures;

/**
 * ÷ÿ÷√√‹¬Î
 * @author Administrator
 *
 */
public class interface_app_reset_password_req extends interface_head_req {
	private String phone;
	private String v_code;
	private String password;
	
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
	
}
