package com.xqj.lovebabies.structures;

/**
 * 分页查询宝贝成长记录请求
 * @author Administrator
 *
 */
public class interface_app_get_baby_growth_by_page_req extends interface_head_req {
	private String user_id;
	private String baby_id;
	private String page_index;

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

	public String getPage_index() {
		return page_index;
	}

	public void setPage_index(String page_index) {
		this.page_index = page_index;
	}
	
}
