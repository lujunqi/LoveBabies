package com.xqj.lovebabies.structures;

/**
 * 查询我的积分
 * @author Administrator
 *
 */
public class interface_app_get_my_points_req extends interface_head_req {
	private String user_id;
	private String index;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}

}
