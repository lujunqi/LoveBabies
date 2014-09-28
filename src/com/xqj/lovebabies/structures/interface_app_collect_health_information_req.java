package com.xqj.lovebabies.structures;

/**
 * 收藏健康育儿资讯请求
 * @author Administrator
 *
 */
public class interface_app_collect_health_information_req extends interface_head_req {
	private String content_id;
	private String user_id;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getContent_id() {
		return content_id;
	}
	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}
	
}
