package com.xqj.lovebabies.structures;

/**
 * 生成验证码
 * @author Administrator
 *
 */
public class interface_app_get_verify_code_req extends interface_head_req {
	private String phone;
	private String reason;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
