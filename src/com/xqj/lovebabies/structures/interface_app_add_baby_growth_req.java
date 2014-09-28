package com.xqj.lovebabies.structures;

import java.util.List;

/**
 * 添加宝贝成长记录请求
 * @author Administrator
 *
 */
public class interface_app_add_baby_growth_req extends interface_head_req {
	private String user_id;
	private String baby_id;
	private String locations;
	private String word_record;
	private String height;
	private String weight;
	private List<String> growth_pic_list;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBaby_id() {
		return baby_id;
	}

	public void setBaby_id(String baby_id) {
		this.baby_id = baby_id;
	}

	public String getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}

	public String getWord_record() {
		return word_record;
	}

	public void setWord_record(String word_record) {
		this.word_record = word_record;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public List<String> getGrowth_pic_list() {
		return growth_pic_list;
	}

	public void setGrowth_pic_list(List<String> growth_pic_list) {
		this.growth_pic_list = growth_pic_list;
	}
}
