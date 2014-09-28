package com.xqj.lovebabies.structures;

public class interface_app_get_notice_list_req extends interface_head_req {
	private int user_id;
	private int page_number;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getPage_number() {
		return page_number;
	}

	public void setPage_number(int page_number) {
		this.page_number = page_number;
	}

}
