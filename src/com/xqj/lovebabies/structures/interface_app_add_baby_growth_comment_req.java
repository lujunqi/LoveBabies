package com.xqj.lovebabies.structures;

import com.xqj.lovebabies.contants.global_contants;

/**
 * 添加宝贝成长记录评论请求
 * @author Administrator
 *
 */
public class interface_app_add_baby_growth_comment_req extends interface_head_req {
	private String record_id;
	private String user_id;
	private String comm_content;
	private String parent_comm_id;// 父评论ID
	private String comm_place;	// 地址zuobia
	private String comm_machion = global_contants.oper_mation;// 评论机器
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

	public String getComm_content() {
		return comm_content;
	}

	public void setComm_content(String comm_content) {
		this.comm_content = comm_content;
	}

	public String getParent_comm_id() {
		return parent_comm_id;
	}

	public void setParent_comm_id(String parent_comm_id) {
		this.parent_comm_id = parent_comm_id;
	}

	public String getComm_place() {
		return comm_place;
	}

	public void setComm_place(String comm_place) {
		this.comm_place = comm_place;
	}

	public String getComm_machion() {
		return comm_machion;
	}

	public void setComm_machion(String comm_machion) {
		this.comm_machion = comm_machion;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
