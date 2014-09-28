package com.xqj.lovebabies.structures;

import java.util.List;

/**
 * É¾³ı±¦±´ÇëÇó
 * @author Administrator
 *
 */
public class interface_app_delete_baby_req extends interface_head_req {
	private String user_id;
	private String baby_id;

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

	


}
