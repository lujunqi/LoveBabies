package com.xqj.lovebabies.databases;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * 宝宝相册参数
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class table_album_baby_growth implements Serializable {
	private String record_id;//记录ID
	private String baby_id;// 宝宝ID
	private String record_time;//记录时间
	private String locations;// 记录位置
	private String age_true;// 宝宝年龄
	private String weight; // 宝宝体重
	private String height;	// 宝宝身高
	private String word_record;//文字记录
	private String pic_name;// 图片文件名
	private String s_pic_name;//图片大图文件名
	private Vector<table_album_gridview_photo_path> pic_list;// 图片文件列表
	private String permissions;
	
	public String sql_create() {
		String sql = null;
		try {
			sql = "create table if not exists t_album_baby_growth(";
			sql += "baby_id varchar(20),";
			sql += "record_id varchar(20),";
			sql += "record_time varchar(200),";
			sql += "locations varchar(200),";
			sql += "age_true varchar(20),";
			sql += "weight varchar(20),";
			sql += "height varchar(20),";
			sql += "pic_name varchar(500),";
			sql += "word_record varchar(2000)";
			sql += ")";

		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}

	public String sql_drop() {
		String sql = null;
		try {
			sql = "drop table t_album_baby_growth";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sql;
	}
	
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	public String getBaby_id() {
		return baby_id;
	}
	public void setBaby_id(String baby_id) {
		this.baby_id = baby_id;
	}
	public String getRecord_time() {
		return record_time;
	}
	public void setRecord_time(String record_time) {
		this.record_time = record_time;
	}
	public String getLocations() {
		return locations;
	}
	public void setLocations(String locations) {
		this.locations = locations;
	}
	public String getAge_true() {
		return age_true;
	}
	public void setAge_true(String age_true) {
		this.age_true = age_true;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWord_record() {
		return word_record;
	}
	public void setWord_record(String word_record) {
		this.word_record = word_record;
	}
	public String getPic_name() {
		return pic_name;
	}
	public void setPic_name(String pic_name) {
		this.pic_name = pic_name;
	}
	public Vector<table_album_gridview_photo_path> getPic_list() {
		return pic_list;
	}
	public void setPic_list(Vector<table_album_gridview_photo_path> pic_list) {
		this.pic_list = pic_list;
	}

	public String getS_pic_name() {
		return s_pic_name;
	}

	public void setS_pic_name(String s_pic_name) {
		this.s_pic_name = s_pic_name;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
}
