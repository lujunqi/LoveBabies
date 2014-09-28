package com.xqj.lovebabies.structures;

import java.util.List;

import com.xqj.lovebabies.databases.table_my_center_baby_relations;
import com.xqj.lovebabies.databases.table_my_center_my_care_baby;
import com.xqj.lovebabies.databases.table_my_center_my_points;
import com.xqj.lovebabies.databases.table_my_center_record_rules;

public class interface_app_get_total_point_resp extends interface_head_resp{
	private String result_code;
	private String return_code;
	private String id;
	private String user_id;
	private String total_integral;
	private String user_grade;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTotal_integral() {
		return total_integral;
	}

	public void setTotal_integral(String total_integral) {
		this.total_integral = total_integral;
	}

	public String getUser_grade() {
		return user_grade;
	}

	public void setUser_grade(String user_grade) {
		this.user_grade = user_grade;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	
	
}
