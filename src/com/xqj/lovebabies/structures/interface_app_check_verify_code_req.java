package com.xqj.lovebabies.structures;

/**
 * 验证手机号以及验证码
 * @author Administrator
 *
 */
public class interface_app_check_verify_code_req extends interface_head_req {
	private String phone;
	private String v_code;
	
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
	
}
