package com.xqj.lovebabies.structures;

/**
 * ��ѯ�ҵ��ղ�
 * @author Administrator
 *
 */
public class interface_app_get_my_favorite_req extends interface_head_req {
	private String user_id;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}
