package com.xqj.lovebabies.structures;

import java.util.List;

import com.xqj.lovebabies.databases.table_my_center_baby_relations;
import com.xqj.lovebabies.databases.table_my_center_my_care_baby;

public class interface_app_get_baby_relations_resp extends interface_head_resp{
	private String result_code;
	private String return_code;
	
	private List<table_my_center_baby_relations> list = null;

	public List<table_my_center_baby_relations> getList() {
		return list;
	}

	public void setList(List<table_my_center_baby_relations> list) {
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
	
	
}
