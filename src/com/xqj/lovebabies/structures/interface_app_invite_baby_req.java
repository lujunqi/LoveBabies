package com.xqj.lovebabies.structures;

/**
 * ÊäÈëÑûÇëÂëÌí¼Ó±¦±¦
 * @author Administrator
 *
 */
public class interface_app_invite_baby_req extends interface_head_req {
	private String user_id;
	private String invitation_code;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getInvitation_code() {
		return invitation_code;
	}
	public void setInvitation_code(String invitation_code) {
		this.invitation_code = invitation_code;
	}
}
