package com.xqj.lovebabies.structures;

public class interface_app_get_notice_praise_list_req extends interface_head_req {
	private String notice_id = "";
	private String user_id = "";

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
