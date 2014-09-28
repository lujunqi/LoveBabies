package com.xqj.lovebabies.structures;

import java.util.List;

import com.xqj.lovebabies.databases.table_album_baby_growth;

public class interface_app_get_baby_growth_by_page_resp extends interface_head_resp {
	private int result_code = 0;
	private String return_code = "";
	private String baby_id;
	
	private List<table_album_baby_growth> list = null;

	public List<table_album_baby_growth> getList() {
		return list;
	}

	public void setList(List<table_album_baby_growth> list) {
		this.list = list;
	}

	public int getResult_code() {
		return result_code;
	}

	public void setResult_code(int result_code) {
		this.result_code = result_code;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getBaby_id() {
		return baby_id;
	}

	public void setBaby_id(String baby_id) {
		this.baby_id = baby_id;
	}
	
}
