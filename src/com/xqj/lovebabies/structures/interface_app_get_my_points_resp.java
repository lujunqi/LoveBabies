package com.xqj.lovebabies.structures;

import java.util.List;

import com.xqj.lovebabies.databases.table_my_center_baby_relations;
import com.xqj.lovebabies.databases.table_my_center_my_care_baby;
import com.xqj.lovebabies.databases.table_my_center_my_points;

public class interface_app_get_my_points_resp extends interface_head_resp{
	private String result_code;
	private String return_code;
	private String index;
	
	private List<table_my_center_my_points> list = null;

	public List<table_my_center_my_points> getList() {
		return list;
	}

	public void setList(List<table_my_center_my_points> list) {
		this.list = list;
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

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	
	
}
