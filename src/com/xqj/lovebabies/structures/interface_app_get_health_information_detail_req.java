package com.xqj.lovebabies.structures;

/**
 * 查询健康育儿资讯详情请求
 * @author Administrator
 *
 */
public class interface_app_get_health_information_detail_req extends interface_head_req {
	private String id;
	private String user_id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
}
