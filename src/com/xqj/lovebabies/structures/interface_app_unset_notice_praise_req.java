package com.xqj.lovebabies.structures;

public class interface_app_unset_notice_praise_req extends interface_head_req {
	private String notice_id = null;
	private String user_id = null;

	public String getNotice_id() {
		return notice_id;
	}

	public void setNotice_id(String notice_id) {
		this.notice_id = notice_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}
