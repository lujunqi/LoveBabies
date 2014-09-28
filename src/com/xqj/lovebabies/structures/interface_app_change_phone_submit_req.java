package com.xqj.lovebabies.structures;

/**
 * ¸ü»»ÊÖ»úºÅ\ÕËºÅ
 * @author Administrator
 *
 */
public class interface_app_change_phone_submit_req extends interface_head_req {
	private String v_code;
	private String new_phone;
	private String old_phone;
	
	public String getV_code() {
		return v_code;
	}
	public void setV_code(String v_code) {
		this.v_code = v_code;
	}
	public String getNew_phone() {
		return new_phone;
	}
	public void setNew_phone(String new_phone) {
		this.new_phone = new_phone;
	}
	public String getOld_phone() {
		return old_phone;
	}
	public void setOld_phone(String old_phone) {
		this.old_phone = old_phone;
	}
}
