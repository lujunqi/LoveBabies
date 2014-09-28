package com.xqj.lovebabies.databases;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * 健康育儿资讯参数
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class table_health_information implements Serializable {
	private String content_id;//记录ID
	private String title;
	private String content;
	private String publish_time;
	private String collect_count;
	private String share_count;
	private String source;
	private String pic_name;
	private String s_pic_name;
	
	public String sql_create() {
		String sql = null;
		try {
			sql = "create table if not exists t_health_information(";
			sql += "content_id varchar(20),";
			sql += "title varchar(200),";
			sql += "content varchar(5000),";
			sql += "publish_time varchar(50),";
			sql += "collect_count varchar(20),";
			sql += "share_count varchar(20),";
			sql += "source varchar(200),";
			sql += "s_pic_name varchar(200),";
			sql += "pic_name varchar(200)";
			sql += ")";

		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String sql_drop() {
		String sql = null;
		try {
			sql = "drop table t_health_information";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String getContent_id() {
		return content_id;
	}
	public void setContent_id(String content_id) {
		this.content_id = content_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPublish_time() {
		return publish_time;
	}
	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public String getCollect_count() {
		return collect_count;
	}
	public void setCollect_count(String collect_count) {
		this.collect_count = collect_count;
	}
	public String getShare_count() {
		return share_count;
	}
	public void setShare_count(String share_count) {
		this.share_count = share_count;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPic_name() {
		return pic_name;
	}
	public void setPic_name(String pic_name) {
		this.pic_name = pic_name;
	}
	public String getS_pic_name() {
		return s_pic_name;
	}
	public void setS_pic_name(String s_pic_name) {
		this.s_pic_name = s_pic_name;
	}
	
}
