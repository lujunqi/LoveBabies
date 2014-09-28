package com.xqj.lovebabies.structures;

/**
 * 获取所有我的宝贝请求
 * @author Administrator
 *
 */
public class interface_app_get_all_my_baby_req extends interface_head_req {
	private String user_id;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	
}
