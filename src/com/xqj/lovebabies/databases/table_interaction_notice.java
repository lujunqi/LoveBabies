package com.xqj.lovebabies.databases;

import java.io.Serializable;

@SuppressWarnings("serial")
public class table_interaction_notice implements Serializable {
	private String notice_id = null;// 公告ID
	private String notice_type_name = null;// 公告类型名称
	private String notice_title = null;// 公告标题
	private String notice_picture_detailed = null;// 公告图片完整图
	private String notice_picture_breviary = null;// 公告图片缩略图
	private String notice_content;// 公告内容
	private String notice_sender_id;// 发送用户ID
	private String notice_sender_icon;// 发送用户头像
	private String notice_sender_name;// 发送用户名称
	private String notice_publish_time;// 公告发布时间
	private String notice_org_name;// 幼儿园机构名称
	private String notice_read_count;// 阅读次数
	private String notice_praise_count;// 点赞次数
	private String notice_download_count;// 下载次数
	private String notice_collect_count;// 收藏次数
	private String notice_comment_count;// 评论次数
	private String notice_share_count;// 分享次数

	public String sql_create() {
		String sql = null;
		try {
			sql = "create table if not exists t_interaction_notice(";
			sql += "notice_id varchar(50),";
			sql += "notice_type_name varchar(50),";
			sql += "notice_title varchar(500),";
			sql += "notice_picture_detailed varchar(2000),";
			sql += "notice_picture_breviary varchar(2000),";
			sql += "notice_content varchar(3900),";
			sql += "notice_sender_id varchar(50),";
			sql += "notice_sender_icon varchar(100),";
			sql += "notice_sender_name varchar(100),";
			sql += "notice_publish_time varchar(50),";
			sql += "notice_org_name varchar(50),";
			sql += "notice_read_count varchar(20),";
			sql += "notice_praise_count varchar(20),";
			sql += "notice_download_count varchar(20),";
			sql += "notice_collect_count varchar(20),";
			sql += "notice_comment_count varchar(20),";
			sql += "notice_share_count varchar(20)";
			sql += ")";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String sql_drop() {
		String sql = null;
		try {
			sql = "drop table t_interaction_notice";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String getNotice_id() {
		return notice_id;
	}

	public void setNotice_id(String notice_id) {
		this.notice_id = notice_id;
	}

	public String getNotice_type_name() {
		return notice_type_name;
	}

	public void setNotice_type_name(String notice_type_name) {
		this.notice_type_name = notice_type_name;
	}

	public String getNotice_title() {
		return notice_title;
	}

	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}

	public String getNotice_content() {
		return notice_content;
	}

	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}

	public String getNotice_sender_id() {
		return notice_sender_id;
	}

	public void setNotice_sender_id(String notice_sender_id) {
		this.notice_sender_id = notice_sender_id;
	}

	public String getNotice_sender_name() {
		return notice_sender_name;
	}

	public void setNotice_sender_name(String notice_sender_name) {
		this.notice_sender_name = notice_sender_name;
	}

	public String getNotice_publish_time() {
		return notice_publish_time;
	}

	public void setNotice_publish_time(String notice_publish_time) {
		this.notice_publish_time = notice_publish_time;
	}

	public String getNotice_org_name() {
		return notice_org_name;
	}

	public void setNotice_org_name(String notice_org_name) {
		this.notice_org_name = notice_org_name;
	}

	public String getNotice_picture_detailed() {
		return notice_picture_detailed;
	}

	public void setNotice_picture_detailed(String notice_picture_detailed) {
		this.notice_picture_detailed = notice_picture_detailed;
	}

	public String getNotice_picture_breviary() {
		return notice_picture_breviary;
	}

	public void setNotice_picture_breviary(String notice_picture_breviary) {
		this.notice_picture_breviary = notice_picture_breviary;
	}

	public String getNotice_sender_icon() {
		return notice_sender_icon;
	}

	public void setNotice_sender_icon(String notice_sender_icon) {
		this.notice_sender_icon = notice_sender_icon;
	}

	public String getNotice_read_count() {
		return notice_read_count;
	}

	public void setNotice_read_count(String notice_read_count) {
		this.notice_read_count = notice_read_count;
	}

	public String getNotice_praise_count() {
		return notice_praise_count;
	}

	public void setNotice_praise_count(String notice_praise_count) {
		this.notice_praise_count = notice_praise_count;
	}

	public String getNotice_download_count() {
		return notice_download_count;
	}

	public void setNotice_download_count(String notice_download_count) {
		this.notice_download_count = notice_download_count;
	}

	public String getNotice_collect_count() {
		return notice_collect_count;
	}

	public void setNotice_collect_count(String notice_collect_count) {
		this.notice_collect_count = notice_collect_count;
	}

	public String getNotice_comment_count() {
		return notice_comment_count;
	}

	public void setNotice_comment_count(String notice_comment_count) {
		this.notice_comment_count = notice_comment_count;
	}

	public String getNotice_share_count() {
		return notice_share_count;
	}

	public void setNotice_share_count(String notice_share_count) {
		this.notice_share_count = notice_share_count;
	}

}
