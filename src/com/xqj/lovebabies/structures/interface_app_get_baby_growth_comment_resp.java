package com.xqj.lovebabies.structures;

import java.util.List;

import com.xqj.lovebabies.databases.table_album_growth_comment;
import com.xqj.lovebabies.databases.table_album_my_baby;

public class interface_app_get_baby_growth_comment_resp extends interface_head_resp{
	public final String SUCCESS = "[-1]";// 未填写成长记录ID或用户ID
	public final String FAILED = "[-9]";//	 其他错误
	private List<table_album_growth_comment> list = null;
	private String result_code;
	private String return_code;
	
	public List<table_album_growth_comment> getList() {
		return list;
	}

	public void setList(List<table_album_growth_comment> list) {
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
