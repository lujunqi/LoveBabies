package com.xqj.lovebabies.structures;

import java.util.List;

import com.xqj.lovebabies.databases.table_album_baby_growth;
import com.xqj.lovebabies.databases.table_health_information;
import com.xqj.lovebabies.databases.table_health_information_top_pic_info;

public class interface_app_get_health_information_resp extends interface_head_resp {
	private int result_code = 0;
	private String return_code = "";
	
	private String action;
	private String page_number;
	private List<table_health_information> list = null;
	private List<table_health_information_top_pic_info> top_list = null;

	public List<table_health_information> getList() {
		return list;
	}

	public void setList(List<table_health_information> list) {
		this.list = list;
	}

	public List<table_health_information_top_pic_info> getTop_list() {
		return top_list;
	}

	public void setTop_list(List<table_health_information_top_pic_info> top_list) {
		this.top_list = top_list;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPage_number() {
		return page_number;
	}

	public void setPage_number(String page_number) {
		this.page_number = page_number;
	}
	
}
