package com.xqj.lovebabies.databases;

import java.io.Serializable;

/**
 * 点赞用户列表的人员信息
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class table_album_growth_praise implements Serializable {
	private String user_id;
	private String user_nick_name;
	private String user_pick;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_nick_name() {
		return user_nick_name;
	}
	public void setUser_nick_name(String user_nick_name) {
		this.user_nick_name = user_nick_name;
	}
	public String getUser_pick() {
		return user_pick;
	}
	public void setUser_pick(String user_pick) {
		this.user_pick = user_pick;
	}
	
	
}
