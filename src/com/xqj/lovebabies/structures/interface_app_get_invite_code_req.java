package com.xqj.lovebabies.structures;

import java.util.List;

/**
 * Éú³ÉÑûÇëÂë
 * @author Administrator
 *
 */
public class interface_app_get_invite_code_req extends interface_head_req {
	private String baby_id;
	private String relation;
	private String permissions;
	
	public String getBaby_id() {
		return baby_id;
	}
	public void setBaby_id(String baby_id) {
		this.baby_id = baby_id;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getPermissions() {
		return permissions;
	}
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

}
