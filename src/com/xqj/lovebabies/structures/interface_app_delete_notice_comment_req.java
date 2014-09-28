package com.xqj.lovebabies.structures;

public class interface_app_delete_notice_comment_req extends interface_head_req {
	private String user_id = null;
	private String comment_id = null;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
}
