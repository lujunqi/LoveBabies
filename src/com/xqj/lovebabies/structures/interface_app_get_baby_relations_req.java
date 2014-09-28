package com.xqj.lovebabies.structures;

/**
 * 获取宝贝亲友列表请求
 * @author Administrator
 *
 */
public class interface_app_get_baby_relations_req extends interface_head_req {
	private String baby_id;

	public String getBaby_id() {
		return baby_id;
	}

	public void setBaby_id(String baby_id) {
		this.baby_id = baby_id;
	}
	
}
