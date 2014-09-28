package com.xqj.lovebabies.structures;

import java.util.List;

/**
 * 删除我的收藏请求
 * @author Administrator
 *
 */
public class interface_app_delete_my_favorite_req extends interface_head_req {
	private String id;
	private String user_id;
	private String content_id;
	
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
	public String getContent_id() {
		return content_id;
	}
	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}

}
