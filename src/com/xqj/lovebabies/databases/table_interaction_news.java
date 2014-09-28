package com.xqj.lovebabies.databases;

import java.io.Serializable;

@SuppressWarnings("serial")
public class table_interaction_news implements Serializable {
	private String news_id = null;
	private String news_org_name = null;
	private String news_title = null;
	private String news_content = null;
	private String user_id = null;
	private String read_count = null;
	private String praise_count = null;
	private String down_count = null;
	private String collect_count = null;
	private String comment_count = null;
	private String share_count = null;
	private String remarks = null;
	private String picture_path = null;
	private String video_path = null;
	private String publish_time = null;

	public String sql_create() {
		String sql = null;
		try {
			sql = "create table if not exists t_interaction_news(";
			sql += "news_id varchar(20),";
			sql += "news_org_name varchar(200),";
			sql += "news_title varchar(200),";
			sql += "news_content varchar(3999),";
			sql += "user_id varchar(50),";
			sql += "read_count varchar(20),";
			sql += "praise_count varchar(20),";
			sql += "down_count varchar(20),";
			sql += "collect_count varchar(20),";
			sql += "comment_count varchar(20),";
			sql += "share_count varchar(20),";
			sql += "remarks varchar(1000),";
			sql += "picture_path varchar(200),";
			sql += "video_path varchar(200),";
			sql += "publish_time varchar(20)";
			sql += ")";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String sql_drop() {
		String sql = null;
		try {
			sql = "drop table t_interaction_news";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String getNews_id() {
		return news_id;
	}

	public void setNews_id(String news_id) {
		this.news_id = news_id;
	}

	public String getNews_org_name() {
		return news_org_name;
	}

	public void setNews_org_name(String news_org_name) {
		this.news_org_name = news_org_name;
	}

	public String getNews_title() {
		return news_title;
	}

	public void setNews_title(String news_title) {
		this.news_title = news_title;
	}

	public String getNews_content() {
		return news_content;
	}

	public void setNews_content(String news_content) {
		this.news_content = news_content;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getRead_count() {
		return read_count;
	}

	public void setRead_count(String read_count) {
		this.read_count = read_count;
	}

	public String getPraise_count() {
		return praise_count;
	}

	public void setPraise_count(String praise_count) {
		this.praise_count = praise_count;
	}

	public String getDown_count() {
		return down_count;
	}

	public void setDown_count(String down_count) {
		this.down_count = down_count;
	}

	public String getCollect_count() {
		return collect_count;
	}

	public void setCollect_count(String collect_count) {
		this.collect_count = collect_count;
	}

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getShare_count() {
		return share_count;
	}

	public void setShare_count(String share_count) {
		this.share_count = share_count;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPicture_path() {
		return picture_path;
	}

	public void setPicture_path(String picture_path) {
		this.picture_path = picture_path;
	}

	public String getVideo_path() {
		return video_path;
	}

	public void setVideo_path(String video_path) {
		this.video_path = video_path;
	}

	public String getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

}
