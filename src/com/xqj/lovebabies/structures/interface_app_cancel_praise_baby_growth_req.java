package com.xqj.lovebabies.structures;

/**
 * 取消点赞宝贝成长记录请求
 * @author Administrator
 *
 */
public class interface_app_cancel_praise_baby_growth_req extends interface_head_req {
	private String user_id;
	private String record_id;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getRecord_id() {
		return record_id;
	}

	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}

}
