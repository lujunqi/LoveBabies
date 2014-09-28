package com.xqj.lovebabies.databases;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class table_album_growth_comment implements Serializable {
	private String comm_id;
	private String user_id;
	private String comment_nick_name;// 评论人的昵称
	private String user_pic;	// 评论人图片
	private String comm_content;
	private String comm_time;
	
	private List<table_album_growth_praise> zan_user_list;// 点赞人员列表
	private String if_praised;//  是否已赞

	public String getComm_id() {
		return comm_id;
	}

	public void setComm_id(String comm_id) {
		this.comm_id = comm_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getComment_nick_name() {
		return comment_nick_name;
	}

	public void setComment_nick_name(String comment_nick_name) {
		this.comment_nick_name = comment_nick_name;
	}

	public String getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}

	public String getComm_content() {
		return comm_content;
	}

	public void setComm_content(String comm_content) {
		this.comm_content = comm_content;
	}

	public String getComm_time() {
		return comm_time;
	}

	public void setComm_time(String comm_time) {
		this.comm_time = comm_time;
	}

	public List<table_album_growth_praise> getZan_user_list() {
		return zan_user_list;
	}

	public void setZan_user_list(List<table_album_growth_praise> zan_user_list) {
		this.zan_user_list = zan_user_list;
	}

	public String getIf_praised() {
		return if_praised;
	}

	public void setIf_praised(String if_praised) {
		this.if_praised = if_praised;
	}
	
	
}
