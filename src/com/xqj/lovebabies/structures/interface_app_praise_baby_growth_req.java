package com.xqj.lovebabies.structures;

import java.util.List;

import com.xqj.lovebabies.contants.global_contants;

/**
 * 点赞宝贝成长记录请求
 * @author Administrator
 *
 */
public class interface_app_praise_baby_growth_req extends interface_head_req {
	private String user_id;
	private String record_id;
	private String location;
	private String oper_mation = global_contants.oper_mation;
	private String remark;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getRecord_id() {
		return record_id;
	}

	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOper_mation() {
		return oper_mation;
	}

	public void setOper_mation(String oper_mation) {
		this.oper_mation = oper_mation;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
