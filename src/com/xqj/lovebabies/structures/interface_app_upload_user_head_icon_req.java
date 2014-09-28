package com.xqj.lovebabies.structures;

/**
 * 更新用户头像
 * @author Administrator
 *
 */
public class interface_app_upload_user_head_icon_req extends interface_head_req {
	private String user_id;
	private String image_path;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}
	
}
